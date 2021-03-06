package com.yxy.log4j2;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.DeleteAction;
import org.apache.logging.log4j.core.appender.rolling.action.Duration;
import org.apache.logging.log4j.core.appender.rolling.action.IfFileName;
import org.apache.logging.log4j.core.appender.rolling.action.IfLastModified;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Booleans;

/**
 * 动态创建日志
 * log4j2 动态生成logger，每个logger一个输出文件 https://blog.csdn.net/ty497122758/article/details/78907943
 * Log4j2 - 动态生成Appender https://blog.csdn.net/lewky_liu/article/details/85726746
 * log4j2如何动态的创建logger和appender  http://arganzheng.life/log4j2-create-logger-programmatic.html
 * log4j2 不使用配置文件，动态生成logger对象 https://www.cnblogs.com/0201zcr/p/5726072.html
 * Programmatic Configuration http://logging.apache.org/log4j/2.x/manual/customconfig.html
 *
 *
 */
public class Log4j2Util {
	
	/**日志打印的目录*/
	private static final String datalogDir = "log";
	
	private static final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
	private static final Configuration config = ctx.getConfiguration();
	
	private Log4j2Util(){}
	
	/**启动一个动态的logger*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void start(String loggerName) {

		//创建一个展示的样式：PatternLayout，   还有其他的日志打印样式。
		Layout layout = PatternLayout.newBuilder()
			.withConfiguration(config).withPattern("%msg%n").build();

		//单个日志文件大小
		TimeBasedTriggeringPolicy tbtp = TimeBasedTriggeringPolicy.newBuilder().build();
		TriggeringPolicy tp = SizeBasedTriggeringPolicy.createPolicy("10M");
		CompositeTriggeringPolicy policyComposite = CompositeTriggeringPolicy.createPolicy(tbtp, tp);

		String loggerDir = datalogDir + File.separator + loggerName;
		//删除日志的条件
		IfFileName ifFileName = IfFileName.createNameCondition(null, loggerName + "\\.\\d{4}-\\d{2}-\\d{2}.*");
		IfLastModified ifLastModified = IfLastModified.createAgeCondition(Duration.parse("1d"));
		DeleteAction deleteAction = DeleteAction.createDeleteAction(
				loggerDir, false, 1, false, null,
				new PathCondition[]{ifLastModified,ifFileName}, null, config);
		Action[] actions = new Action[]{deleteAction};

		String loggerPathPrefix = loggerDir + File.separator + loggerName;

		RollingFileAppender appender = RollingFileAppender.newBuilder()
        .withAppend(true)
        .setConfiguration(config)
				.withFileName(loggerPathPrefix + ".log")
				.withFilePattern(loggerPathPrefix + ".%d{yyyy-MM-dd}.%i.log")
				.withName(loggerName)
        .withPolicy(policyComposite)
				.withLayout(layout)
				.build();
		appender.start();
		config.addAppender(appender);

		AppenderRef ref = AppenderRef.createAppenderRef(loggerName, null, null);
		AppenderRef[] refs = new AppenderRef[]{ref};
		LoggerConfig loggerConfig = LoggerConfig.createLogger(false,
				Level.ALL, loggerName, "true", refs, null, config, null);
		loggerConfig.addAppender(appender, null, null);
		config.addLogger(loggerName, loggerConfig);
		ctx.updateLoggers();
	}

	/**使用完之后记得调用此方法关闭动态创建的logger，避免内存不够用或者文件打开太多*/
	public static void stop(String loggerName) {
		synchronized (config){
			config.getAppender(loggerName).stop();
			config.getLoggerConfig(loggerName).removeAppender(loggerName);
			config.removeLogger(loggerName);
			ctx.updateLoggers();
		}
	}

	/**获取Logger*/
	public static Logger getLogger(String loggerName) {
		synchronized (config) {
			if (!config.getLoggers().containsKey(loggerName)) {
				start(loggerName);
			}
		}
		return LogManager.getLogger(loggerName);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		for(int i = 0; i < 3; i++){
			String name = "s" + String.valueOf(i);
			Logger logger = getLogger(name);
			logger.info("asdfasdf");
			stop(name);
		}
	}
}
