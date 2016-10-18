/*
 * Created on Oct 17, 2016
 */
package com.downloadmanager.services;

import java.io.IOException;
import java.net.SocketException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.downloadmanager.common.DownloadDTO;
import com.downloadmanager.download.executor.DownloadJobExecutorService;
import com.downloadmanager.download.executor.FtpDownloadHelperThread;
import com.downloadmanager.objects.AuthObject;

/**
 * @author SumeetS
 *
 */
@Service
public class FtpDownloadService implements DownloadService {
    
    @Autowired
    Environment environment;
    
    @Autowired
    DownloadJobExecutorService downloadJobExecutorService;
    /*
     * (non-Javadoc)
     * @see com.downloadmanager.services.DownloadService#download(java.net.URL, java.lang.String, com.downloadmanager.objects.AuthObject)
     */
    @Override
    public void download(DownloadDTO dto) throws SocketException, IOException {
	if(dto.getAuthObject() == null){
	    dto = new DownloadDTO(dto.getDownloadUrl(), dto.getSaveFileLocation(), new AuthObject(environment.getRequiredProperty("default.ftp.user"), environment.getRequiredProperty("default.ftp.password")));
	}
	downloadJobExecutorService.execute(new FtpDownloadHelperThread(dto));
    }

}
