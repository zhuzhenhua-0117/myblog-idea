package com.smallhua.org.config;

import cn.hutool.core.collection.CollectionUtil;
import com.smallhua.org.common.util.ConstUtil;
import com.smallhua.org.dao.UserDao;
import com.smallhua.org.mapper.TPermissionMapper;
import com.smallhua.org.mapper.TUserMapper;
import com.smallhua.org.model.TPermission;
import com.smallhua.org.model.TPermissionExample;
import com.smallhua.org.model.TUser;
import com.smallhua.org.model.TUserExample;
import com.smallhua.org.security.component.DynamicSecurityService;
import com.smallhua.org.security.config.SecurityConfig;
import com.smallhua.org.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈spring security 配置〉
 *
 * @author ZZH
 * @create 2021/4/27
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig extends SecurityConfig {

    @Autowired
    private TUserMapper tUserMapper;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TPermissionMapper permissionMapper;
    @Autowired
    private UserDao userDao;

    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return () -> {
            Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();

           /* 配置url是否具有访问权限
            {
                url: "role_code1,role_code2,role_code3",
                        url1: "role_code11,role_code21,role_code31"
            }
            InterceptorStatusToken token = super.beforeInvocation(fi);
            然后会调用AccessDecisionManager中的decide方法进行鉴权操作
            参数Collection<ConfigAttribute>会经过DynamicSecurityMetadataSource类进行获取
            获取到的就是对应url的值。最后在与userdetail中的GrantedAuthority集合进行比对*/

            TPermissionExample example = new TPermissionExample();
            example.createCriteria().andStatusEqualTo(ConstUtil.STATUS_NOT_DISABLE);
            List<TPermission> tPermissions = permissionMapper.selectByExample(example);

            tPermissions.stream().forEach(item -> {
                String url = item.getUrl();
                String authorizeValue = item.getValue();
                map.put(url, new org.springframework.security.access.SecurityConfig(authorizeValue));
            });
            return map;
        };
    }



    @Bean
    public UserDetailsService userDetailsService() {
        return (account) -> {
            UserDetails userDetails = null;

            //查询数据库
            TUserExample tUserExample = new TUserExample();
            TUserExample.Criteria criteria = tUserExample.createCriteria();
            criteria.andAccountEqualTo(account);

            List<TUser> tUsers = tUserMapper.selectByExample(tUserExample);
            if (CollectionUtil.isEmpty(tUsers)) {
                throw new UsernameNotFoundException(String.format("无效账号%s", account));
            }
            userDetails = new User(tUsers.get(0).getAccount(), tUsers.get(0).getPassword(), getAuthorities(tUsers.get(0).getId()));
            return userDetails;
        };
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Long userId) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<String> permissionValue = commonService.getPermissionValues(userId);
        permissionValue.stream().forEach(item -> {
            authorities.add(new SimpleGrantedAuthority(item));
        });

        return authorities;
    }


}