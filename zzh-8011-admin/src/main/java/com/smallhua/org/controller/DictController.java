package com.smallhua.org.controller;

import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.model.TDict;
import com.smallhua.org.bussiness.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈数据字典管理〉
 *
 * @author ZZH
 * @create 2021/5/21
 * @since 1.0.0
 */
@RestController
@RequestMapping("dictController")
@Api("数据字典管理")
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("更新或保存数据字典")
    @PostMapping("updOrSaveDict")
    public CommonResult updOrSaveDict(@RequestBody TDict dict){
        return dictService.updOrSaveDict(dict);
    }

    @ApiOperation("根据分类code查出所有字典")
    @GetMapping("dict/{categoryCode}")
    public CommonResult detailDict(@PathVariable("categoryCode") String categoryCode){
        return dictService.detailDict(categoryCode);
    }

    @ApiOperation("根据分类code和dictcode查出指定字典")
    @GetMapping("dict/{categoryCode}/{dictCode}")
    public CommonResult detailOnlyDict(@PathVariable("categoryCode") String categoryCode,
                                   @PathVariable("dictCode") String dictCode){
        return dictService.detailOnlyDict(categoryCode, dictCode);
    }

    @ApiOperation("查出所有分类（按创建时间排序）")
    @GetMapping("categorys")
    public CommonResult detailCategorys(){
        return dictService.detailCategorys();
    }
}