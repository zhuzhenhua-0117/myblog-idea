package com.smallhua.org.controller;

import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.model.TLeavemsg;
import com.smallhua.org.service.LeaveMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈留言管理〉
 *
 * @author ZZH
 * @create 2021/6/12
 * @since 1.0.0
 */
@Api(tags = "留言管理")
@RestController
@RequestMapping("leaveMessage")
public class LeaveMessageController {

    @Autowired
    private LeaveMessageService leaveMessageService;

    @ApiOperation("保存编辑留言")
    @PostMapping(value = "leaveMessage")
    public CommonResult saveOrUpdateLeaveMsg(@RequestBody TLeavemsg leaveMsg){
        return leaveMessageService.saveOrUpdateLeaveMsg(leaveMsg);
    }

    @ApiOperation("根据id删除留言")
    @DeleteMapping(value = "leaveMessage/{id}")
    public CommonResult delLeaveMsg(@PathVariable("id") Long id){
        return leaveMessageService.delLeaveMsg(id);
    }

    @ApiOperation("留言列表查看")
    @PostMapping(value = "/public/leaveMessageList")
    public CommonPage leaveMessageList(@RequestBody BaseParam baseParam){
        return leaveMessageService.leaveMessageList(baseParam);
    }

}