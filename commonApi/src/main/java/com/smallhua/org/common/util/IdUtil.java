package com.smallhua.org.common.util;

import cn.hutool.core.lang.Snowflake;

/**
 * 〈一句话功能简述〉<br>
 * 〈Id生成器〉
 *
 * @author ZZH
 * @create 2021/5/1
 * @since 1.0.0
 */
public class IdUtil {

    public static long generateIdBySnowFlake(){
        Snowflake snowflake = cn.hutool.core.util.IdUtil.getSnowflake(1,1);
        long id = snowflake.nextId();
        return id;
    }
}