package com.smallhua.org.front.service;

import com.smallhua.org.front.dao.UserDao;
import com.smallhua.org.front.dto.RolePermission;
import com.smallhua.org.front.dto.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈用户业务功能模型〉
 *
 * @author ZZH
 * @create 2021/4/25
 * @since 1.0.0
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    public UserRole queryUserInfo(Long userId){
        return userDao.selectUserByUserId(userId);
    }

    public List<RolePermission> queryRolePermission(){
        return userDao.queryRolePermission();
    }

}