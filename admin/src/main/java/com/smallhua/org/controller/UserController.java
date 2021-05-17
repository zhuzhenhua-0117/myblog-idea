package com.smallhua.org.controller;

import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.service.UserService;
import com.smallhua.org.vo.RegistVo;
import com.smallhua.org.vo.UpdUserVo;
import com.smallhua.org.vo.UserVo;
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
@RequestMapping("/user")
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

    @ApiOperation("更新用户信息")
    @PostMapping("/updUserInfo")
    public CommonResult updUserInfo(@Validated @RequestBody UpdUserVo updUserVo){
        return userService.updUserInfo(updUserVo);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("delUser/{userId}")
    public CommonResult delUser(@PathVariable("userId") Long userId){
        return userService.delUser(userId);
    }

    @ApiOperation("用户退出系统")
    @PostMapping("logout")
    public CommonResult logout(){
        return userService.logout();
    }
    @ApiOperation("用户退出系统")
    @GetMapping("test")
    public CommonResult test(){
        return CommonResult.success("sadasda");
    }
}