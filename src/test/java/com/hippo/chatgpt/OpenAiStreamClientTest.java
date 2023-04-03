package com.hippo.chatgpt;

import com.hippo.chatgpt.client.OpenAiStreamClient;
import com.hippo.chatgpt.entity.billing.CreditGrantsResponse;
import com.hippo.chatgpt.entity.chat.ChatCompletion;
import com.hippo.chatgpt.entity.chat.Message;
import com.hippo.chatgpt.entity.completions.Completion;
import com.hippo.chatgpt.service.ChatMessageService;
import com.hippo.chatgpt.sse.ConsoleEventSourceListener;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 * 2023-02-28
 */
@Slf4j
public class OpenAiStreamClientTest {

    private OpenAiStreamClient client;

    @Autowired
    private ChatMessageService chatMessageService;

    @Before
    public void before() {
        //国内访问需要做代理，国外服务器不需要
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(proxy)
//                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        client = OpenAiStreamClient.builder()
                .apiKey(Arrays.asList("*************", "*************"))
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传
//                .apiHost("https://自己代理的服务器地址/")
                .build();
    }
    @Test
    public void creditGrants() {
        CreditGrantsResponse creditGrantsResponse = client.creditGrants();
        log.info("账户总余额（美元）：{}", creditGrantsResponse.getTotalGranted());
        log.info("账户总使用金额（美元）：{}", creditGrantsResponse.getTotalUsed());
        log.info("账户总剩余金额（美元）：{}", creditGrantsResponse.getTotalAvailable());
    }

    @Test
    public void chatCompletions() {

        String ans = "Sure, I'd be happy to chat with you in English! What would you like to talk about?";

        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener(chatMessageService, "123456789");
        Message message = Message.builder().role(Message.Role.USER).content("我现在需要你扮演一个专业营养师，我现在需要一个月内增重五公斤，你能给我列出我接下来一个月的增重计划么？包括饮食计划，运动计划，我现在的身高体重是178CM,65KG！").build();
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .temperature(0.2)
                .maxTokens(4000)
                .messages(Arrays.asList(message))
                .stream(true)
                .build();
        client.streamChatCompletion(chatCompletion, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void completions() {
        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener(null, "");
        Completion q = Completion.builder()
                .prompt("我想申请转专业，从计算机专业转到会计学专业，帮我完成一份两百字左右的申请书")
                .stream(true)
                .build();
        client.streamCompletions(q, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
