/*
 * 
 * Created on Oct 19, 2016
 *
 */
package com.downloadmanager.objects;

import java.util.HashSet;
import java.util.Set;

import com.downloadmanager.common.DownloadStatus;

/**
 * @author SumeetS
 *
 */
public class DownloadChunkStatusWrapper {
    private Set<ChunkWrapper> inprogress= new HashSet<ChunkWrapper>();
    private Set<ChunkWrapper> downloaded= new HashSet<ChunkWrapper>();
    private Set<ChunkWrapper> failed= new HashSet<ChunkWrapper>();
    
    public void addInProgress(ChunkWrapper chunk){
	inprogress.add(chunk);
    }
    
    public void addDownloaded(ChunkWrapper chunk){
	inprogress.remove(chunk);
	downloaded.add(chunk);
    }
    
    public void addFailed(ChunkWrapper chunk){
	inprogress.remove(chunk);
	failed.add(chunk);
    }
    
    public DownloadStatus getStatus(){
	if(failed.isEmpty() && inprogress.isEmpty()){
	    return DownloadStatus.DOWNLOADED;
	}else if(!failed.isEmpty() && inprogress.isEmpty()){
	    return DownloadStatus.FAILED;
	}else{
	    return DownloadStatus.INPROGRESS;
	}
    }
    
    public String getChunkStatusDump(){
	StringBuilder sb = new StringBuilder();
	for(ChunkWrapper chunk : inprogress){
	    sb.append(chunk.getId()+"  "+ chunk.getByteRange()+"  INPROCESS");
	    sb.append(System.lineSeparator());
	}
	
	for(ChunkWrapper chunk : downloaded){
	    sb.append(chunk.getId()+"  "+ chunk.getByteRange()+"  DOWNLOADED");
	    sb.append(System.lineSeparator());
	}
	for(ChunkWrapper chunk : failed){
	    sb.append(chunk.getId()+"  "+ chunk.getByteRange()+"  FAILED");
	    sb.append(System.lineSeparator());
	}
	return sb.toString();
    }
}
