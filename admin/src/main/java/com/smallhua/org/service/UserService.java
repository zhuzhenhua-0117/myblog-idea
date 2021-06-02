package com.smallhua.org.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.util.*;
import com.smallhua.org.dao.UserDao;
import com.smallhua.org.dto.UserRole;
import com.smallhua.org.mapper.TUserMapper;
import com.smallhua.org.model.TUser;
import com.smallhua.org.model.TUserExample;
import com.smallhua.org.security.util.JwtTokenUtil;
import com.smallhua.org.util.RedisUtil;
import com.smallhua.org.vo.userVo.RegistVo;
import com.smallhua.org.vo.userVo.UpdPwdVo;
import com.smallhua.org.vo.userVo.UpdUserVo;
import com.smallhua.org.vo.userVo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ZZH
 * @create 2021/4/29
 * @since 1.0.0
 */
@Service
@Transactional
@Slf4j
public class UserService {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private TUserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommonService commonService;

    public CommonResult login(UserVo userVo) {
        String account = userVo.getAccount();
        if (StrUtil.isEmpty(account)) {
            return CommonResult.failed("账号不能为空");
        }
        log.info("account={}", userVo.getAccount());
        TUserExample userExample = new TUserExample();
        userExample.createCriteria().andAccountEqualTo(account.trim()).andStatusEqualTo(ConstUtil.ZERO);

        List<TUser> tUsers = userMapper.selectByExample(userExample);
        if (CollectionUtil.isNotEmpty(tUsers)) {
            TUser tUser = tUsers.get(0);
            List<SimpleGrantedAuthority> authors = commonService.getPermissionValues(tUser.getId()).stream().map(item -> new SimpleGrantedAuthority(item)).collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, null, authors);
            if (passwordEncoder.matches(userVo.getPassword(), tUser.getPassword())) {
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                //生成token
                String token = jwtTokenUtil.generateToken(tUser.getUserName(), tUser.getAccount());

                //更新user最新登陆时间
                tUser.setLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(tUser);

                String keyOfUser = RedisUtil.getKeyOfUser(tUser.getUserName(), tUser.getAccount());
                RedisUtil.setUserInfo(keyOfUser, tUser);

                Map<String, Object> map = new HashMap<>();
                map.put("tokenHead", tokenHead);
                map.put("token", token);
                return CommonResult.success(map, "登陆成功");
            }
        } else {
            return CommonResult.failed("账号密码错误");
        }

        return CommonResult.failed("账号密码错误");
    }


    /**
     * 用户注册接口
     *
     * @param registVo
     * @return
     */
    public CommonResult register(RegistVo registVo) {
        TUser tUser = new TUser();
        //copy 请求参数对象到dao层对象中
        HashMap<String, String> mapping = CollUtil.newHashMap();
        //设置别名
        mapping.put("icon", "userProfilePhoto");
        BeanUtil.copyProperties(registVo, tUser, CopyOptions.create().setFieldMapping(mapping));

        //校验用户是否注册
        TUserExample example = new TUserExample();
        example.createCriteria().andAccountEqualTo(tUser.getAccount().trim())
                .andStatusEqualTo(ConstUtil.ZERO);
        ;
        List<TUser> tUsers = userMapper.selectByExample(example);

        if (!CollUtil.isEmpty(tUsers)) {
            return CommonResult.failed("该账号已存在，注册失败");
        }

        tUser.setCreateTime(new Date());
        tUser.setPassword(passwordEncoder.encode(tUser.getPassword()));
        tUser.setId(IdUtil.generateIdBySnowFlake());

        int i = userMapper.insertSelective(tUser);

        if (i > 0) {
            return CommonResult.success("注册成功");
        }

        return CommonResult.failed("注册失败");
    }

    public CommonResult updOrSaveUserInfo(UpdUserVo updUserVo) {
        TUser user;
        if (updUserVo.getId() != null) {
            //编辑用户
            TUserExample userExample = new TUserExample();
            userExample.createCriteria().andIdEqualTo(updUserVo.getId()).andStatusEqualTo(ConstUtil.ZERO);
            List<TUser> tUsers = userMapper.selectByExample(userExample);
            if (CollUtil.isEmpty(tUsers)) {
                return CommonResult.failed("该用户不存在");
            }
            user = tUsers.get(0);

            HashMap<String, String> mapping = CollUtil.newHashMap();
            //设置别名
            mapping.put("icon", "userProfilePhoto");
            BeanUtil.copyProperties(updUserVo, user, CopyOptions.create().setFieldMapping(mapping));

            int i = userMapper.updateByExample(user, userExample);

            if (i > 0) {
//                SessionUtil.removeAttribute(ConstUtil.REDIS_USER);
//                SessionUtil.setAttribute(ConstUtil.REDIS_USER, user);
                RedisUtil.delUserInfo(RedisUtil.getKeyOfUser(jwtTokenUtil.getSubjectByToken()));
                RedisUtil.setUserInfo(RedisUtil.getKeyOfUser(jwtTokenUtil.getSubjectByToken()), user);

                return CommonResult.success("更新成功");
            } else {
                return CommonResult.failed("更新失败");
            }
        } else {
            //保存
            user = new TUser();
            HashMap<String, String> mapping = CollUtil.newHashMap();
            //设置别名
            mapping.put("icon", "userProfilePhoto");
            BeanUtil.copyProperties(updUserVo, user, CopyOptions.create().setFieldMapping(mapping));
            user.setCreateTime(new Date());

            int i = userMapper.insert(user);
            if (i < 1) {
                return CommonResult.failed("保存失败");
            }
            return CommonResult.failed("保存成功");
        }

    }

    public UserRole getUserInfoByUserId(Long userId) {
        log.info("开始获得用户数据");
        UserRole user = (UserRole) ServletUtil.getRequest().getSession().getAttribute("user");
        if (user != null) {
            return user;
        }

        UserRole userRole = userDao.selectUserInfoByUserId(userId);
        log.info("从数据库获得了用户数据");

        if (userRole != null) {
//            SessionUtil.setAttribute("user", userRole);
            RedisUtil.setUserInfo(RedisUtil.getKeyOfUser(jwtTokenUtil.getSubjectByToken()), userRole);
        }

        return userRole;
    }

    public CommonResult delUser(Long userId) {
        TUserExample userExample = new TUserExample();
        userExample.createCriteria().andIdEqualTo(userId);

        List<TUser> tUsers = userMapper.selectByExample(userExample);
        if (CollUtil.isNotEmpty(tUsers)) {
            TUser tUser = tUsers.get(0);
            tUser.setStatus(ConstUtil.ONE);

            int i = userMapper.updateByPrimaryKey(tUser);

            if (i > 0) {
//                SessionUtil.removeAttribute("user");
//                UserRole user = SessionUtil.getAttribute("user", UserRole.class);
                RedisUtil.delUserInfo(RedisUtil.getKeyOfUser(jwtTokenUtil.getSubjectByToken()));
                TUser user = RedisUtil.getUserInfo(RedisUtil.getKeyOfUser(jwtTokenUtil.getSubjectByToken()));

                log.info(user.getUserName() + "删掉了用户{}", tUser.getUserName());
                return CommonResult.success("删除成功");

            }
        }
        return CommonResult.failed("删除失败");
    }

    public CommonResult logout() {
//        SessionUtil.removeAttribute("user");
        RedisUtil.delUserInfo(RedisUtil.getKeyOfUser(jwtTokenUtil.getSubjectByToken()));
        return CommonResult.success("你已退出");
    }

    public CommonResult updPwd(UpdPwdVo updPwdVo) {
        TUserExample example = new TUserExample();
        example.createCriteria()
                .andStatusEqualTo(ConstUtil.ZERO)
                .andAccountEqualTo(updPwdVo.getAccount());

        List<TUser> tUsers = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(tUsers)) {
            return CommonResult.failed("更新失败哦");
        }
        TUser tUser = tUsers.get(0);

        if (!passwordEncoder.matches(updPwdVo.getOldPassword(), tUser.getPassword())) {
            return CommonResult.failed("密码不对哟");
        }
        tUser.setPassword(passwordEncoder.encode(updPwdVo.getNewPassword()));
        int i = userMapper.updateByPrimaryKeySelective(tUser);
        if (i < 1) {
            return CommonResult.failed("更新失败");
        }
        return CommonResult.success("更新成功");
    }

    public CommonResult resetPwd(String account) {
        TUserExample example = new TUserExample();
        example.createCriteria()
                .andStatusEqualTo(ConstUtil.ZERO)
                .andAccountEqualTo(account);
        List<TUser> tUsers = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(tUsers)) {
            return CommonResult.failed("更新失败哦");
        }
        TUser tUser = tUsers.get(0);
        tUser.setPassword(passwordEncoder.encode("123456"));
        userMapper.updateByPrimaryKeySelective(tUser);
        return CommonResult.success("重置成功");
    }


    public CommonPage<TUser> getAllUser(BaseParam baseParam) {
        TUserExample userExample = new TUserExample();
        TUserExample.Criteria criteria = userExample.createCriteria();

        ConditionUtil.createCondition(baseParam, criteria, TUser.class);

        return PageUtil.pagination(baseParam, () -> userMapper.selectByExample(userExample));
    }
}