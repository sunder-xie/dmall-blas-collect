package com.dmall.blas.collect.service.impl;

import com.dmall.blas.collect.dao.BlasSysInfoMapper;
import com.dmall.blas.collect.service.GroovyService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by lrkin on 2017/5/18.
 */
@Service("groovyService")
public class GroovyServiceImpl implements GroovyService {
    @Resource
    private BlasSysInfoMapper mapper;

    @Override
    public String getDataParseGroovy(String url) {
        //用redis测试guava
//        Jedis jedis = new Jedis("localhost");
//        return jedis.get(url);
        return mapper.selectGroovyByUrl(url);
    }
}
