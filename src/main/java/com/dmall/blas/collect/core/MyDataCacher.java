package com.dmall.blas.collect.core;

import com.dmall.blas.collect.service.GroovyService;
import com.google.common.cache.CacheLoader;
import org.elasticsearch.common.Strings;
import com.dmall.tool.basic.toolkit.GuavaCache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * Created by lrkin on 2017/5/18.
 */
@Component
public class MyDataCacher {
    @Resource
    private GroovyService groovyService;

    private GuavaCache<String, String> groovyCache = GuavaCache.MakeCache(5000, 300, new CacheLoader<String, String>() {
        public String load(String url) {
            return groovyService.getDataParseGroovy(url);
        }
    });

    public String getGroovy(String url) throws ExecutionException {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }

        synchronized (this.groovyCache) {
            return this.groovyCache.get(url);
        }
    }
}
