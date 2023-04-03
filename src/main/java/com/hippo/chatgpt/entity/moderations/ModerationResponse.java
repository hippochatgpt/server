package com.hippo.chatgpt.entity.moderations;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 *
 * @author xiewei
 */
@Data
public class ModerationResponse implements Serializable {
    private String id;
    private String model;
    private List<Result> results;
}
