/*
 * Created on Oct 18, 2016
 */
package com.downloadmanager.services;

import java.io.File;
import java.io.FileWriter;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.downloadmanager.common.CommonConstants;
import com.downloadmanager.common.DownloadStatus;
import com.downloadmanager.common.UUIDGenerator;
import com.downloadmanager.objects.ChunkWrapper;
import com.downloadmanager.objects.DownloadChunkStatusWrapper;

/**
 * @author SumeetS
 *
 */
@Service
public class DownloadStatusServiceImpl implements DownloadStatusService{

    private ConcurrentHashMap<String, String> downloadStatusMap = new ConcurrentHashMap<String, String>();
    private ConcurrentHashMap<String, DownloadChunkStatusWrapper> downloadChunkStatusMap = new ConcurrentHashMap<String, DownloadChunkStatusWrapper>();
    private static Logger LOGGER = LoggerFactory.getLogger(DownloadStatusServiceImpl.class);
    
    @Resource
    Environment environment;
    
    @Autowired
    UUIDGenerator uUIDGenerator;
    
    /*
     * (non-Javadoc)
     * @see com.downloadmanager.services.DownloadStatusService#getDownloadStatus(java.lang.String)
     */
    @Override
    public String getDownloadStatus(String key) {
	StringBuilder sb = new StringBuilder();
	if (key == null) {
	    for (Entry<String, String> obj : downloadStatusMap.entrySet()) {
		String value = checkAndUpdateIfKeyExistsInChunkMap(obj.getKey(),obj.getValue());
		sb.append(obj.getKey() +"["+uUIDGenerator.getFileNameFromUuid(obj.getKey())+"]"+ CommonConstants.SEPARATOR + value);
		sb.append(System.lineSeparator());
	    }
	    return sb.toString();
	}
	String value = downloadStatusMap.get(key).toString();
	value = checkAndUpdateIfKeyExistsInChunkMap(key, value);
	return sb.append(key +"["+uUIDGenerator.getFileNameFromUuid(key)+"]"+ CommonConstants.SEPARATOR + value).toString();
    }
    
    /**
     * @param key
     * @return updated value if no status change then old value
     */
    private String checkAndUpdateIfKeyExistsInChunkMap(String key,String value){
	if (downloadChunkStatusMap.containsKey(key)) {
	    DownloadChunkStatusWrapper downloadChunkWrapper = downloadChunkStatusMap.get(key);
	    DownloadStatus status = downloadChunkWrapper.getStatus();
	    updateStatus(key, status);
	    value = status.toString();
	}
	if(DownloadStatus.FAILED.toString().equals(value)){
	    File f = new File(uUIDGenerator.getFileNameFromUuid(key));
	    f.delete();
	}
	return value;
    }

    /*
     * (non-Javadoc)
     * @see com.downloadmanager.services.DownloadStatusService#updateStatus(java.lang.String, com.downloadmanager.common.DownloadStatus)
     * to be called when not downloading in small parts and downloading as a whole file
     */
    @Override
    public void updateStatus(String key, DownloadStatus status) {
	downloadStatusMap.put(key, status.toString());
    }

    /*
     * (non-Javadoc)
     * @see com.downloadmanager.services.DownloadStatusService#updateChunkStatus(java.lang.String, com.downloadmanager.common.DownloadStatus)
     * to be called by the chunk downloader..
     * key in form of key-1 key-2
     */
    @Override
    public void updateChunkStatus(ChunkWrapper chunk, DownloadStatus status) {
	String key = chunk.getId();
	String keyMain = key.replaceAll("-[0-9]*$", "");
	downloadChunkStatusMap.putIfAbsent(keyMain, new DownloadChunkStatusWrapper());
	DownloadChunkStatusWrapper downloadChunkWrapper = downloadChunkStatusMap.get(keyMain);
	if (status == DownloadStatus.INPROGRESS) {
	    downloadChunkWrapper.addInProgress(chunk);
	} else if (status == DownloadStatus.FAILED) {
	    downloadChunkWrapper.addFailed(chunk);
	} else {
	    downloadChunkWrapper.addDownloaded(chunk);
	}
	LOGGER.debug("putting chunk :" + key + "    Status: " + status.toString());
    }
    
    public String getAllChunkStatus(){
	StringBuilder sb = new StringBuilder();
	for(Entry<String, DownloadChunkStatusWrapper> obj : downloadChunkStatusMap.entrySet()){
	    sb.append("key :" +obj.getKey());
	    sb.append(System.lineSeparator());
	    DownloadChunkStatusWrapper chunkWrapper = obj.getValue();
	    sb.append(chunkWrapper.getChunkStatusDump());
	    sb.append(System.lineSeparator());
	}
	return sb.toString();
    }
    
    @PreDestroy
    public void backup() throws Exception {
	System.out.println("Taking dump of data");
	File downloadListDump = new File(environment.getRequiredProperty("backup.dump"));
	try{
	    if(!downloadListDump.exists()){
		downloadListDump.createNewFile();
	    }
	    FileWriter fw = new FileWriter(downloadListDump);
	    fw.append(getDownloadStatus(null));
	    fw.append(System.lineSeparator());
	    fw.append("CHUNK DOWNLOAD STATUS::");
	    fw.append(System.lineSeparator());
	    fw.append(getAllChunkStatus());
	    fw.flush();
	    fw.close();
	}catch(Exception e){
	    LOGGER.error("error creating dump of downloaded files " + e.getMessage());
	}
    }

}
