package com.dmall.blas.collect.core;

import com.dmall.tool.basic.toolkit.Md5Utils;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by lrkin on 2017/5/17.
 */
public class GroovyContext {
    private static GroovyContext instance = new GroovyContext();
    private Logger logger = LoggerFactory.getLogger(GroovyContext.class);
    private Map<String, String> groovyKeyCache = Maps.newConcurrentMap();
    private Map<String, Script> groovyScriptCache = Maps.newConcurrentMap();

    public static GroovyContext Singleton() {
        return GroovyContext.instance;
    }

    private GroovyContext() {
        //
    }

    public Script getScript(String groovyKey, String groovyCode) {
        if (Strings.isNullOrEmpty(groovyKey) || Strings.isNullOrEmpty(groovyCode)) {
            return null;
        }

        synchronized (GroovyContext.class) {
            try {
                {
                    String groovyExistMd5 = this.groovyKeyCache.get(groovyKey);
                    String groovyNewMd5 = Md5Utils.md5(groovyCode);
                    if (!Strings.isNullOrEmpty(groovyExistMd5)) {
                        if (!groovyExistMd5.equals(groovyNewMd5)) {
                            this.groovyKeyCache.put(groovyKey, groovyNewMd5);
                            this.groovyScriptCache.remove(groovyKey);
                        }
                    } else {
                        this.groovyKeyCache.put(groovyKey, groovyNewMd5);
                    }
                }

                Script groovyScript = this.groovyScriptCache.get(groovyKey);
                if (groovyScript != null) {
                    return groovyScript;
                }

                Binding binding = new Binding();
                groovyScript = new GroovyShell(binding).parse(groovyCode);
                this.groovyScriptCache.put(groovyKey, groovyScript);
                this.logger.warn("load groovy Code: {}, {}", groovyKey, groovyCode);
                return groovyScript;

                // @SuppressWarnings("unchecked")
                // Class<Script> groovyClass = (Class<Script>) new
                // GroovyClassLoader().parseClass(groovyCode);
                // script = groovyClass.newInstance();
                //
                // this.scriptCache.put(cacheKey, script);
            } catch (Throwable t) {
                logger.error("groovy script eval error. script: {}", groovyCode, t);
            }

            return null;
        }
    }
}
