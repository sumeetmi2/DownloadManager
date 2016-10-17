/*
 * 
 * Created on Oct 17, 2016
 *
 */
package com.downloadmanager.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

/**
 * @author SumeetS
 *
 */
@Service
public class HttpDownloadService implements DownloadService{

    /* (non-Javadoc)
     * @see com.downloadmanager.services.DownloadService#download(java.lang.String, java.lang.String)
     */
    @Override
    public void download(URL downloadUrl, String saveLocation) throws IOException {
	File dstFile = new File(saveLocation);
	if (!dstFile.exists()) {
	    dstFile.mkdirs();
	}
	saveLocation = saveLocation.replaceAll("/$","");
	String[] tmpParts = downloadUrl.getPath().split("/");
	saveLocation += "/"+ tmpParts[tmpParts.length-1];
	try{
	    FileUtils.copyURLToFile(downloadUrl,new File(saveLocation));
	}catch(IOException e){
	    throw e;
	}
    }

}
