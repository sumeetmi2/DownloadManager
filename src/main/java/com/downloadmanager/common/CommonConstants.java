/*
 * 
 * Created on Oct 17, 2016
 *
 */
package com.downloadmanager.common;

/**
 * @author SumeetS
 *
 */
public class CommonConstants {
    public static final String HTTP = "http";
    public static final String FTP = "ftp";
    public static final String SFTP = "sftp";
    
    //need to make this configurable
    public static final int PARALLEL_DOWNLOADS = 10;
    public static final int BLOCK_SIZE = 8192;
    public static final int MIN_DOWNLOAD_SIZE = 50*BLOCK_SIZE;
    
    public static final int FTP_DEFAULT_PORT=21;
}
