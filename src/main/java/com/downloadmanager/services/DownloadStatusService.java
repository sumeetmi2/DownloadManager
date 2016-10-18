/*
 * 
 * Created on Oct 18, 2016
 *
 */
package com.downloadmanager.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.downloadmanager.common.DownloadStatus;

/**
 * @author SumeetS
 *
 */
@Service
public interface DownloadStatusService {
    public Map<String,String> getDownloadStatus(String key);
    public void updateStatus(String key,DownloadStatus status);
}
