package com.smallhua.org.util;

import com.smallhua.org.common.util.ApplicationUtil;
import com.smallhua.org.model.TUser;
import com.smallhua.org.security.util.JwtTokenUtil;

/**
 * 〈一句话功能简述〉<br>
 * 〈获得用户信息〉
 *
 * @author ZZH
 * @create 2021/5/24
 * @since 1.0.0
 */
public class SessionHelper {

    public static TUser currentUser(){
        JwtTokenUtil bean = ApplicationUtil.getBean(JwtTokenUtil.class);
        TUser user = RedisUtil.getUserInfo(RedisUtil.getKeyOfUser(bean.getSubjectByToken()));
        return user;
    }

    public static Long currentUserId(){
        TUser user = currentUser();
        if (user == null) {
            return null;
        }
        return user.getId();
    }

}