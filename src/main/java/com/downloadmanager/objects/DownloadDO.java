/*
 * 
 * Created on Oct 19, 2016
 *
 */
package com.downloadmanager.objects;

import java.io.Serializable;

/**
 * @author SumeetS
 *
 */
public class DownloadDO implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String downloadUrl;
    private String saveFileLocation;
    private String userName;
    private String password;
    /**
     * @return the downloadUrl
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }
    /**
     * @param downloadUrl the downloadUrl to set
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    /**
     * @return the saveFileLocation
     */
    public String getSaveFileLocation() {
        return saveFileLocation;
    }
    /**
     * @param saveFileLocation the saveFileLocation to set
     */
    public void setSaveFileLocation(String saveFileLocation) {
        this.saveFileLocation = saveFileLocation;
    }
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    

}
