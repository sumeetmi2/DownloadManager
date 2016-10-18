/*
 * 
 * Created on Oct 17, 2016
 *
 */
package com.downloadmanager.services;

import org.springframework.stereotype.Service;

import com.downloadmanager.common.DownloadDTO;

/**
 * @author SumeetS
 *
 */
@Service
public class SftpDownloadService implements DownloadService{

    /* (non-Javadoc)
     * @see com.downloadmanager.services.DownloadService#download(java.net.URL, java.lang.String, com.downloadmanager.objects.AuthObject)
     */
    @Override
    public void download(DownloadDTO dto) {
	// TODO Auto-generated method stub
    }

}
