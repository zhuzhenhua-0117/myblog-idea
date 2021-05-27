package com.smallhua.org.front.controller;

import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.front.service.LabelService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈标签管理〉
 *
 * @author ZZH
 * @create 2021/5/27
 * @since 1.0.0
 */
@RestController
@RequestMapping("labelController")
@Api("标签管理")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping("labels")
    public CommonResult getAllLabels(){
        return labelService.getAllLabels();
    }

}