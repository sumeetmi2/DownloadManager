/*
 * Created on Oct 17, 2016
 */
package com.downloadmanager.services;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.downloadmanager.common.CommonConstants;
import com.downloadmanager.common.DownloadDTO;
import com.downloadmanager.common.DownloadStatus;
import com.downloadmanager.download.executor.HttpDownloadHelperThread;
import com.downloadmanager.download.executor.DownloadJobExecutorService;

/**
 * @author SumeetS
 *
 */
@Service
public class HttpDownloadService implements DownloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDownloadService.class);

    @Resource
    Environment environment;

    @Autowired
    DownloadJobExecutorService downloadJobExecutorService;
    
    @Autowired
    DownloadStatusService downloadStatusService;

    /*
     * (non-Javadoc)
     * @see com.downloadmanager.services.DownloadService#download(java.net.URL, java.lang.String, com.downloadmanager.objects.AuthObject)
     */
    @Override
    public void download(DownloadDTO dto) throws Exception {
	downloadStatusService.updateStatus(dto.getDownloadId(), DownloadStatus.INPROGRESS);
	try {
	    int fileSize = -1;
	    File f = new File(dto.getSaveFileLocation());
	    if (f.exists()) {
		f.delete();
	    }
	    HttpURLConnection conn = (HttpURLConnection) dto.getDownloadUrl().openConnection();
	    conn.setConnectTimeout(10000);
	    conn.connect();

	    if (conn.getResponseCode() / 100 != 2) {
		throw new IOException("unable to fetch file for download");
	    }
	    int contentLength = conn.getContentLength();
	    if (contentLength < 1) {
		throw new IOException("unable to fetch file for download");
	    }
	    if (fileSize == -1) {
		fileSize = contentLength;
		LOGGER.info("Downloading file of size : " + fileSize);
	    }

	    if (fileSize > CommonConstants.MIN_DOWNLOAD_SIZE) {
		int chunkSize = Math.round(((float) fileSize / CommonConstants.PARALLEL_DOWNLOADS) / CommonConstants.BLOCK_SIZE)
			* CommonConstants.BLOCK_SIZE;
		int startByte = 0;
		int endByte = chunkSize - 1;
		int cnt =1;
		downloadJobExecutorService
			.execute(new HttpDownloadHelperThread(new URL(dto.getDownloadUrl().toString()), dto.getSaveFileLocation(), startByte, endByte,dto.getDownloadId()+"-"+(cnt++),downloadStatusService));
		while (endByte < fileSize) {
		    startByte = endByte + 1;
		    endByte += chunkSize;
		    downloadJobExecutorService.execute(new HttpDownloadHelperThread(new URL(dto.getDownloadUrl().toString()), dto.getSaveFileLocation(), startByte, endByte,dto.getDownloadId()+"-"+(cnt++),downloadStatusService));
		}
	    } else {
		FileUtils.copyURLToFile(dto.getDownloadUrl(), new File(dto.getSaveFileLocation()));
		downloadStatusService.updateStatus(dto.getDownloadId(), DownloadStatus.DOWNLOADED);
	    }
	} catch (Exception e) {
	    downloadStatusService.updateStatus(dto.getDownloadId(), DownloadStatus.FAILED);
	    throw e;
	}
    }

}
