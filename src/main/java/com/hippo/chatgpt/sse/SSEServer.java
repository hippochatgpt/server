package com.hippo.chatgpt.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author xiewei
 */
@Component
@Slf4j
public class SSEServer {

    /**
     * 当前连接数
     */
    private static AtomicInteger count = new AtomicInteger(0);

    private static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public static SseEmitter connect(String userId){
        //设置超时时间，0表示不过期，默认是30秒，超过时间未完成会抛出异常
        SseEmitter sseEmitter = new SseEmitter(0L);
        //注册回调
        sseEmitter.onCompletion(completionCallBack(userId));
        sseEmitter.onError(errorCallBack(userId));
        sseEmitter.onTimeout(timeOutCallBack(userId));
        sseEmitterMap.put(userId,sseEmitter);
        //数量+1
        count.getAndIncrement();
        log.info("create new sse connect ,current user:{}",userId);
        return sseEmitter;
    }

    public static boolean isExist(String userId){
        return sseEmitterMap.containsKey(userId);
    }

    /**
     * 给指定用户发消息
     */
    public static void sendMessage(String userId, String message){
        if(sseEmitterMap.containsKey(userId)){
            try{
                sseEmitterMap.get(userId).send(message);
            }catch (IOException e){
                log.error("user id:{}, send message error:{}",userId,e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 想多人发送消息，组播
     */
    public static void groupSendMessage(String groupId, String message){
        if(sseEmitterMap!=null&&!sseEmitterMap.isEmpty()){
            sseEmitterMap.forEach((k,v) -> {
                try{
                    if(k.startsWith(groupId)){
                        v.send(message, MediaType.APPLICATION_JSON);
                    }
                }catch (IOException e){
                    log.error("user id:{}, send message error:{}",groupId,message);
                    removeUser(k);
                }
            });
        }
    }

    public static void batchSendMessage(String message) {
        sseEmitterMap.forEach((k,v)->{
            try{
                v.send(message,MediaType.APPLICATION_JSON);
            }catch (IOException e){
                log.error("user id:{}, send message error:{}",k,e.getMessage());
                removeUser(k);
            }
        });
    }
    /**
     * 群发消息
     */
    public static void batchSendMessage(String message, Set<String> userIds){
        userIds.forEach(userId->sendMessage(userId,message));
    }
    public static void removeUser(String userId){
        sseEmitterMap.remove(userId);
        //数量-1
        count.getAndDecrement();
        log.info("remove user id:{}",userId);
    }
    public static List<String> getIds(){
        return new ArrayList<>(sseEmitterMap.keySet());
    }
    public static int getUserCount(){
        return count.intValue();
    }
    private static Runnable completionCallBack(String userId) {
        return () -> {
            log.info("结束连接,{}",userId);
            removeUser(userId);
        };
    }
    private static Runnable timeOutCallBack(String userId){
        return ()->{
            log.info("连接超时,{}",userId);
            removeUser(userId);
        };
    }
    private static Consumer<Throwable> errorCallBack(String userId){
        return throwable -> {
            log.error("连接异常，{}",userId);
            removeUser(userId);
        };
    }
}
