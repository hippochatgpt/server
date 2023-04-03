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
@TableName("chat_message")
public class ChatMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
     * 主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
     * 会话ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long chatId;

	/**
     * 消息内容
	 */
	private String content;

	/**
     * 消息类型：1发送，2接收
	 */
	private Integer type;

	/**
	 * 删除状态：1正常，0删除
	 */
	private Integer rmStatus;

	/**
     * 创建时间
	 */
	private Timestamp gmtCreate;

	/**
     * 修改时间
	 */
	private Timestamp gmtUpdate;

}
