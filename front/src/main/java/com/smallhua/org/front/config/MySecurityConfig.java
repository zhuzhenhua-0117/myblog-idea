package com.smallhua.org.front.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.smallhua.org.front.dao.UserDao;
import com.smallhua.org.front.dto.RolePermission;
import com.smallhua.org.mapper.TUserMapper;
import com.smallhua.org.mapper.TUserRoleMapper;
import com.smallhua.org.model.TUser;
import com.smallhua.org.model.TUserExample;
import com.smallhua.org.model.TUserRoleExample;
import com.smallhua.org.security.component.DynamicSecurityService;
import com.smallhua.org.security.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    private TUserRoleMapper tUserRoleMapper;
    @Autowired
    private UserDao userDao;

    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return new DynamicSecurityService() {
            @Override
            public Map<String, ConfigAttribute> loadDataSource() {
                Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();

                /*配置url是否具有访问权限
                {
                    url: "role_code1,role_code2,role_code3",
                    url1: "role_code11,role_code21,role_code31"
                }
                InterceptorStatusToken token = super.beforeInvocation(fi);
                然后会调用AccessDecisionManager中的decide方法进行鉴权操作
                参数Collection<ConfigAttribute>会经过DynamicSecurityMetadataSource类进行获取
                获取到的就是对应url的值。最后在与userdetail中的GrantedAuthority集合进行比对*/

                List<RolePermission> rolePermissions = userDao.queryRolePermission();
                Map<String, List<RolePermission>> collect = rolePermissions.stream().collect(Collectors.groupingBy(RolePermission::getUrl));

                Set<String> urls = collect.keySet();
                Iterator<String> iterator = urls.iterator();
                while (iterator.hasNext()){
                    String url = iterator.next();
                    List<RolePermission> rolePermission = collect.get(url);
                    Set<String> roleCodes = rolePermission.stream().map(RolePermission::getRoleCode).collect(Collectors.toSet());
                    map.put(url, new org.springframework.security.access.SecurityConfig(Convert.convert(String.class, roleCodes)));
                }
                return map;
            }
        };
    }



    @Bean
    public UserDetailsService userDetailsService() {
        return (account) -> {
            UserDetails userDetails = null;

            //查询数据库
            TUserExample tUserExample = new TUserExample();
            TUserExample.Criteria criteria = tUserExample.createCriteria();
            criteria.andAccoutEqualTo(account);

            List<TUser> tUsers = tUserMapper.selectByExample(tUserExample);
            if (CollectionUtil.isEmpty(tUsers)) {
                throw new UsernameNotFoundException(String.format("无效账号%s", account));
            }
            userDetails = new User(tUsers.get(0).getAccout(), tUsers.get(0).getPassword(), getAuthorities(tUsers.get(0).getId()));
            return userDetails;
        };
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Long id) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        TUserRoleExample tUserRoleExample = new TUserRoleExample();
        tUserRoleExample.createCriteria().andUserIdEqualTo(id);

        return authorities;
    }


}