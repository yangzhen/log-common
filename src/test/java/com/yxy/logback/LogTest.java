package com.yxy.logback;

import ch.qos.logback.classic.Logger;
/**
 * @author Wzy525110
 */
public class LogTest {
 
    public static void main(String[] args) {
        LoggerBuilder loggerBuilder =new LoggerBuilder();
        Logger logger = loggerBuilder.getLogger("test");
        logger.debug("shuai1 +++++++++++++++++++++++++++++++++++++debug");
        logger.warn("shuai2 +++++++++++++++++++++++++++++++++++++warn");
        logger.info("shuai3 +++++++++++++++++++++++++++++++++++++info");
        logger.error("shuai4 +++++++++++++++++++++++++++++++++++++error");
    }
}