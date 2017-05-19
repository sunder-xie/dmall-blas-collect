package com.dmall.blas.collect.dao;

import org.springframework.stereotype.Repository;

/**
 * Created by lrkin on 2017/5/18.
 */
@Repository
public interface BlasSysInfoMapper {
    String selectGroovyByUrl(String url);
}
