package com.hippo.chatgpt.entity.images;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 *
 * @author xiewei
 */
@Data
public class ImageResponse implements Serializable {
    private long created;
    private List<Item> data;
}
