/*
 * Created on Oct 16, 2016
 */
package com.downloadmanager.controllers;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.downloadmanager.common.DownloadDTO;
import com.downloadmanager.common.DownloadStatus;
import com.downloadmanager.common.Utils;
import com.downloadmanager.common.Validator;
import com.downloadmanager.download.executor.DownloadJobExecutorService;
import com.downloadmanager.objects.AuthObject;
import com.downloadmanager.services.DownloadService;
import com.downloadmanager.services.DownloadServiceFactory;
import com.downloadmanager.services.DownloadStatusService;

/**
 * @author SumeetS
 *
 */
@Controller
@RequestMapping(value = "/download")
public class DownloadController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);
    
    @Autowired
    DownloadServiceFactory downloadServiceFactory;

    @Autowired
    Validator validator;
    
    @Autowired
    Utils utils;
    
    @Autowired
    DownloadStatusService downloadStatusService;
    
    @Autowired
    DownloadJobExecutorService downloadJobExecutorService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> download(@RequestParam(value = "url", required = true) String downloadUrl,
	    @RequestParam(value = "save", required = true) String saveLocation, @RequestParam(value = "userName", required = false) String userName,
	    @RequestParam(value = "password", required = false) String password) {
	long timetaken = 0;
	if (validator.validateUrl(downloadUrl)) {
	    try {
		long startTime = System.currentTimeMillis();
		URL url = new URL(downloadUrl);
		DownloadService downloadService = downloadServiceFactory.getService(url.getProtocol());
		saveLocation = utils.processFilePath(url, saveLocation);
		downloadStatusService.updateStatus(saveLocation, DownloadStatus.INPROGRESS);
		AuthObject auth = null;
		if(userName!=null && userName.length()>0 && password!=null && password.length()>0){
		    auth = new AuthObject(userName, password);
		}
		DownloadDTO dto = new DownloadDTO(url,saveLocation,auth);
		downloadJobExecutorService.addDownloadTask(dto, downloadService);
		timetaken = (System.currentTimeMillis()-startTime)/1000;
		LOGGER.info("Time taken to download file: "+ timetaken + " sec");
	    } catch (MalformedURLException e) {
		return new ResponseEntity<String>("Please check url", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
		downloadStatusService.updateStatus(saveLocation, DownloadStatus.FAILED);
		return new ResponseEntity<String>("Unable to download :"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	} else {
	    return new ResponseEntity<String>("Unsupported protocol", HttpStatus.FORBIDDEN);
	}
	return new ResponseEntity<String>("Download successful. File location: "+saveLocation +"    TimeTaken: "+timetaken +" sec", HttpStatus.OK);
    }
}
