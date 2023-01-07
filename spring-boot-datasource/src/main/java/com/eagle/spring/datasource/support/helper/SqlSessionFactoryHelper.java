package com.eagle.spring.datasource.support.helper;

import com.eagle.spring.core.context.IOCContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * @description: SqlSessionFactory工厂帮助类
 * @Author: csc
 * @create: 2023/01/06
 */
public class SqlSessionFactoryHelper {
    /**
     * 获取SqlSessionFactory 工厂bean对象
     *
     * @return SqlSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionTemplate sqlSessionTemplate = IOCContext.getBean(SqlSessionTemplate.class);
        return sqlSessionTemplate.getSqlSessionFactory();
    }
}
