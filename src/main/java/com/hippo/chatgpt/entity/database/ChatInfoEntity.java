package com.hippo.chatgpt.entity.database;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 订单主表
 *
 * @author xiewei
 */
@Data
@TableName("chat_info")
public class ChatInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
     * 主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
     * 会话名称
	 */
	private String chatName;

	/**
     * 删除状态：0正常，1删除
	 */
	private Integer rmStatus;

	/**
     * 用户ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

	/**
     * 创建时间
	 */
	private Timestamp gmtCreate;

	/**
     * 修改时间
	 */
	private Timestamp gmtUpdate;

}
