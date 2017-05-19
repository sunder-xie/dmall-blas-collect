package com.dmall.blas.collect;

import com.dmall.blas.collect.core.AppLifeCycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by lrkin on 2017/5/18.
 */
public class MainEntry {

    private static Logger logger = LoggerFactory.getLogger(MainEntry.class);

    private static final String SPRING_CONFIG = "classpath:spring-config.xml";

    public static void main(String[] agrs) {
        ClassPathXmlApplicationContext cxa = null;
        try {
            cxa = new ClassPathXmlApplicationContext(SPRING_CONFIG);
        } catch (Exception e) {
            logger.error("spring context start error: ", e);
        }

        AppLifeCycleManager.waitStopped();
        if (cxa != null) {
            cxa.destroy();
        }
        logger.info("程序退出");
        System.exit(0);
    }
}
