package com.hippo.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author xiewei
 */
@Data
public class ChatChoiceRes implements Serializable {
    private long index;
    /**
     * 请求参数stream为true返回是delta
     */
    @JsonProperty("delta")
    private MessageRes delta;
    @JsonProperty("finish_reason")
    private String finishReason;
}
