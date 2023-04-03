package com.hippo.chatgpt.entity.models;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 *
 * @author xiewei
 */
@Data
public class ModelResponse implements Serializable {
    private String object;
    private List<Model> data;
}
