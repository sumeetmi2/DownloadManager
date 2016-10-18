/*
 * 
 * Created on Oct 18, 2016
 *
 */
package com.downloadmanager.common;

import java.io.File;
import java.net.URL;

import org.springframework.stereotype.Component;

/**
 * @author SumeetS
 *
 */
@Component
public class Utils {
    
    public String processFilePath(URL downloadUrl,String saveFileLocation){
	File dstFile = new File(saveFileLocation);
	if (!dstFile.exists()) {
	    dstFile.mkdirs();
	}
	saveFileLocation = saveFileLocation.replaceAll("/$","");
	String[] tmpParts = downloadUrl.getPath().split("/");
	saveFileLocation += "/"+ tmpParts[tmpParts.length-1];
	return saveFileLocation;
    }
}
