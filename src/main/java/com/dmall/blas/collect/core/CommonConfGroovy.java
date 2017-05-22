package com.dmall.blas.collect.core;

import groovy.lang.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lrkin on 2017/5/17.
 */
public class CommonConfGroovy {

    private Logger logger = LoggerFactory.getLogger(CommonConfGroovy.class);

    private static final String IMPORT_COMMON_CONF_GROOVY = "import com.dmall.up.gather.CommonConfGroovy";
    private static CommonConfGroovy instance = new CommonConfGroovy();

    public static CommonConfGroovy Singleton() {
        return CommonConfGroovy.instance;
    }

    private CommonConfGroovy() {
        //
    }

    /**
     * 根据脚本key值获得groovy{@link Script}对象
     *
     * @param commonConfKey 脚本的key值
     * @return
     * @throws Exception
     */
    public Script getScript(String commonConfKey) throws Exception {
//        Script script = null;
//        CommonConf commonConf = GatherToolkit.dataCacher().getCommonConfByKey(commonConfKey);
//        if (commonConf != null) {
//            String scriptStr = commonConf.getConfValue();
//            if (!scriptStr.contains(IMPORT_COMMON_CONF_GROOVY)) {
//                scriptStr = IMPORT_COMMON_CONF_GROOVY + ";\n" + scriptStr;
//            }
//            script = GroovyContext.Singleton().getScript(commonConfKey, scriptStr);
//        }
        return null;
    }

    /**
     * 执行groovy脚本
     *
     * @param commonConfKey 脚本的key值
     * @param methodName    脚本方法名称
     * @param params        脚本方法入参
     * @return
     * @throws Exception
     */
    public Object invoke(String commonConfKey, String methodName, Object params) throws Exception {
        Script script = getScript(commonConfKey);
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
//            logger.error("执行脚本{}方法{}出错: {}", commonConfKey, methodName, e);
        }
        return defaultValue;
    }
}