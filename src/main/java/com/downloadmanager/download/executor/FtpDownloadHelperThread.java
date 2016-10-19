/*
 * Created on Oct 18, 2016
 */
package com.downloadmanager.download.executor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.downloadmanager.common.CommonConstants;
import com.downloadmanager.common.DownloadStatus;
import com.downloadmanager.objects.AuthObject;
import com.downloadmanager.objects.DownloadDTO;
import com.downloadmanager.services.DownloadStatusService;

/**
 * @author SumeetS
 *
 */
public class FtpDownloadHelperThread implements Runnable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpDownloadHelperThread.class);
    private DownloadDTO dto;
    private DownloadStatusService downloadStatusService;
    /**
     * 
     */
    public FtpDownloadHelperThread(DownloadDTO dto,DownloadStatusService downloadStatusService) {
	this.dto = dto;
	this.downloadStatusService = downloadStatusService;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
	try {
	    FTPClient ftpClient = new FTPClient();
	    URL downloadUrl = dto.getDownloadUrl();
	    String saveLocation = dto.getSaveFileLocation();
	    File f = new File(saveLocation);
	    if (f.exists()) {
		f.delete();
	    }
	    
	    AuthObject authObject = dto.getAuthObject();
	    int port = downloadUrl.getPort();
	    ftpClient.connect(downloadUrl.getHost(), port == -1 ? CommonConstants.FTP_DEFAULT_PORT : port);
	    ftpClient.login(authObject.getUserName(), authObject.getPassword());
	    ftpClient.setBufferSize(CommonConstants.BLOCK_SIZE);
	    ftpClient.enterLocalPassiveMode();
	    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

	    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(saveLocation)));
	    ftpClient.retrieveFile(downloadUrl.getPath(), outputStream);
	    outputStream.close();

	    ftpClient.logout();
	    ftpClient.disconnect();
	    downloadStatusService.updateStatus(dto.getDownloadId(), DownloadStatus.DOWNLOADED);
	} catch (Exception e) {
	    downloadStatusService.updateStatus(dto.getDownloadId(), DownloadStatus.FAILED);
	    LOGGER.error("unable to download ftp file: " + dto.getSaveFileLocation() + " ::"+e.getMessage());
	}

    }

}
