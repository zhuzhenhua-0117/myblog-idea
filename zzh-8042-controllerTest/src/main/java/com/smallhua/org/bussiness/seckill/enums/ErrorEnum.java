package com.smallhua.org.bussiness.seckill.enums;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    STOCK_NOT_ENOUGH(9001, "商品库存不足"),
    SIGN_INCORRECT(9002, "签名不正确"),
    PARAM_INCORRECT(9003, "参数不正确"),

    ;


    private int code;

    private String desc;


    ErrorEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

}
