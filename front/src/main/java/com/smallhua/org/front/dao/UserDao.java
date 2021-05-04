package com.smallhua.org.front.dao;

import com.smallhua.org.front.dto.RolePermission;
import com.smallhua.org.front.dto.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈操作用户数据库模型〉
 *
 * @author ZZH
 * @create 2021/4/25
 * @since 1.0.0 UserMapper
 */
@Mapper
public interface UserDao {

    UserRole selectUserByUserId(@Param("userId") Long userId);

    List<RolePermission> queryRolePermission();
}