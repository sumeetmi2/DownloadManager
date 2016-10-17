/*
 * 
 * Created on Oct 17, 2016
 *
 */
package com.downloadmanager.services;

import java.io.File;
import java.net.URL;

import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.downloadmanager.common.CommonConstants;
import com.downloadmanager.common.DownloadAccelerator;

/**
 * @author SumeetS
 *
 */
@Service
public class HttpDownloadService implements DownloadService{

    @Resource
    Environment environment;
    
    /* (non-Javadoc)
     * @see com.downloadmanager.services.DownloadService#download(java.lang.String, java.lang.String)
     */
    @Override
    public void download(URL downloadUrl, String saveLocation) throws Exception {
	File dstFile = new File(saveLocation);
	if (!dstFile.exists()) {
	    dstFile.mkdirs();
	}
	saveLocation = saveLocation.replaceAll("/$","");
	String[] tmpParts = downloadUrl.getPath().split("/");
	saveLocation += "/"+ tmpParts[tmpParts.length-1];
	try{
	   DownloadAccelerator accelerator = new DownloadAccelerator(CommonConstants.PARALLEL_DOWNLOADS);
	   long start = System.currentTimeMillis();
	   accelerator.accelerate(downloadUrl, saveLocation);
	   System.out.println("Time taken to download:"+(System.currentTimeMillis()-start));
	}catch(Exception e){
	    throw e;
	}
    }

}
