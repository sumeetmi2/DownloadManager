/*
 * 
 * Created on Oct 19, 2016
 *
 */
package com.downloadmanager.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.downloadmanager.application.DownloadManagerApplication;
import com.downloadmanager.common.CommonConstants;
import com.downloadmanager.common.DownloadStatus;
import com.downloadmanager.objects.DownloadDO;


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
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    
    @Autowired
    WebApplicationContext webApplicationContext;
    
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
    
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
	DownloadDO downloadDO = new DownloadDO();
	downloadDO.setDownloadUrl(url);
	downloadDO.setSaveFileLocation(location);
	MvcResult mvcResult = mockMvc.perform(post("/download/").content(this.json(downloadDO))
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
    
    
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
    
}
