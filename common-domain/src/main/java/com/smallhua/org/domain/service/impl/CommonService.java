package com.smallhua.org.domain.service.impl;

import com.smallhua.org.domain.dto.RolePermission;
import com.smallhua.org.domain.dto.UserRole;
import com.smallhua.org.domain.mapper.UserMapper;
import com.smallhua.org.domain.service.ZPermissionService;
import com.smallhua.org.model.TPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈两个以上服务调用dao层并作相同操作〉
 *
 * @author ZZH
 * @create 2021/5/17
 * @since 1.0.0
 */
@Service
public class CommonService implements ZPermissionService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 动态授权 认证时将资源权限放到userDetailService中
     * @return
     */
    @Override
    public Set<String> getPermissionValues(Long userId) {
        Set<String> permissionValue = new HashSet<>();
        UserRole userRole = userMapper.selectUserInfoByUserId(userId);
        List<RolePermission> roles = userRole.getRoles();
        roles.stream().forEach(item -> {
            List<TPermission> permissionList = item.getPermissionList();
            permissionValue.addAll(permissionList.stream().map(item1 -> item1.getValue()).collect(Collectors.toList()));
        });
        return permissionValue;
    }
}