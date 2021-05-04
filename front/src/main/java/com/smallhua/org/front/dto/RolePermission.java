package com.smallhua.org.front.dto;

import com.smallhua.org.model.TRolePermission;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈角色权限关联对象模型〉
 *
 * @author ZZH
 * @create 2021/4/27
 * @since 1.0.0
 */
@Data
public class RolePermission extends TRolePermission {

    private String roleName;

    private String roleCode;

    private String url;

}