package com.hippo.chatgpt.sse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hippo.chatgpt.entity.chat.ChatResponse;
import com.hippo.chatgpt.entity.database.ChatMessageEntity;
import com.hippo.chatgpt.service.ChatMessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 描述： sse
 *
 * @author xiewei
 */
@Slf4j
public class ConsoleEventSourceListener extends EventSourceListener {

    private final String chatId;

    private final ChatMessageService chatMessageService;

    private StringBuilder stringBuilder;

    public ConsoleEventSourceListener(ChatMessageService chatMessageService, String chatId) {
        this.chatId = chatId;
        this.chatMessageService = chatMessageService;
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        log.info("OpenAI建立sse连接...");
        stringBuilder = new StringBuilder();
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
        String endFlag = "[DONE]";
        if (endFlag.equals(data)) {
            System.out.println();
            log.info("OpenAI返回数据结束了");
            SSEServer.sendMessage(chatId, endFlag);
            return;
        }
        ChatResponse response = JSONUtil.toBean(data, ChatResponse.class);
        String as = "";
        if (ObjectUtil.isNotEmpty(response)
                && !CollectionUtil.isEmpty(response.getChoices())
                && ObjectUtil.isNotEmpty(response.getChoices().get(0))
                && ObjectUtil.isNotEmpty(response.getChoices().get(0).getDelta())
                && ObjectUtil.isNotEmpty(response.getChoices().get(0).getDelta().getContent())) {
            as = response.getChoices().get(0).getDelta().getContent();
        }
        System.out.print(as);
        if (ObjectUtil.isNotEmpty(as)) {
            String space = " ";
            if (as.contains(space)) {
                as = as.replace(space, "&nbsp;");
            }
            String enter = "\n";
            if (as.contains(enter)) {
                String enterHtml = "<br />";
                as = as.replace(enter, enterHtml);
            }
            stringBuilder.append(as);
            SSEServer.sendMessage(chatId, as);
        }
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        ChatMessageEntity chatMessage = new ChatMessageEntity();
        chatMessage.setType(2);
        chatMessage.setChatId(Long.parseLong(chatId));
        chatMessage.setContent(stringBuilder.toString());
        chatMessageService.save(chatMessage);
        log.info("OpenAI关闭sse连接...");
    }

    @SneakyThrows
    @Override
    public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
        // 连接失败，删除最后一条提问
        QueryWrapper<ChatMessageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("chat_id", chatId).eq("rm_status", 1));
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.last("limit 1");
        ChatMessageEntity last = chatMessageService.getOne(queryWrapper);
        last.setRmStatus(0);
        chatMessageService.updateById(last);
        if(Objects.isNull(response)){
            log.error("OpenAI  sse连接异常: {}", t.getMessage(), t);
            eventSource.cancel();
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
    }
}