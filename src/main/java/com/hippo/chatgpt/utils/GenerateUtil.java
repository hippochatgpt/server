package com.hippo.chatgpt.utils;

import com.hippo.chatgpt.sse.SSEServer;

import java.util.Scanner;
import java.util.UUID;

/**
 * @author xiewei
 */
public class GenerateUtil {

    public static String generateUserToken(){
        String userId = UUID.randomUUID().toString().replace("-", "");
        while (SSEServer.isExist(userId)) {
            userId = UUID.randomUUID().toString().replace("-", "");
        }
        return userId;
    }

    public static int countChar(String content) {
        int count = 0;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (isChinese(c)) {
                count += 2;
            } else {
                count += 1;
            }
        }
        return count;
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }
}
