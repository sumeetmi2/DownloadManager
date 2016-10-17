/*
 * Created on Oct 15, 2016
 */
package com.downloadmanager.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author SumeetS
 *
 */
@Aspect
@Component
public class Logger {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Logger.class);
    private final org.slf4j.Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exceptions");

    public Logger() {
    }

    @Around(value = "execution(* com.downloadmanager..*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
	StopWatch stopWatch = new StopWatch();
	stopWatch.start();

	Object retVal = joinPoint.proceed();

	stopWatch.stop();

	StringBuffer logMessage = new StringBuffer();
	logMessage.append(joinPoint.getTarget().getClass().getName());
	logMessage.append(".");
	logMessage.append(joinPoint.getSignature().getName());
	logMessage.append("(");
	// append args
	Object[] args = joinPoint.getArgs();
	for (int i = 0; i < args.length; i++) {
	    logMessage.append(args[i]).append(",");
	}
	if (args.length > 0) {
	    logMessage.deleteCharAt(logMessage.length() - 1);
	}

	logMessage.append(")");
	logMessage.append(" execution time: ");
	logMessage.append(stopWatch.getTotalTimeMillis());
	logMessage.append(" ms");
	LOGGER.info(logMessage.toString());
	return retVal;
    }

    @AfterThrowing(pointcut = "execution(* com.downloadmanager..*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
	EXCEPTION_LOGGER.error(ex.getMessage(), ex);

    }
}
