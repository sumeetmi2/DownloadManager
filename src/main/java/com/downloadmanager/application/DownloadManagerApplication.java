/*
 * 
 * Created on Oct 16, 2016
 *
 */
package com.downloadmanager.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author SumeetS
 *
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.downloadmanager"})
@EnableAspectJAutoProxy
@SpringBootApplication
public class DownloadManagerApplication extends SpringBootServletInitializer   {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.application().setRegisterShutdownHook(true);
	return application.sources(DownloadManagerApplication.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(DownloadManagerApplication.class, args);
    } 
}
