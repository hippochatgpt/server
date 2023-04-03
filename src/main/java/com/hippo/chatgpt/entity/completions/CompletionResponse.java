package com.hippo.chatgpt.entity.completions;

import com.hippo.chatgpt.entity.common.OpenAiResponse;
import com.hippo.chatgpt.entity.common.Choice;
import com.hippo.chatgpt.entity.common.Usage;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述： 答案类
 *
 * @author xiewei
 */
@Data
public class CompletionResponse extends OpenAiResponse implements Serializable {
    private String id;
    private String object;
    private long created;
    private String model;
    private Choice[] choices;
    private Usage usage;
}
