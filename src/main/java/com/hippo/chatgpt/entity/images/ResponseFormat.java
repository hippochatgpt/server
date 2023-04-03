package com.hippo.chatgpt.entity.images;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author xiewei
 */
@AllArgsConstructor
@Getter
public enum ResponseFormat implements Serializable {
    URL("url"),
    B64_JSON("b64_json"),
    ;

    private String name;
}
