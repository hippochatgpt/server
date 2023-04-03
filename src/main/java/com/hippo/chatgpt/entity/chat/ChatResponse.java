package com.hippo.chatgpt.entity.chat;

import com.hippo.chatgpt.entity.common.Usage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述： chat答案类
 *
 * @author xiewei
 */
@Data
public class ChatResponse implements Serializable {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoiceRes> choices;
    private Usage usage;
}
