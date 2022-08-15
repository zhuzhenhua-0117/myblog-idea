package com.smallhua.org.bussiness;

import cn.hutool.core.bean.BeanUtil;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.util.ConstUtil;
import com.smallhua.org.common.util.TreeUtil;
import com.smallhua.org.mapper.TPermissionMapper;
import com.smallhua.org.mapper.TRolePermissionMapper;
import com.smallhua.org.model.TPermission;
import com.smallhua.org.model.TPermissionExample;
import com.smallhua.org.model.TRolePermission;
import com.smallhua.org.model.TRolePermissionExample;
import com.smallhua.org.vo.permissonVo.PermissonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈权限管理〉
 *
 * @author ZZH
 * @create 2021/5/21
 * @since 1.0.0
 */
@Service
@Transactional
public class PermissonService {
    @Autowired
    private TPermissionMapper permissonMapper;
    @Autowired
    private TRolePermissionMapper rolePermissionMapper;

    public CommonResult updOrSavePermisson(TPermission permission) {
        StringBuffer sb = new StringBuffer();
        TPermissionExample permissionExample = new TPermissionExample();

        if (!"0".equals(permission.getPid())){
            permissionExample.createCriteria()
                    .andStatusEqualTo(ConstUtil.ZERO)
                    .andIdEqualTo(permission.getPid());
            List<TPermission> tPermissions = permissonMapper.selectByExample(permissionExample);
            sb.append(tPermissions.get(0).getFullpath());
        }

        sb.append("/").append(permission.getId());
        if (permission.getId() == null) {
            permission.setCreateTime(new Date());
            permission.setFullpath(sb.toString());
            permissonMapper.insertSelective(permission);
            return CommonResult.success(null);
        }else {
            TPermissionExample example = new TPermissionExample();
            example.createCriteria()
                    .andStatusEqualTo(ConstUtil.ZERO)
                    .andIdEqualTo(permission.getId());

            TPermission tPermission = permissonMapper.selectByExample(permissionExample).get(0);
            BeanUtil.copyProperties(permission, tPermission);
            tPermission.setFullpath(sb.toString());
            tPermission.setCreateTime(new Date());
            permissonMapper.updateByPrimaryKeySelective(tPermission);
            return CommonResult.success(null);
        }

    }

    public CommonResult<List<TPermission>> selAllPermission() {
        TPermissionExample example = new TPermissionExample();
        example.setOrderByClause("SORT");
        example.createCriteria().andStatusEqualTo(ConstUtil.ZERO);

        List<TPermission> tPermissions = permissonMapper.selectByExample(example);
        List<TPermission> tree = TreeUtil.createTree(tPermissions);
        return CommonResult.success(tree);
    }

    public CommonResult<List<PermissonVo>> selAllPermissionByRole(Long roleId) {
        TRolePermissionExample example = new TRolePermissionExample();
        example.createCriteria()
                .andRoleIdEqualTo(roleId);
        List<Long> permissionIds = rolePermissionMapper.selectByExample(example)
                .stream()
                .map(TRolePermission::getPermissionId)
                .collect(Collectors.toList());
        List<PermissonVo> permissonVos = (List<PermissonVo>) this.selAllPermission();
        List<PermissonVo> collect = permissonVos.stream().map(item -> {
            if (permissionIds.contains(item.getId())) {
                item.setIfHasResource((byte) 1);
            } else {
                item.setIfHasResource((byte) 0);
            }
            return item;
        }).collect(Collectors.toList());
        return CommonResult.success(collect);
    }

    public CommonResult detailPermission(Long id) {
        TPermission tPermission = permissonMapper.selectByPrimaryKey(id);
        return CommonResult.success(tPermission);
    }
}