package com.smallhua.org.controller;

import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.model.TRole;
import com.smallhua.org.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈角色管理〉
 *
 * @author ZZH
 * @create 2021/5/20
 * @since 1.0.0
 */
@RestController
@RequestMapping("roleController")
@Api(value = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "角色信息回显")
    @GetMapping("role/{roleId}")
    public CommonResult selRole(@PathVariable("roleId") Long roleId){
        return roleService.selRole(roleId);
    }

    @ApiOperation(value = "更新保存角色")
    @PutMapping("role")
    public CommonResult updOrSaveRole(@RequestBody TRole role){
        return roleService.updOrSaveRole(role);
    }

    @ApiOperation(value = "变更角色状态")
    @PutMapping("disable/{roleId}/{sta}")
    public CommonResult disableRole(@PathVariable("roleId") Long roleId, @PathVariable Byte sta){
        return roleService.disableRole(roleId, sta);
    }

    @ApiOperation(value = "角色列表")
    @PostMapping("roles")
    public CommonPage<TRole> selAllRoles(@RequestBody BaseParam baseParam){
        return roleService.selAllRoles(baseParam);
    }

}