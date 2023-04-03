package com.hippo.chatgpt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hippo.chatgpt.entity.database.ChatUserEntity;
import com.hippo.chatgpt.mapper.ChatUserMapper;
import com.hippo.chatgpt.service.ChatUserService;
import org.springframework.stereotype.Service;

/**
 * @author xiewei
 */
@Service
public class ChatUserServiceImpl extends ServiceImpl<ChatUserMapper, ChatUserEntity> implements ChatUserService {
}
