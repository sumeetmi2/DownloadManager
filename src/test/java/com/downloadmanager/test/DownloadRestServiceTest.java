/*
 * 
 * Created on Oct 19, 2016
 *
 */
package com.downloadmanager.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.downloadmanager.application.DownloadManagerApplication;
import com.downloadmanager.common.CommonConstants;
import com.downloadmanager.common.DownloadStatus;


/**
 * @author SumeetS
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DownloadManagerApplication.class)
@WebAppConfiguration
public class DownloadRestServiceTest{
    
    private MockMvc mockMvc;
    private String location;
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    
    @Autowired
    WebApplicationContext webApplicationContext;
    
    @Before
    public void setUp(){
	mockMvc = webAppContextSetup(webApplicationContext).build();
	location = "D:/tmp1";
    }
    
    @Test
    public void http1MB() throws Exception{
	String url = "http://speedtest.ftp.otenet.gr/files/test1Mb.db";
	DownloadStatus status = executeTest(url);
	Assert.assertEquals(DownloadStatus.DOWNLOADED, status);
    }
    
    @Test
    public void ftp20MB() throws Exception{
	String url = "ftp://speedtest.tele2.net/20MB.zip";
	DownloadStatus status = executeTest(url);
	Assert.assertEquals(DownloadStatus.DOWNLOADED, status);
    }
    
    @Test
    public void http100MB() throws Exception{
	String url = "http://freehd.in/music/english_videos/Take_It_Off_Lil_Jon/Take_It_Off_UHD.mp4";
	DownloadStatus status = executeTest(url);
	Assert.assertEquals(DownloadStatus.DOWNLOADED, status);
    }
    
    private DownloadStatus executeTest(String url) throws Exception{
	MvcResult mvcResult = mockMvc.perform(post("/download/?url="+url+"&save="+location)
		.contentType(contentType)).andReturn();
	String response = mvcResult.getResponse().getContentAsString();
	String[] responseParts= response.split("\n");
	String downloadId = responseParts[0].replaceAll("DownloadId:","").trim();
	String downloadLocation = responseParts[1].replaceAll("File location:","").trim();
	DownloadStatus status;
	while(true){
	    MvcResult mvcResult1= mockMvc.perform(get("/download/status?id="+downloadId).contentType(contentType)).andReturn();
	    String response1 = mvcResult1.getResponse().getContentAsString();
	    
	    DownloadStatus currentStatus = DownloadStatus.valueOf(response1.split(CommonConstants.SEPARATOR)[1]);
	    System.out.println("FILE: "+downloadLocation+"    CURRENT STATUS:" + currentStatus.toString());
	    if(DownloadStatus.INPROGRESS != currentStatus){
		status = currentStatus;
		break;
	    }
	    Thread.sleep(10000);
	}
	return status;
    }
    
    
}
