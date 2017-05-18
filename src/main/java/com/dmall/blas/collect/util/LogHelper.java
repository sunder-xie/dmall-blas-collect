package com.dmall.blas.collect.util;

/**
 * 日志工具类
 * Created by heaven.zyc on 2015/5/18.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {

    // 用户访问日志
    public static final Logger LOG_AUTO_ACTIVE = LoggerFactory.getLogger("AUTO_ACTIVE");
    public static final Logger LOG_TIMEOUT = LoggerFactory.getLogger("TIMEOUT");

}