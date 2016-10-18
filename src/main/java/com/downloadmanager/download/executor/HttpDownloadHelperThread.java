/*
 * Created on Oct 18, 2016
 */
package com.downloadmanager.download.executor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.downloadmanager.common.CommonConstants;

/**
 * @author SumeetS
 *
 */
public class HttpDownloadHelperThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDownloadHelperThread.class);

    private URL url;
    private String file;
    private int startByte;
    private int endByte;

    /**
     * 
     */
    public HttpDownloadHelperThread(URL url, String file, int startByte, int endByte) {
	this.url = url;
	this.file = file;
	this.startByte = startByte;
	this.endByte = endByte;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
	String byteRange = null;
	BufferedInputStream in = null;
	RandomAccessFile raf = null;
	try {
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	
	    byteRange = startByte + "-" + endByte;
	    conn.setRequestProperty("Range", "bytes=" + byteRange);
	    conn.connect();

	    if (conn.getResponseCode() / 100 != 2) {
		throw new IOException("unable to fetch file for download");
	    }
	    LOGGER.debug(Thread.currentThread().getName() + " started " + byteRange);

	    in = new BufferedInputStream(conn.getInputStream());
	    raf = new RandomAccessFile(file, "rw");
	    raf.seek(startByte);
	    byte data[] = new byte[CommonConstants.BLOCK_SIZE];
	    int read;
	    while ((read = in.read(data, 0, CommonConstants.BLOCK_SIZE)) != -1) {
		raf.write(data, 0, read);
		startByte += read;
	    }
	    LOGGER.debug(Thread.currentThread().getName() + " Completed " + byteRange);
	} catch (Exception e) {
	    LOGGER.error("failed to download segment" + byteRange);
	} finally {
	    try {
		raf.close();
		in.close();
	    } catch (Exception e) {
		LOGGER.error("unable to close file/connection");
	    }
	}
    }

}
