package com.hippo.chatgpt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hippo.chatgpt.entity.database.ChatUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiewei
 */
@Mapper
public interface ChatUserMapper extends BaseMapper<ChatUserEntity> {
}
