package com.smallhua.org.bussiness.uniqueSn;

import lombok.Getter;

@Getter
public enum BusinessTypeEnum {

    SETTLED("JS");

    private String desc;

    BusinessTypeEnum(String desc){
        this.desc = desc;
    }
}
