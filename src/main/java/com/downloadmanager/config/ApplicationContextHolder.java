/*
 * Created on Oct 19, 2016
 */
package com.downloadmanager.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author SumeetS
 *
 */

@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
	context = applicationContext;
    }

    public static ApplicationContext getContext() {
	return context;
    }
}
