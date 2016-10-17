/*
 * 
 * Created on Oct 17, 2016
 *
 */
package com.downloadmanager.services;

import org.springframework.stereotype.Component;

import com.downloadmanager.common.CommonConstants;

/**
 * @author SumeetS
 *
 */
@Component
public class DownloadServiceFactory {
    
    public DownloadService getService(String protocol){
	switch(protocol){
	    case CommonConstants.HTTP :  return new HttpDownloadService();
	    case CommonConstants.FTP  : return new FtpDownloadService();
	    case CommonConstants.SFTP : return new SftpDownloadService();
	}
	return null;
    }
}
