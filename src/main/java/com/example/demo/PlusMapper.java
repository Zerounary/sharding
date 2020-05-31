package com.example.demo;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlusMapper {

    @Select("select * from t_order ${ew.customSqlSegment}")
    List<Map> getAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}
