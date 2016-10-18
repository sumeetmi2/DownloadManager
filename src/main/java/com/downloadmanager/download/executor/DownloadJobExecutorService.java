/*
 * 
 * Created on Oct 18, 2016
 *
 */
package com.downloadmanager.download.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import com.downloadmanager.common.DownloadDTO;
import com.downloadmanager.services.DownloadService;

/**
 * @author SumeetS
 *
 */
@Component
public class DownloadJobExecutorService {
    BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(50);
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    
    public void addDownloadTask(DownloadDTO dto,DownloadService downloadService) throws Exception{
	downloadService.download(dto);
    }
    
    public void execute(Runnable task){
	executorService.execute(task);
    }
    
    
}
