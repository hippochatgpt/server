package com.hippo.chatgpt.entity.fineTune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiewei
 */
@Data
public class Event implements Serializable {
    private String object;
    @JsonProperty("created_at")
    private long createdAt;
    private String level;
    private String message;
}
