package com.smallhua.org.domain.mapper;

import com.smallhua.org.domain.dto.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 〈一句话功能简述〉<br>
 * 〈操作用户数据库模型〉
 *
 * @author ZZH
 * @create 2021/4/25
 * @since 1.0.0 UserMapper
 */
@Mapper
public interface UserMapper {

    UserRole selectUserInfoByUserId(@Param("userId") Long userId);

}