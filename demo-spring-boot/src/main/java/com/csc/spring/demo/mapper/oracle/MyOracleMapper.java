package com.csc.spring.demo.mapper.oracle;

import com.eagle.spring.datasource.interceptor.TargetDataSource;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 子类测试
 * @Author: csc
 * @Create: 2023-01-06
 */
@Mapper
public interface MyOracleMapper extends OracleMapper {
    @TargetDataSource("mysql")
    @Override
    String getOracle();
}
