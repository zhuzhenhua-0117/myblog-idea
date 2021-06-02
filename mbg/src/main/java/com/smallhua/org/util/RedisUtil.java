package com.smallhua.org.util;

import com.smallhua.org.common.util.ApplicationUtil;
import com.smallhua.org.common.util.ConstUtil;
import com.smallhua.org.model.TUser;
import com.smallhua.org.service.RedisService;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈redis工具类保存用户状态〉
 *
 * @author ZZH
 * @create 2021/6/2
 * @since 1.0.0
 */
public class RedisUtil {

    public static final String LOGIN_USER_PREFIX = "login_user";
    public static final String SPLIT = ":";


    public static String getKeyOfUser(String username, String account){
        StringBuffer sb = new StringBuffer();
        sb.append(LOGIN_USER_PREFIX)
                .append(SPLIT)
                .append(account)
                .append(SPLIT)
                .append(username);
        return sb.toString();
    }

    public static String getKeyOfUser(Map<String, String> subject){
        return getKeyOfUser(subject.get(ConstUtil.PAYLOAD_KEY_USERNAME), subject.get(ConstUtil.PAYLOAD_KEY_ACCOUNT));
    }

    public static void setUserInfo(String key, TUser user){
        RedisService redisService = (RedisService) ApplicationUtil.getBean("redisServiceImpl");
        redisService.set(key, user);
    }

    public static TUser getUserInfo(String key){
        RedisService redisService = (RedisService) ApplicationUtil.getBean("redisServiceImpl");
        return (TUser) redisService.get(key);
    }

    public static boolean delUserInfo(String key){
        RedisService redisService = ApplicationUtil.getBean("redisServiceImpl", RedisService.class);
        return redisService.del(key);
    }

}