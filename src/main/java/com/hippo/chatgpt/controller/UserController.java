package com.hippo.chatgpt.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hippo.chatgpt.entity.database.ChatUserEntity;
import com.hippo.chatgpt.service.ChatUserService;
import com.hippo.chatgpt.utils.GenerateUtil;
import com.hippo.chatgpt.utils.ResultCode;
import com.hippo.chatgpt.utils.ResultUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xiewei
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private ChatUserService chatUserService;

    private static final String PHONE_MACHES = "1[3-9]\\d{9}";

    private static final String MAIL_MACHES = "\\w{1,30}@[a-zA-Z0-9]{2,20}(\\.[a-zA-Z0-9]{2,20}){1,2}";

    private static final String USER_ID = "userId";

    /**
     * 注册
     * @return
     */
    @PostMapping("/register")
    public ResultUtil register(@RequestBody ChatUserEntity user){
        QueryWrapper<ChatUserEntity> queryWrapper = new QueryWrapper<>();
        // 参数校验
        if (ObjectUtil.isEmpty(user.getPhoneNum()) && ObjectUtil.isEmpty(user.getMailAccount())) {
            return ResultUtil.error(ResultCode.PARAM_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            return ResultUtil.error(ResultCode.PARAM_ERROR);
        }
        if (ObjectUtil.isNotEmpty(user.getPhoneNum())) {
            String phoneNum = user.getPhoneNum();
            if (!phoneNum.matches(PHONE_MACHES)) {
                return ResultUtil.error(ResultCode.PARAM_ERROR);
            }
            queryWrapper.eq("phone_num", phoneNum);
            ChatUserEntity check = chatUserService.getOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(check)) {
                return ResultUtil.error(ResultCode.USER_EXIST);
            }
        }
        if (ObjectUtil.isNotEmpty(user.getMailAccount())) {
            String mailAccount = user.getMailAccount();
            if (!mailAccount.matches(MAIL_MACHES)) {
                return ResultUtil.error(ResultCode.PARAM_ERROR);
            }
            queryWrapper.eq("mail_account", mailAccount);
            ChatUserEntity check = chatUserService.getOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(check)) {
                return ResultUtil.error(ResultCode.USER_EXIST);
            }
        }
        // 加密
        user.setPassword(DigestUtils.md2Hex(user.getPassword()));
        // 生成token
        String token = GenerateUtil.generateUserToken();
        user.setToken(token);
        chatUserService.save(user);
        return ResultUtil.ok().data(token);
    }

    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    public ResultUtil login(@RequestBody ChatUserEntity user) {
        // 参数校验
        if (ObjectUtil.isEmpty(user.getPhoneNum()) && ObjectUtil.isEmpty(user.getMailAccount())) {
            return ResultUtil.error(ResultCode.PARAM_ERROR);
        }
        ChatUserEntity check = new ChatUserEntity();
        // 校验账号密码
        QueryWrapper<ChatUserEntity> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(user.getPhoneNum())) {
            String phoneNum = user.getPhoneNum();
            queryWrapper.eq("phone_num", phoneNum);
            check = chatUserService.getOne(queryWrapper);
            if (ObjectUtil.isEmpty(check)) {
                return ResultUtil.error(ResultCode.PASSWORD_ERROR);
            }
        }
        if (ObjectUtil.isNotEmpty(user.getMailAccount())) {
            String mailAccount = user.getMailAccount();
            queryWrapper.eq("mail_account", mailAccount);
            check = chatUserService.getOne(queryWrapper);
            if (ObjectUtil.isEmpty(check)) {
                return ResultUtil.error(ResultCode.PASSWORD_ERROR);
            }
        }
        if (!check.getPassword().equals(DigestUtils.md2Hex(user.getPassword()))) {
            // 密码错误
            return ResultUtil.error(ResultCode.PASSWORD_ERROR);
        }
        // 生成token
        String token = GenerateUtil.generateUserToken();
        check.setToken(token);
        chatUserService.updateById(check);
        return ResultUtil.ok().data(token);
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout")
    public ResultUtil logout(HttpServletRequest request){
        String userId = request.getHeader(USER_ID);
        ChatUserEntity check = chatUserService.getById(userId);
        if (ObjectUtil.isEmpty(check)) {
            return ResultUtil.error(ResultCode.TOKEN_OVERDUE);
        }
        check.setToken(GenerateUtil.generateUserToken());
        chatUserService.updateById(check);
        return ResultUtil.ok();
    }

}
