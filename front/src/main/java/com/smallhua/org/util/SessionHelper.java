package com.smallhua.org.util;

import com.smallhua.org.common.util.ConstUtil;
import com.smallhua.org.common.util.SessionUtil;
import com.smallhua.org.dto.UserRole;

/**
 * 〈一句话功能简述〉<br>
 * 〈获得用户信息〉
 *
 * @author ZZH
 * @create 2021/5/24
 * @since 1.0.0
 */
public class SessionHelper {

    public static UserRole currentUser(){
        UserRole user = SessionUtil.getAttribute(ConstUtil.REDIS_USER, UserRole.class);
        return user;
    }

    public static Long currentUserId(){
        UserRole user = SessionUtil.getAttribute(ConstUtil.REDIS_USER, UserRole.class);
        if (user == null) {
            return null;
        }
        return user.getId();
    }

}