package com.hippo.chatgpt.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author xiewei
 */
@Data
public class ResultUtil implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean status;

    private Integer code;

    private String msg;

    private Object data = new HashMap<String, Object>();

    /**
     * 把构造方法私有
     */
    private ResultUtil() {
    }

    /**
     * 成功静态方法
     * @return
     */
    public static ResultUtil ok() {
        ResultUtil r = new ResultUtil();
        r.setStatus(true);
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(ResultCode.SUCCESS.getMsg());
        return r;
    }

    public static ResultUtil ok(String msg) {
        ResultUtil r = new ResultUtil();
        r.setStatus(true);
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(msg);
        return r;
    }

    public static ResultUtil ok(Object data) {
        ResultUtil r = new ResultUtil();
        r.setStatus(true);
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(ResultCode.SUCCESS.getMsg());
        r.setData(data);
        return r;
    }

    /**
     * 失败静态方法
     * @return
     */
    public static ResultUtil error() {
        ResultUtil r = new ResultUtil();
        r.setStatus(false);
        r.setCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
        r.setMsg("失败");
        return r;
    }


    public static ResultUtil error(String msg) {
        ResultUtil r = new ResultUtil();
        r.setStatus(false);
        r.setCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
        r.setMsg(msg);
        return r;
    }


    public static ResultUtil error(ResultCode resultCode) {
        ResultUtil r = new ResultUtil();
        r.setStatus(false);
        r.setCode(resultCode.getCode());
        r.setMsg(resultCode.getMsg());
        return r;
    }

    public static ResultUtil error(int code, String msg) {
        ResultUtil r = new ResultUtil();
        r.setCode(code);
        r.setMsg(msg);
        r.setStatus(false);
        return r;
    }

    public ResultUtil status(Boolean status){
        this.setStatus(status);
        return this;
    }

    public ResultUtil msg(String msg){
        this.setMsg(msg);
        return this;
    }

    public ResultUtil code(Integer code){
        this.setCode(code);
        return this;
    }

    public ResultUtil data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

    public ResultUtil data(Object data){
        this.setData(data);
        return this;
    }

}
