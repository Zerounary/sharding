package com.example.demo;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OrderDao{

  @Insert("insert into t_order (price, user_id,status) values(#{price}, #{userId}, #{status})")
  int insertOrder(@Param("price") BigDecimal price, @Param("userId") Long userId, @Param("status") String status );
}