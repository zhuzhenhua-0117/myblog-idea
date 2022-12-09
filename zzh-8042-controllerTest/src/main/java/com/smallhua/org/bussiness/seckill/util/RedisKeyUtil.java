package com.smallhua.org.bussiness.seckill.util;

/**
 * 〈一句话功能简述〉<br>
 * 〈字符串加工〉
 *
 * @author ZZH
 * @create 2022/12/9
 * @since 1.0.0
 */
public class RedisKeyUtil {


    public static String getRedisKey(String key){
        return "stock#product#".concat(key);
    }
}