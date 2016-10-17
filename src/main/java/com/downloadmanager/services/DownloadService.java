/*
 * 
 * Created on Oct 17, 2016
 *
 */
package com.downloadmanager.services;

import java.net.URL;

/**
 * @author SumeetS
 *
 */
public interface DownloadService {
    public void download(URL downloadUrl,String saveLocation) throws Exception;
}
