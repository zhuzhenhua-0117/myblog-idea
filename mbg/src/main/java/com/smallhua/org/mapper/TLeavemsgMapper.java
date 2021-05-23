package com.smallhua.org.mapper;

import com.smallhua.org.model.TLeavemsg;
import com.smallhua.org.model.TLeavemsgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TLeavemsgMapper {
    long countByExample(TLeavemsgExample example);

    int deleteByExample(TLeavemsgExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TLeavemsg record);

    int insertSelective(TLeavemsg record);

    List<TLeavemsg> selectByExample(TLeavemsgExample example);

    TLeavemsg selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TLeavemsg record, @Param("example") TLeavemsgExample example);

    int updateByExample(@Param("record") TLeavemsg record, @Param("example") TLeavemsgExample example);

    int updateByPrimaryKeySelective(TLeavemsg record);

    int updateByPrimaryKey(TLeavemsg record);
}