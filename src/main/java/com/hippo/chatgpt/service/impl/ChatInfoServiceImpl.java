package com.hippo.chatgpt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hippo.chatgpt.entity.database.ChatInfoEntity;
import com.hippo.chatgpt.mapper.ChatInfoMapper;
import com.hippo.chatgpt.service.ChatInfoService;
import org.springframework.stereotype.Service;

/**
 * @author xiewei
 */
@Service
public class ChatInfoServiceImpl extends ServiceImpl<ChatInfoMapper, ChatInfoEntity> implements ChatInfoService {

}
