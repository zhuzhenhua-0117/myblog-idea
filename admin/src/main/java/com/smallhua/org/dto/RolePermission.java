package com.smallhua.org.dto;

import com.smallhua.org.model.TPermission;
import com.smallhua.org.model.TRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈角色权限封装对象〉
 *
 * @author ZZH
 * @create 2021/4/29
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission extends TRole {

    List<TPermission> permissionList;

}