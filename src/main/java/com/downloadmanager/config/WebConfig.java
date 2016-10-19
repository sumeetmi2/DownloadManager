/*
 */
package com.downloadmanager.config;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.downloadmanager.common.Logger;
import com.downloadmanager.common.SecurityFilter;

/**
 * @author SumeetS
 *
 */
@EnableWebMvc
@ComponentScan(basePackages = {"com.downloadmanager"})
@Configuration
@EnableAspectJAutoProxy
public class WebConfig extends WebMvcAutoConfigurationAdapter {

    @Autowired
    ApplicationContext applicationContext;

    @Resource
    Environment environment;

    
    @Bean
    public LocaleResolver localeResolver() {
	SessionLocaleResolver localeResolver = new SessionLocaleResolver();
	localeResolver.setDefaultLocale(Locale.ENGLISH);
	return localeResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
	LocaleChangeInterceptor changeInterceptor = new LocaleChangeInterceptor();
	changeInterceptor.setParamName("lang");
	interceptorRegistry.addInterceptor(changeInterceptor);
    }

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
	InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	resolver.setPrefix("/");
	resolver.setSuffix(".html");
	return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	registry.addResourceHandler("/js/**").addResourceLocations("/app/js/");
	registry.addResourceHandler("/css/**").addResourceLocations("/assets/css/");
	registry.addResourceHandler("/img/**").addResourceLocations("/assets/img/");
	registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/img/");
	registry.addResourceHandler("/api-docs/**").addResourceLocations("/assets/api-docs/");
	registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public Logger logger() {
	return new Logger();
    }

    @Bean
    public FilterRegistrationBean securityAuthenticationFilterRegistration() {
	FilterRegistrationBean retVal = new FilterRegistrationBean();
	Filter filter = securityAuthenticationFilter();
	retVal.setFilter(filter);
	retVal.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
	return retVal;
    }
    
    @Bean
    public FilterRegistrationBean springSecurityFilter() {
	FilterChainProxy o = (FilterChainProxy) applicationContext.getBean(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
	FilterRegistrationBean trVal = new FilterRegistrationBean();
	trVal.setFilter(o);
	trVal.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 2);
	return trVal;
    }
    
    @Bean
    public Filter securityAuthenticationFilter() {
	return new SecurityFilter();
    }
    
}
