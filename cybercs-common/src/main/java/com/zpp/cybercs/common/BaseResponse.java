package com.zpp.cybercs.common;


import lombok.Data;

import java.io.Serializable;

/**
 * @author zheng
 * @description 通用返回类
 * @date 2023/11/4 14:23:33
 * @version 1.0
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -1320482340908849298L;

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
