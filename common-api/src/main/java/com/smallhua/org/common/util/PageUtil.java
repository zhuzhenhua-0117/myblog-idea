package com.smallhua.org.common.util;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smallhua.org.common.api.CommonPage;
import com.smallhua.org.common.domain.BaseParam;
import com.smallhua.org.common.function.Selector;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈分页实现〉
 *
 * @author ZZH
 * @create 2021/5/19
 * @since 1.0.0
 */
public class PageUtil {

    public static <T> CommonPage<T> pagination(BaseParam baseParam, Selector<T> selector){
        if (StrUtil.isNotEmpty(baseParam.getOrderBy())) {
            PageHelper.startPage(baseParam.getPage(), baseParam.getPageSize(), baseParam.getOrderBy());
        }else {
            PageHelper.startPage(baseParam.getPage(), baseParam.getPageSize());
        }

        List<T> select = selector.select();
        PageInfo<T> page = new PageInfo<T>(select);
        return CommonPage.newInstance(page);
    }

}