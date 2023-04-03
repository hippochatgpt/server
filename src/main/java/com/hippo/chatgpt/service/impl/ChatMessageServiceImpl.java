package com.hippo.chatgpt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hippo.chatgpt.entity.database.ChatMessageEntity;
import com.hippo.chatgpt.mapper.ChatMessageMapper;
import com.hippo.chatgpt.service.ChatMessageService;
import org.springframework.stereotype.Service;

/**
 * @author xiewei
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessageEntity> implements ChatMessageService {
}
