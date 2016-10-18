/*
 * 
 * Created on Oct 18, 2016
 *
 */
package com.downloadmanager.objects;

/**
 * @author SumeetS
 *
 */
public final class AuthObject {
    private String userName;
    private String password;
    
    public AuthObject(String userName,String password){
	this.userName =userName;
	this.password = password;
    }
    
    public String getUserName(){
	return userName;
    }
    
    public String getPassword(){
	return password;
    }
    
}
