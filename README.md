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

# 快速开始
本项目支持**默认输出**和**流式输出**
## 1、修改配置文件
替换application.yaml中的数据库信息、api-key
## 2、运行项目
RUN ChatGPTApplication