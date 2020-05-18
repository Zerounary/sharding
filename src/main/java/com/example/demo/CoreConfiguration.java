package com.example.demo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.github.abel533.sql.SqlMapper;

@Configuration
public class CoreConfiguration {
    @Autowired
    SqlSession sqlSession;

    @Bean("sqlMapper")
    public SqlMapper sqlMapper(){
        return  new SqlMapper(sqlSession);
    }
    @Bean("resourceLoader")
    public ResourceLoader createResourceLoader() {
        return new DefaultResourceLoader();
    }
}
