package com.smallhua.org.front.controller;

import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.front.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/user/{userId}")
    public CommonResult queryUserInfoById(@PathVariable("userId") Long userId){
        return CommonResult.success(userService.queryUserInfo(userId));
    }

}