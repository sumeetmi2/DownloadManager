/*
 * 
 * Created on Oct 16, 2016
 *
 */
package com.downloadmanager.controllers;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.downloadmanager.common.Validator;
import com.downloadmanager.services.DownloadService;
import com.downloadmanager.services.DownloadServiceFactory;

/**
 * @author SumeetS
 *
 */
@Controller
@RequestMapping(value="/download")
public class DownloadController extends BaseController{
    
    @Autowired
    DownloadServiceFactory downloadServiceFactory;
    
    @Autowired
    Validator validator;
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> download(@RequestParam(value="url",required=true) String downloadUrl, @RequestParam(value = "save",required=true) String saveLocation) {
	if(validator.validateUrl(downloadUrl)){
	    try {
		URL url = new URL(downloadUrl);
		DownloadService downloadService = downloadServiceFactory.getService(url.getProtocol());
		downloadService.download(url, saveLocation);
	    }catch (MalformedURLException e) {
		return new ResponseEntity<String>("Please check url",HttpStatus.BAD_REQUEST);
	    }catch(Exception e){
		return new ResponseEntity<String>("Unable to download",HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	}else{
	    return new ResponseEntity<String>("Unsupported protocol",HttpStatus.FORBIDDEN);
	}
	
	return new ResponseEntity<String>("Download successful",HttpStatus.OK);
    }
}
