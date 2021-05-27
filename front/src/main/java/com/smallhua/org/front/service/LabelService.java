package com.smallhua.org.front.service;

import cn.hutool.core.collection.CollUtil;
import com.smallhua.org.common.api.CommonResult;
import com.smallhua.org.front.mapper.LabelMapper;
import com.smallhua.org.front.vo.CascadeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈标签功能〉
 *
 * @author ZZH
 * @create 2021/5/27
 * @since 1.0.0
 */
@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelMapper labelMapper;


    public CommonResult getAllLabels() {
        List<CascadeVo> cascadeVos = labelMapper.queryType();

        if (CollUtil.isNotEmpty(cascadeVos)){
            cascadeVos.stream().forEach(item -> {
                item.setChildren(labelMapper.queryLabel(Long.valueOf(item.getValue())));
            });
        }

        return new CommonResult().success(cascadeVos);
    }
}