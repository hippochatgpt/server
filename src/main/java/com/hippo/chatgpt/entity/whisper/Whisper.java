package com.hippo.chatgpt.entity.whisper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * 描述：语音转文字
 *
 * @author xiewei
 */
@Data
public class Whisper implements Serializable {


    @Getter
    @AllArgsConstructor
    public enum Model{
        WHISPER_1("whisper-1"),
        ;
        private String name;
    }
}
