package com.smallhua.org.service;

import cn.hutool.core.bean.BeanUtil;
import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.ConditionUtil;
import com.smallhua.org.common.util.ConstUtil;
import com.smallhua.org.common.util.PageUtil;
import com.smallhua.org.mapper.TRoleMapper;
import com.smallhua.org.model.TRole;
import com.smallhua.org.model.TRoleExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈角色功能〉
 *
 * @author ZZH
 * @create 2021/5/20
 * @since 1.0.0
 */
@Service
@Transactional
public class RoleService {

    @Autowired
    private TRoleMapper roleMapper;

    public CommonResult selRole(Long roleId) {
        TRole tRole = roleMapper.selectByPrimaryKey(roleId);
        return CommonResult.success(tRole);
    }

    public CommonResult updOrSaveRole(TRole role) {
        if (role.getId() == null) {
            role.setCreateTime(new Date());
            int i = roleMapper.insertSelective(role);
            if (i < 1) {
                return CommonResult.failed();
            }
        }else {
            TRoleExample roleExample = new TRoleExample();
            roleExample.createCriteria()
                    .andStatusEqualTo(ConstUtil.ZERO)
                    .andIdEqualTo(role.getId());

            List<TRole> tRoles = roleMapper.selectByExample(roleExample);
            TRole tRole = tRoles.get(0);

            BeanUtil.copyProperties(role, tRole);

            roleMapper.updateByPrimaryKey(tRole);

        }
        return CommonResult.success(null);
    }

    public CommonResult disableRole(Long roleId, Byte sta) {
        TRoleExample roleExample = new TRoleExample();
        roleExample.createCriteria()
                .andStatusEqualTo(ConstUtil.ZERO)
                .andIdEqualTo(roleId);

        List<TRole> tRoles = roleMapper.selectByExample(roleExample);
        TRole tRole = tRoles.get(0);
        tRole.setStatus(sta);
        roleMapper.updateByPrimaryKey(tRole);
        return CommonResult.success(null);
    }

    public CommonPage<TRole> selAllRoles(BaseParam baseParam) {
        TRoleExample example = new TRoleExample();
        TRoleExample.Criteria criteria = example.createCriteria();

        ConditionUtil.createCondition(baseParam, criteria, TRole.class);

        return PageUtil.pagination(baseParam, () -> roleMapper.selectByExample(example));
    }
}