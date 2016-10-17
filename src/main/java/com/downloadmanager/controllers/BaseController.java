/*
 *  
 * Created on Oct 16, 2016
 *
 */
package com.downloadmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.downloadmanager.objects.ResponseWrapperObject;

/**
 * @author SumeetS
 *
 */
public class BaseController {


    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    protected ResponseEntity<ResponseWrapperObject<String>> sendSuccessResponse(ResponseWrapperObject<String> responseObj, HttpStatus httpStatus) {
	return new ResponseEntity<ResponseWrapperObject<String>>(responseObj, httpStatus);
    }

    protected ResponseEntity<ResponseWrapperObject<String>> sendErrorResponse(ResponseWrapperObject<String> responseObj, HttpStatus httpStatus) {
	return new ResponseEntity<ResponseWrapperObject<String>>(responseObj, httpStatus);
    }
    
}
