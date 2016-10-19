/*
 * 
 * Created on Oct 17, 2016
 *
 */
package com.downloadmanager.common;

import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author SumeetS
 *
 */
@Component
public class Validator {
    
    @Resource
    Environment environment;
    
    /**
     * 
     * :TODO implement proper url validation
     * @param url
     * @return
     */
    public boolean validateUrl(String url){
//	String[] schemes = environment.getRequiredProperty("protocols.supported").split(",");
//	UrlValidator urlValidator = new UrlValidator(schemes);
//	return urlValidator.isValid(url);
	return true;
    }
}
