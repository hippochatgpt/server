package com.hippo.chatgpt.entity.whisper;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author xiewei
 */
@Data
public class WhisperResponse implements Serializable {

    private String text;
}
