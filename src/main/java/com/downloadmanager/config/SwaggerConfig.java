/*
 *  
 * Created on Oct 16, 2016
 *
 */
package com.downloadmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author SumeetS
 *
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket downloadApis() {
	return new Docket(DocumentationType.SWAGGER_2).groupName("download-apis").apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
		.paths(downloadApiPaths()).build();
    }

    /**
     * @return
     */
    private Predicate<String> downloadApiPaths() {
	return PathSelectors.regex("/download/.*");
    }
    
    private ApiInfo apiInfo() {
	ApiInfo apiInfo = new ApiInfoBuilder().title("downloadmanager").description("downloadmanager APIs").contact("sumeetmi2@gmail.com")
		.license("downloadmanager API License").licenseUrl("downloadmanager API License URL").termsOfServiceUrl("downloadmanager Terms of Service URL")
		.version("1.0.0").build();
	return apiInfo;
    }
}

