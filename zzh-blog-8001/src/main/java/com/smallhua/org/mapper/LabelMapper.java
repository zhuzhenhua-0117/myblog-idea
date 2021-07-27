package com.smallhua.org.mapper;


import com.smallhua.org.vo.CascadeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LabelMapper {

    //查询类别与标签级联信息
    List<CascadeVo> queryType();

    List<CascadeVo> queryLabel(@Param("typeId") Long typeId);

    List<CascadeVo> queryAllLabel();
}
