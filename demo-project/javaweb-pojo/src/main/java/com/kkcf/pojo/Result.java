package com.kkcf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg;  //响应码 描述字符串
    private T data; //返回的数据

    /**
     * 此静态方法用于：创建一个成功响应
     *
     * @return 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<T>(1, "success", null);
    }

    /**
     * 此静态方法用于；创建一个成功响应
     *
     * @param data 响应的数据
     * @return 成功想要
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(1, "success", data);
    }

    /**
     * 此静态方法用于：创建一个失败响应
     *
     * @param msg 失败原因
     * @return 失败响应
     */
    public static <T> Result<T> error(String msg) {
        return new Result<T>(0, msg, null);
    }

    /**
     * 此静态方法用于：创建一个失败响应
     *
     * @param msg 失败原因
     * @return 失败响应
     */
    public static <T> Result<T> error(String msg, T data) {
        return new Result<T>(0, msg, data);
    }
}