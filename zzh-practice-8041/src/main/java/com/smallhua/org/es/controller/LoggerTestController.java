package com.smallhua.org.es.controller;

import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.model.TUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈日志切面测试〉
 *
 * @author ZZH
 * @create 2021/7/28
 * @since 1.0.0
 */
@RestController
@RequestMapping("logger")
@Api(tags = "aop日志操作")
public class LoggerTestController {

    @ApiOperation("正常日志")
    @PostMapping("getNormalLog/{id}")
    public CommonResult getNormalLog(@PathVariable("id") Long id, @RequestBody TUser user){
        return CommonResult.success("获得id为" + id + "的参数");
    }


    @ApiOperation("异常日志")
    @GetMapping("getExceptionLog/{id}")
    public CommonResult getExceptionLog(@PathVariable("id") Long id){
        int i = 1/0;
        return CommonResult.success(null);
    }

}