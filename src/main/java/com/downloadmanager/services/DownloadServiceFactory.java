/*
 * 
 * Created on Oct 17, 2016
 *
 */
package com.downloadmanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.downloadmanager.common.CommonConstants;

/**
 * @author SumeetS
 *
 */
@Component
public class DownloadServiceFactory {
    
    @Autowired
    DownloadService httpDownloadService;
    @Autowired
    DownloadService ftpDownloadService;
    @Autowired
    DownloadService sftpDownloadService;
    
    public DownloadService getService(String protocol){
	switch(protocol){
	    case CommonConstants.HTTP :  return httpDownloadService;
	    case CommonConstants.FTP  : return ftpDownloadService;
	    case CommonConstants.SFTP : return sftpDownloadService;
	}
	return null;
    }
}
