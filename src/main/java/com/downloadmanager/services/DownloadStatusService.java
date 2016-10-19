/*
 * 
 * Created on Oct 18, 2016
 *
 */
package com.downloadmanager.services;

import org.springframework.stereotype.Service;

import com.downloadmanager.common.DownloadStatus;
import com.downloadmanager.objects.ChunkWrapper;

/**
 * @author SumeetS
 *
 */
@Service
public interface DownloadStatusService {
    public String getDownloadStatus(String key);
    public void updateStatus(String key,DownloadStatus status);
    public void updateChunkStatus(ChunkWrapper chunk,DownloadStatus status);
}
