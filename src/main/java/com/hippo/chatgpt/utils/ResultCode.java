package com.hippo.chatgpt.utils;

/**
 * @author xw
 */
public enum ResultCode {
    SUCCESS(200,"成功"),
    PARAM_ERROR(400, "参数错误"),
    UN_AUTHORIZED(401, "认证失败"),
    TOKEN_OVERDUE(402, "token过期"),
    PERMISSION_INSUFFICIENT(403, "权限不足"),
    TOKEN_MODIFIED(405, "被挤下线"),
    USERNAME_OR_PASSWORD_ERROR(406, "用户名或密码错误"),
    USER_LOCKED(407, "账号已被锁定,请联系管理员"),
    ACCOUNT_EXPIRED(408, "账户已过期"),
    BAD_CREDENTIAL(409, "证书错误"),
    CREDENTIAL_EXPIRED(410, "证书已过期"),
    PASSWORD_ERROR(411, "账号或密码错误"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    USER_EXIST(412, "用户已存在"),
    ;

    private Integer code;

    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ResultCode getByCode(Integer code){
        for (ResultCode value : ResultCode.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
