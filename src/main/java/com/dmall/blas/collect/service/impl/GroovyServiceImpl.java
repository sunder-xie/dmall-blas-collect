package com.dmall.blas.collect.service.impl;

import com.dmall.blas.collect.service.GroovyService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

/**
 * Created by lrkin on 2017/5/18.
 */
@Service("groovyService")
public class GroovyServiceImpl implements GroovyService {
    @Override
    public String getDataParseGroovy(String url) {
        //测试guava
        Jedis jedis = new Jedis("localhost");
        return jedis.get(url);
    }
}
