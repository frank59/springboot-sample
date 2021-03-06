package com.geewaza.code.springboot.aop.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangh
 */

public enum Logs {

    /**
     * 系统日志
     */
    DEFAULT(LoggerFactory.getLogger(Logs.class));

    Logs(Logger logger) {
        this.logger = logger;
    }

    private Logger logger;

    public Logger getLogger() {
        return logger;
    }
}
