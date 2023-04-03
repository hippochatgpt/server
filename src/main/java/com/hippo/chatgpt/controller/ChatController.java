package com.hippo.chatgpt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hippo.chatgpt.client.OpenAiClient;
import com.hippo.chatgpt.client.OpenAiStreamClient;
import com.hippo.chatgpt.entity.chat.ChatCompletion;
import com.hippo.chatgpt.entity.chat.Message;
import com.hippo.chatgpt.entity.database.ChatInfoEntity;
import com.hippo.chatgpt.entity.database.ChatMessageEntity;
import com.hippo.chatgpt.service.ChatInfoService;
import com.hippo.chatgpt.service.ChatMessageService;
import com.hippo.chatgpt.sse.ConsoleEventSourceListener;
import com.hippo.chatgpt.sse.SSEServer;
import com.hippo.chatgpt.utils.GenerateUtil;
import com.hippo.chatgpt.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiewei
 */
@RestController
@RequestMapping("/v1")
@Slf4j
public class ChatController {

    @Resource
    private OpenAiStreamClient openAiStreamClient;
    @Resource
    private OpenAiClient openAiClient;
    @Resource
    private ChatInfoService chatInfoService;
    @Resource
    private ChatMessageService chatMessageService;

    private static final String USER_ID = "userId";

    private static final String CHAT_ID = "chatId";

    @PostMapping("/chat/completions")
    public ResultUtil chatCompletions(HttpServletRequest request, @RequestBody Message message){
        String chatId = request.getHeader(CHAT_ID);
        QueryWrapper<ChatMessageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("chat_id", chatId).eq("rm_status", 1));
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.last("limit 20");
        List<ChatMessageEntity> messageEntityList = chatMessageService.list(queryWrapper);
        int countChar = GenerateUtil.countChar(message.getContent());
        int max = 4096-2500;
        List<Message> messages = new ArrayList<>();
        for (ChatMessageEntity chatMessage : messageEntityList) {
            countChar += GenerateUtil.countChar(chatMessage.getContent());
            if (countChar < max) {
                messages.add(Message.builder()
                        .role(chatMessage.getType() == 1 ? Message.Role.USER : Message.Role.ASSISTANT)
                        .content(chatMessage.getContent())
                        .build());
            } else {
                break;
            }
        }
        message.setRole(Message.Role.USER.getName());
        messages.add(message);
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO_0301.getName())
                .temperature(0.2)
                .maxTokens(2500)
                .messages(messages)
                .stream(true)
                .build();
        log.info("message: {}", message.getContent());
        ChatMessageEntity chatMessage = new ChatMessageEntity();
        chatMessage.setType(1);
        chatMessage.setChatId(Long.parseLong(chatId));
        chatMessage.setContent(message.getContent());
        chatMessageService.save(chatMessage);
        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener(chatMessageService, chatId);
        openAiStreamClient.streamChatCompletion(chatCompletion, eventSourceListener);
        return ResultUtil.ok();
    }

    @GetMapping("/models")
    public ResultUtil models(){
        return ResultUtil.ok().data(openAiClient.models());
    }

    @GetMapping("/connect/{chatId}")
    public SseEmitter connect(@PathVariable String chatId){
        return SSEServer.connect(chatId);
    }

    /**
     * 新建会话
     * @param request
     * @param chat
     * @return
     */
    @PostMapping("/new/chat")
    public ResultUtil newChat(HttpServletRequest request, @RequestBody ChatInfoEntity chat){
        String userId = request.getHeader(USER_ID);
        chat.setUserId(Long.parseLong(userId));
        chatInfoService.save(chat);
        return ResultUtil.ok().data(chat.getId());
    }

    /**
     * 编辑会话名称
     * @param request
     * @param chat
     * @return
     */
    @PostMapping("/edit/chat")
    public ResultUtil editChat(HttpServletRequest request, @RequestBody ChatInfoEntity chat){
        String userId = request.getHeader(USER_ID);
        chat.setUserId(Long.parseLong(userId));
        chatInfoService.updateById(chat);
        return ResultUtil.ok().data(chat.getId());
    }

    /**
     * 查询会话列表
     * @param request
     * @param pageSize
     * @param currentPage
     * @return
     */
    @GetMapping("/chat/list")
    public ResultUtil chatList(HttpServletRequest request,
                               @RequestParam(value = "pageSize", defaultValue = "20", required = false) Integer pageSize,
                               @RequestParam(value = "currentPage", defaultValue = "1", required = false) Integer currentPage) {
        String userId = request.getHeader(USER_ID);
        //分页查询
        Page<ChatInfoEntity> pageParam = new Page<>();
        pageParam.setCurrent(currentPage);
        pageParam.setSize(pageSize);
        QueryWrapper<ChatInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", userId).eq("rm_status", 1));
        queryWrapper.orderByDesc("gmt_create");
        Page<ChatInfoEntity> res = chatInfoService.page(pageParam, queryWrapper);
        return ResultUtil.ok(res);
    }

    /**
     * 删除会话
     * @param chatId
     * @return
     */
    @GetMapping("/chat/rmSingle")
    public ResultUtil chatRmSingle(@RequestParam(value = "chatId") String chatId) {
        ChatInfoEntity chatInfo = chatInfoService.getById(chatId);
        chatInfo.setRmStatus(0);
        return ResultUtil.ok(chatInfoService.updateById(chatInfo));
    }

    /**
     * 清除所有会话
     * @param request
     * @return
     */
    @GetMapping("/chat/rmAll")
    public ResultUtil chatRmAll(HttpServletRequest request) {
        String userId = request.getHeader(USER_ID);
        UpdateWrapper<ChatInfoEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        updateWrapper.set("rm_status", 0);
        return ResultUtil.ok(chatInfoService.update(updateWrapper));
    }

    /**
     * 查询消息列表
     * @param chatId
     * @param pageSize
     * @param currentPage
     * @return
     */
    @GetMapping("/message/list")
    public ResultUtil messageList(@RequestParam(value = "chatId") String chatId,
                                  @RequestParam(value = "pageSize", defaultValue = "20", required = false) Integer pageSize,
                                  @RequestParam(value = "currentPage", defaultValue = "1", required = false) Integer currentPage) {
        //分页查询
        Page<ChatMessageEntity> pageParam = new Page<>();
        pageParam.setCurrent(currentPage);
        pageParam.setSize(pageSize);
        QueryWrapper<ChatMessageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("chat_id", chatId).eq("rm_status", 1));
        queryWrapper.orderByDesc("gmt_create");
        return ResultUtil.ok(chatMessageService.page(pageParam, queryWrapper));
    }

}
