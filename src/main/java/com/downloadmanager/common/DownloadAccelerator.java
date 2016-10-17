/*
 * 
 * Created on Oct 17, 2016
 *
 */
package com.downloadmanager.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

/**
 * @author SumeetS
 *
 */
public class DownloadAccelerator {
    
    private int fileSize = -1;
    private static final int BLOCK_SIZE = 4096;
    private static final int MIN_DOWNLOAD_SIZE = 100*BLOCK_SIZE;
    private int numOfConnections;
    
    public DownloadAccelerator(int numOfConnections){
	this.numOfConnections = numOfConnections;
    }
    
    public void accelerate(URL url,String file) throws IOException, InterruptedException{
	
	File f= new File(file);
	if(f.exists()){
	    f.delete();
	}
	
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setConnectTimeout(10000);
	conn.connect();
	
	if(conn.getResponseCode()/100 != 2){
	    error(); 
	}
	int contentLength = conn.getContentLength();
	if(contentLength < 1){
	    error();
	}
	if(fileSize==-1){
	    fileSize = contentLength;
	    System.out.println("Downloading file of size : "+ fileSize);
	}
	
	if(fileSize > MIN_DOWNLOAD_SIZE){
	    int chunkSize = Math.round(((float)fileSize/numOfConnections)/BLOCK_SIZE)*BLOCK_SIZE;
	    int startByte = 0;
	    int endByte = chunkSize - 1;
	    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	    executor.execute(new DownloadHelperThread(new URL(url.toString()), file, startByte, endByte));
	    while(endByte < fileSize){
		startByte = endByte+1;
		endByte += chunkSize;
		if(endByte > fileSize){
		    break;
		}
		executor.execute(new DownloadHelperThread(new URL(url.toString()), file, startByte, endByte));
	    }
	   
	    executor.shutdown();
	    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	}else{
	    FileUtils.copyURLToFile(url,new File(file));
	}
	
    }
    
    private void error() throws IOException{
	throw new IOException("unable to fetch file for download");
    }
    
    private class DownloadHelperThread implements Runnable{

	private URL url;
	private String file;
	private int startByte;
	private int endByte;
	
	/**
	 * 
	 */
	public DownloadHelperThread(URL url, String file,int startByte, int endByte){
	    this.url = url;
	    this.file= file;
	    this.startByte= startByte;
	    this.endByte = endByte;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
	    String byteRange =null;
	    BufferedInputStream in = null;
	    RandomAccessFile raf = null;
	    try{
		System.out.println(Thread.currentThread().getName()+ " started ");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		byteRange = startByte + "-" + endByte;
		conn.setRequestProperty("Range","bytes="+byteRange);
		conn.connect();
		
		if(conn.getResponseCode()/100 != 2){
		    error();
		}
		
		in = new BufferedInputStream(conn.getInputStream());
		raf = new RandomAccessFile(file, "rw");
		raf.seek(startByte);
		byte data[] = new byte[BLOCK_SIZE];
		int read;
		while((read = in.read(data,0,BLOCK_SIZE)) != -1){
		    raf.write(data,0,read);
		    startByte += read;
		}
		System.out.println(Thread.currentThread().getName()+ " Completed ");
	    }catch(Exception e){
		System.out.println("failed to download segment" + byteRange);
	    }finally{
		try{
		    raf.close();
		    in.close();
		}catch(Exception e){
		    System.out.println("unable to close file/connection");
		}
	    }
	}
	
    }
}
