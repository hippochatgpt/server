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
@TableName("chat_user")
public class ChatUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 手机号
	 */
	private String phoneNum;

	/**
	 * 邮箱
	 */
	private String mailAccount;

	/**
	 * 密码（base64加密）
	 */
	private String password;

	/**
	 * 用户登录标识
	 */
	private String token;

	/**
	 * 创建时间
	 */
	private Timestamp gmtCreate;

	/**
	 * 修改时间
	 */
	private Timestamp gmtUpdate;

}
