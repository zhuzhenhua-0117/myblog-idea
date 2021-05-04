package com.smallhua.org.dto;

import com.smallhua.org.model.TUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈封装用户角色信息〉
 *
 * @author ZZH
 * @create 2021/4/25
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends TUser {

    private List<RolePermission> roles;
}