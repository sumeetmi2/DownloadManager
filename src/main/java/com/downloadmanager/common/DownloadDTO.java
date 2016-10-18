/*
 * 
 * Created on Oct 18, 2016
 *
 */
package com.downloadmanager.common;

import java.net.URL;

import com.downloadmanager.objects.AuthObject;

/**
 * @author SumeetS
 *
 */
public class DownloadDTO {
    private URL downloadUrl;
    private String saveFileLocation;
    private AuthObject authObject;
    
    public DownloadDTO(URL downloadUrl,String saveFileLocation,AuthObject authObject){
	this.downloadUrl = downloadUrl;
	this.saveFileLocation= saveFileLocation;
	this.authObject = authObject;
    }
    /**
     * @return the downloadUrl
     */
    public URL getDownloadUrl() {
        return downloadUrl;
    }
    /**
     * @return the saveFileLocation
     */
    public String getSaveFileLocation() {
        return saveFileLocation;
    }
    /**
     * @return the authObject
     */
    public AuthObject getAuthObject() {
        return authObject;
    }
    
    
}
