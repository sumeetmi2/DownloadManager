/*
 * Created on Oct 17, 2016
 */
package com.downloadmanager.services;

import com.downloadmanager.objects.DownloadDTO;

/**
 * @author SumeetS
 *
 */
public interface DownloadService {
    public void download(DownloadDTO dto) throws Exception;
}
