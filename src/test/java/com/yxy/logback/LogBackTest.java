package com.yxy.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.junit.Test;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import static org.slf4j.impl.StaticLoggerBinder.getSingleton;

public class LogBackTest {

    @Test
    public void test(){
        ILoggerFactory factory =  StaticLoggerBinder.getSingleton().getLoggerFactory();
        LoggerContext loggerContext = (LoggerContext) factory;
        Logger loggerSdk = loggerContext.getLogger("sdk-log");

        System.out.println("========>" + factory);
        RollingFileAppender rollingFileAppender = new RollingFileAppender();
        String path = System.getProperty("catalina.base");
        rollingFileAppender.setName("sdk-log");
        rollingFileAppender.setFile("/Users/yangxinyan/logs/sdk.log");
        rollingFileAppender.setContext(loggerContext);


        TimeBasedRollingPolicy timeBasedRollingPolicy = new TimeBasedRollingPolicy();
        timeBasedRollingPolicy.setContext(loggerContext);
        timeBasedRollingPolicy.setFileNamePattern("/Users/yangxinyan/logs/sdk.log.%d{yyyy-MM-dd-HH-mm}");
        timeBasedRollingPolicy.setParent(rollingFileAppender);
        timeBasedRollingPolicy.start();
        rollingFileAppender.setRollingPolicy(timeBasedRollingPolicy);

        PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
        patternLayoutEncoder.setPattern("%msg\n");
        patternLayoutEncoder.setContext(loggerContext);
        patternLayoutEncoder.start();
        rollingFileAppender.setEncoder(patternLayoutEncoder);
        rollingFileAppender.start();
        loggerSdk.addAppender(rollingFileAppender);

        loggerSdk.info("*****sdk*******");



        org.slf4j.Logger loggerapp = LoggerFactory.getLogger("elk_app");
        loggerapp.info("xxxljlisji");

    }
}
