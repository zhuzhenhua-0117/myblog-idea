package com.smallhua.org.controller;

import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.model.TUser;
import com.smallhua.org.service.UserService;
import com.smallhua.org.vo.userVo.RegistVo;
import com.smallhua.org.vo.userVo.UpdPwdVo;
import com.smallhua.org.vo.userVo.UpdUserVo;
import com.smallhua.org.vo.userVo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈用户接口〉
 *
 * @author ZZH
 * @create 2021/4/25
 * @since 1.0.0
 */
@RestController
@RequestMapping("/userController")
@Api("用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("登录入口")
    @PostMapping("/login")
    public CommonResult login(@Validated @RequestBody UserVo userVo){
        return userService.login(userVo);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public CommonResult register(@Validated @RequestBody RegistVo registVo){
        return userService.register(registVo);
    }

    @ApiOperation("根据账号更新用户信息")
    @PostMapping("/user")
    public CommonResult updOrSaveUserInfo(@Validated @RequestBody UpdUserVo updUserVo){
        return userService.updOrSaveUserInfo(updUserVo);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("user/{userId}")
    public CommonResult delUser(@PathVariable("userId") Long userId){
        return userService.delUser(userId);
    }

    @ApiOperation("用户退出系统")
    @PostMapping("logout")
    public CommonResult logout(){
        return userService.logout();
    }

    @ApiOperation("根据账号更新用户密码")
    @PutMapping("updPwd")
    public CommonResult updPwd(@RequestBody UpdPwdVo updPwdVo){
        return userService.updPwd(updPwdVo);
    }

    @ApiOperation("重置密码")
    @PutMapping("resetPwd/{account}")
    public CommonResult resetPwd(@PathVariable("account") String account){
        return userService.resetPwd(account);
    }

    @ApiOperation("查询用户列表")
    @PostMapping("users")
    public CommonPage<TUser> getAllUser(@RequestBody BaseParam baseParam){
        return userService.getAllUser(baseParam);
    }

}