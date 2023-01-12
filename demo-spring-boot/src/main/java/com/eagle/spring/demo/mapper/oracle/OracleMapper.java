package com.eagle.spring.demo.mapper.oracle;

import com.eagle.spring.datasource.interceptor.TargetDataSource;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OracleMapper {
    /**
     * 查询接口
     */
    @TargetDataSource("oracle")
    String getOracle();
}
