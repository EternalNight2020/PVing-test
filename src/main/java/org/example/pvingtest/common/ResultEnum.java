package org.example.pvingtest.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
public enum ResultEnum  {

    SUCCESS("10000", "成功"),

    BUS_ERROR("10001", "业务异常"),

    ;

    @Getter
    private String code;

    @Getter
    private String msg;

}
