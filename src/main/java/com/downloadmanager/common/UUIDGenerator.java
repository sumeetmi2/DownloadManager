/*
 * 
 * Created on Oct 19, 2016
 *
 */
package com.downloadmanager.common;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * @author SumeetS
 *
 */
@Component
public class UUIDGenerator {
    
    private ConcurrentHashMap<String, String> uuidFileNameMap = new ConcurrentHashMap<>();
    
    UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");   
    
    public String generateId(String fileName){
	String uuid = uid.randomUUID().toString();
	uuidFileNameMap.put(uuid,fileName);
	return uuid;
    }
    
    public String getFileNameFromUuid(String uuid){
	return uuidFileNameMap.get(uuid);
    }
}
