这是一个非官方的社区维护的库。
OpenAi官方文档地址：https://platform.openai.com/docs/api-reference
## 此项目不仅仅支持chat对话模型，支持openai官方所有api，包括
- [x] Billing           余额查询
- [x] Models            模型检索
- [x] Completions       chatgpt对话
- [x] Images            图片模型
- [x] Embeddings        模型自定义训练
- [x] Files             文件上传自定义模型
- [x] Fine-tune         微调
- [x] Moderations       文本审核，敏感词鉴别
- [x] Engines           官方已移除
- [x] Chat              gpt-3.5对话模型
- [x] Speech To Text    语音转文字，语音翻译

### 整合Spring Boot 实现CahtGPT对话模式
此项目支持两种流式输出有完整示例代码可参考 。

流式输出实现方式 | 小程序 | 安卓 | ios | H5 
---|---|---|---|---
SSE | 不支持| 支持| 支持 | 支持
WebSocket | 支持| 支持| 支持| 支持

# 工程简介

**ChatGPT的Java客户端**

OpenAI官方Api的Java SDK

目前支持api-keys的方式调用，获取api-keys可以百度或者csdn查一下。

**api-keys的方式调用目前需要用梯子才可访问。**

OpenAi官方文档地址：https://platform.openai.com/docs/api-reference

# 数据库
## 1、chat_info
CREATE TABLE `chat_info` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`chat_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会话名称',
`rm_status` tinyint DEFAULT '1' COMMENT '删除状态：0正常，1删除',
`user_id` bigint DEFAULT NULL COMMENT '用户ID',
`gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1642792108317065218 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci

## 2、chat_message
CREATE TABLE `chat_message` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID主键',
`chat_id` bigint DEFAULT NULL COMMENT '会话ID',
`content` longtext COLLATE utf8mb4_general_ci COMMENT '消息内容',
`type` tinyint DEFAULT '1' COMMENT '消息类型：1发送，2接收',
`rm_status` tinyint DEFAULT '1' COMMENT '删除状态：1正常，0删除',
`gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1642796339417591810 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci

## 3、chat_user
CREATE TABLE `chat_user` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`user_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名称',
`phone_num` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
`mail_account` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
`password` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码（base64加密）',
`token` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户登录标识',
`gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1642559198464036866 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci

# 快速开始
本项目支持**默认输出**和**流式输出**
## 1、修改配置文件
替换application.yaml中的数据库信息、api-key
## 2、运行项目
RUN ChatGPTApplication
