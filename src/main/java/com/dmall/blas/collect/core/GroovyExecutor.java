package com.dmall.blas.collect.core;

import com.dmall.blas.collect.util.GatherToolkit;
import groovy.lang.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lrkin on 2017/5/18.
 */
public class GroovyExecutor {

    private Logger logger = LoggerFactory.getLogger(GroovyExecutor.class);

    private static final String IMPORT_COMMON_CONF_GROOVY = "import com.dmall.blas.collect.core.GroovyExecutor";
    private static GroovyExecutor instance = new GroovyExecutor();

    public static GroovyExecutor Singleton() {
        return GroovyExecutor.instance;
    }

    private GroovyExecutor() {
        //
    }

    /**
     * 根据脚本key值获得groovy{@link Script}对象
     *
     * @param url 脚本的key值
     * @return
     * @throws Exception
     */
    public static Script getScript(String url) throws Exception {
        Script script = null;
        String scriptStr = GatherToolkit.dataCacher().getGroovy(url);
        if (scriptStr != null) {
//            if (!scriptStr.contains(IMPORT_COMMON_CONF_GROOVY)) {
//                scriptStr = IMPORT_COMMON_CONF_GROOVY + ";\n" + scriptStr;
//            }
            script = GroovyContext.Singleton().getScript(url, scriptStr);
        }
        return script;
    }

    /**
     * 执行groovy脚本
     *
     * @param url        脚本的key值
     * @param methodName 脚本方法名称
     * @param params     脚本方法入参
     * @return
     * @throws Exception
     */
    public static Object invoke(String url, String methodName, Object params) throws Exception {
        Script script = getScript(url);
        if (script == null) {
            return null;
        }

        return script.invokeMethod(methodName, params);
    }

    /**
     * 执行groovy脚本，并指定默认的返回值。
     *
     * @param commonConfKey 脚本的key值
     * @param methodName    脚本方法名称
     * @param params        脚本方法入参
     * @param defaultValue  如果执行为null或者异常时，指定默认值。
     * @return
     */
    public <T> T invoke(String commonConfKey, String methodName, Object params, T defaultValue) {
        try {
            Object result = invoke(commonConfKey, methodName, params);
            return result == null ? defaultValue : (T) result;
        } catch (Exception e) {
        }
        return defaultValue;
    }
}
