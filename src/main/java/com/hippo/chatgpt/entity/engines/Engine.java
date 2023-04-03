package com.hippo.chatgpt.entity.engines;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author xiewei
 */
@Data
public class Engine implements Serializable {

    private String id;
    private String object;
    private String owner;
    private boolean ready;
    private Object permissions;
    private long created;

}
