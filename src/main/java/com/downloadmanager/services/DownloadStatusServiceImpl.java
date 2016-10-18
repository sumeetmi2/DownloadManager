/*
 * 
 * Created on Oct 18, 2016
 *
 */
package com.downloadmanager.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.downloadmanager.common.DownloadStatus;

/**
 * @author SumeetS
 *
 */
@Service
public class DownloadStatusServiceImpl implements DownloadStatusService{
    
    private Map<String,String> downloadStatusMap = new HashMap<String, String>();
    
    /* (non-Javadoc)
     * @see com.downloadmanager.services.DownloadStatusService#getDownloadStatus(java.lang.String)
     */
    @Override
    public Map<String, String> getDownloadStatus(String key) {
	if(key == null){
	    return new HashMap<>(downloadStatusMap);
	}
	Map<String,String> responseMap =new HashMap<String,String>();
	responseMap.put(key, downloadStatusMap.get(key));
	return responseMap;
    }
    
    @Override
    public void updateStatus(String key,DownloadStatus status){
	downloadStatusMap.put(key, status.toString());
    }

}
