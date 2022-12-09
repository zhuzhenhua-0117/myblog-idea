package com.smallhua.org.bussiness.seckill.exception;

import com.smallhua.org.bussiness.seckill.enums.ErrorEnum;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author ZZH
 * @create 2022/12/9
 * @since 1.0.0
 */
public class SecKillException extends RuntimeException {

    private String msg;
    private int code;

    public SecKillException(int code, String msg){
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public SecKillException(ErrorEnum errEnum){
        this(errEnum.getCode(), errEnum.getDesc());
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public int getCode(){
        return this.code;
    }
}