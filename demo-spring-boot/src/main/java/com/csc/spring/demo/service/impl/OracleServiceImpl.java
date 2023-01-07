package com.csc.spring.demo.service.impl;

import com.csc.spring.demo.mapper.oracle.MyOracleMapper;
import com.csc.spring.demo.mapper.oracle.OracleMapper;
import com.csc.spring.demo.service.OracleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OracleServiceImpl implements OracleService {
    @Autowired
    private OracleMapper oracleMapper;
    @Autowired
    private MyOracleMapper myOracleMapper;

    @Override
    public String getOracle() {
        return oracleMapper.getOracle();
    }

    @Override
    public String getMyOracle() {
        return myOracleMapper.getOracle();
    }
}
