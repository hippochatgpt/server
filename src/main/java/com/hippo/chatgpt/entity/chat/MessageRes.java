package com.hippo.chatgpt.entity.chat;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author xiewei
 */
@Data
public class MessageRes implements Serializable {
    private String content;
    private String role;
}
