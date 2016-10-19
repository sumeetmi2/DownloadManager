/*
 * Created on Oct 16, 2016
 */
package com.downloadmanager.controllers;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.downloadmanager.common.DownloadStatus;
import com.downloadmanager.common.UUIDGenerator;
import com.downloadmanager.common.Utils;
import com.downloadmanager.common.Validator;
import com.downloadmanager.download.executor.DownloadJobExecutorService;
import com.downloadmanager.objects.AuthObject;
import com.downloadmanager.objects.DownloadDO;
import com.downloadmanager.objects.DownloadDTO;
import com.downloadmanager.services.DownloadStatusService;

/**
 * @author SumeetS
 *
 */
@Controller
@RequestMapping(value = "/download")
public class DownloadController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);
    
    @Resource
    Environment environment;

    @Autowired
    Validator validator;
    
    @Autowired
    Utils utils;
    
    @Autowired
    DownloadJobExecutorService downloadJobExecutorService;
    
    @Autowired
    DownloadStatusService downloadStatusService;
    
    @Autowired
    UUIDGenerator uUIDGenerator;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> download(@RequestBody DownloadDO downloadDO) {
	long timetaken = 0;
	String downloadUrl = downloadDO.getDownloadUrl();
	String saveLocation = downloadDO.getSaveFileLocation();
	String userName = downloadDO.getUserName();
	String password = downloadDO.getPassword();
	DownloadDTO dto = null;
	if (validator.validateUrl(downloadUrl)) {
	    try {
		long startTime = System.currentTimeMillis();
		URL url = new URL(downloadUrl);
		saveLocation = utils.processFilePath(url, saveLocation);
		AuthObject auth = null;
		if(userName!=null && userName.length()>0 && password!=null && password.length()>0){
		    auth = new AuthObject(userName, password);
		}
		dto = new DownloadDTO(uUIDGenerator.generateId(saveLocation),url,saveLocation,auth);
		downloadJobExecutorService.addDownloadTask(dto);
		timetaken = (System.currentTimeMillis()-startTime)/1000;
		LOGGER.info("Time taken to download file: "+ timetaken + " sec");
	    } catch (MalformedURLException e) {
		return new ResponseEntity<String>("Please check url", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
		downloadStatusService.updateStatus(dto.getDownloadId(), DownloadStatus.FAILED);
		return new ResponseEntity<String>("Unable to download :"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	} else {
	    return new ResponseEntity<String>("Unsupported protocol", HttpStatus.FORBIDDEN);
	}
	String serverUrl = environment.getRequiredProperty("serverUrl")+"download/status?id="+dto.getDownloadId();
	return new ResponseEntity<String>("DownloadId:"+dto.getDownloadId() + "\nFile location: "+saveLocation +"\nCheck Status:" + serverUrl,HttpStatus.OK);
    }
    
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<?> status(@RequestParam(value = "id", required = false) String id){
	return new ResponseEntity<String>(downloadStatusService.getDownloadStatus(id), HttpStatus.OK);
    }
}
