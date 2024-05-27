package org.example.pvingtest.common;

import lombok.Data;

/**
 * @package: org.example.pvingtest.common
 * @className: CommonResponse
 * @author: alexwang
 * @description: TODO
 * @date: 2024/5/27 15:45
 */
@Data
public class CommonResponse<T> {

    private String code;

    private String msg;

    private T data;

    private CommonResponse(ResultEnum resultEnum,T data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.data = data;
    }

    private CommonResponse(String errorMsg) {
        this.code = ResultEnum.BUS_ERROR.getCode();
        this.msg = errorMsg;
    }

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(ResultEnum.SUCCESS,null);
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(ResultEnum.SUCCESS,data);
    }

    public static <T> CommonResponse<T> fail() {
        return new CommonResponse<>(ResultEnum.BUS_ERROR.getMsg());
    }

    public static <T> CommonResponse<T> fail(String errorMsg) {
        return new CommonResponse<>(errorMsg);
    }
}
