package com.smallhua.org.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.util.IdUtil;
import com.smallhua.org.common.util.ServletUtil;
import com.smallhua.org.dao.UserDao;
import com.smallhua.org.dto.UserRole;
import com.smallhua.org.mapper.TUserMapper;
import com.smallhua.org.model.TUser;
import com.smallhua.org.model.TUserExample;
import com.smallhua.org.security.util.JwtTokenUtil;
import com.smallhua.org.util.ConstUtil;
import com.smallhua.org.util.SessionUtil;
import com.smallhua.org.vo.RegistVo;
import com.smallhua.org.vo.UpdUserVo;
import com.smallhua.org.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserCacheService userCacheService;

    public CommonResult login(UserVo userVo) {
        String account = userVo.getAccount();
        if (StrUtil.isEmpty(account)){
            return CommonResult.failed("账号不能为空");
        }
        log.info("account={}", userVo.getAccount());
        TUserExample userExample = new TUserExample();
        userExample.createCriteria().andAccountEqualTo(account.trim()).andStatusEqualTo(ConstUtil.STATUS_NOT_DISABLE);

        List<TUser> tUsers = userMapper.selectByExample(userExample);
        UserDetails userDetails = userDetailsService.loadUserByUsername(account);
        if (CollectionUtil.isNotEmpty(tUsers)){
            TUser tUser = tUsers.get(0);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, null, userDetails.getAuthorities());
            if (passwordEncoder.matches(userVo.getPassword(), userDetails.getPassword())){
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                //生成token
                String token = jwtTokenUtil.generateToken(userDetails);
//                ServletUtil.getResponse().addCookie(new Cookie("token", token));

                //更新user最新登陆时间
                tUser.setLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(tUser);

                //将用户数据放到缓存中
                UserRole userRole = userDao.selectUserInfoByUserId(tUser.getId());
                ServletUtil.getRequest().getSession().setAttribute("user", userRole);

                return CommonResult.success(tokenHead + token ,"登陆成功");
            }
        }else {
            return CommonResult.failed("账号密码错误");
        }

        return CommonResult.failed("账号密码错误");
    }


    /**
     * 用户注册接口
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
                .andStatusEqualTo(ConstUtil.STATUS_NOT_DISABLE);;
        List<TUser> tUsers = userMapper.selectByExample(example);

        if (!CollUtil.isEmpty(tUsers)){
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

    public CommonResult updUserInfo(UpdUserVo updUserVo) {
        TUser user;

        TUserExample userExample = new TUserExample();
        userExample.createCriteria().andAccountEqualTo(updUserVo.getAccount()).andStatusEqualTo(ConstUtil.STATUS_NOT_DISABLE);
        List<TUser> tUsers = userMapper.selectByExample(userExample);
        if (CollUtil.isEmpty(tUsers)){
            return CommonResult.failed("该用户不存在");
        }
        user = tUsers.get(0);

        String account = jwtTokenUtil.getAccountByToken();
        if (!account.equals(updUserVo.getAccount())){
            return CommonResult.failed("请登录"+account+"账号进行操作");
        }

        HashMap<String, String> mapping = CollUtil.newHashMap();
        //设置别名
        mapping.put("icon", "userProfilePhoto");
        BeanUtil.copyProperties(updUserVo, user, CopyOptions.create().setFieldMapping(mapping));

        if (StrUtil.isNotEmpty(updUserVo.getNewPassword())){
            user.setPassword(passwordEncoder.encode(updUserVo.getNewPassword()));
        }

        int i = userMapper.updateByExample(user, userExample);

        if (i > 0){
            SessionUtil.removeAttribute("user");
            return CommonResult.success("更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    public UserRole getUserInfoByUserId(Long userId){
        log.info("开始获得用户数据");
        UserRole user = (UserRole)ServletUtil.getRequest().getSession().getAttribute("user");
        if (user != null){
            return user;
        }

        UserRole userRole = userDao.selectUserInfoByUserId(userId);
        log.info("从数据库获得了用户数据");

        if (userRole != null){
//            userCacheService.setUserInfo(userRole);
            SessionUtil.getAttribute("user", UserRole.class);
        }

        return  userRole;
    }

    public CommonResult delUser(Long userId) {
        TUserExample userExample = new TUserExample();
        userExample.createCriteria().andIdEqualTo(userId);

        List<TUser> tUsers = userMapper.selectByExample(userExample);
        if (CollUtil.isNotEmpty(tUsers)){
            TUser tUser = tUsers.get(0);
            tUser.setStatus(ConstUtil.STATUS_DISABLE);

            int i = userMapper.updateByPrimaryKey(tUser);

            if (i > 0){
                SessionUtil.removeAttribute("user");
                UserRole user = SessionUtil.getAttribute("user", UserRole.class);
                log.info(user.getUserName()+"删掉了用户{}",tUser.getUserName());
                return CommonResult.success("删除成功");

            }
        }
        return CommonResult.failed("删除失败");
    }

    public CommonResult logout() {
        SessionUtil.removeAttribute("user");
        return CommonResult.success("你已退出");
    }
}