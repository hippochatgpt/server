package com.hippo.chatgpt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hippo.chatgpt.entity.database.ChatMessageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiewei
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessageEntity> {
}
