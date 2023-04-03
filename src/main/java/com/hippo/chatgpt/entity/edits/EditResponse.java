package com.hippo.chatgpt.entity.edits;


import com.hippo.chatgpt.entity.common.Choice;
import com.hippo.chatgpt.entity.common.Usage;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author xiewei
 */
@Data
public class EditResponse implements Serializable {
    private String id;
    private String object;
    private long created;
    private String model;
    private Choice[] choices;
    private Usage usage;
}
