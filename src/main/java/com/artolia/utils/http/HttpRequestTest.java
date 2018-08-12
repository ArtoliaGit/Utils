package com.artolia.utils.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpRequestTest {

	@Test
	public void testGet() {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
//			URI uri = new URIBuilder()
//					.setScheme("http")
//					.setHost("www.google.com")
//					.setPath("/search")
//					.setParameter("q", "httpclient")
//					.setParameter("btnG", "Google Search")
//					.setParameter("aq", "f")
//					.setParameter("oq", "")
//					.build();
//			HttpGet httpGet = new HttpGet(uri);
			HttpGet httpGet = new HttpGet("http://www.baidu.com/s?wd=httpclient");
			System.out.println(httpGet.getURI());
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000)
					.setConnectionRequestTimeout(5000).setConnectTimeout(5000).build();
			httpGet.setConfig(requestConfig);
			
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				HttpEntity entity = response.getEntity();
				System.out.println("---------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("response content length: " + entity.getContentLength());
					System.out.println("response content: " + EntityUtils.toString(entity));
				}
				System.out.println("---------------------------------------");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
			}
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testPost() {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://localhost:9080/logininfo/login");
		
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair("type", "house"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httpPost.setEntity(uefEntity);
			System.out.println("executing request: " + httpPost.getURI());
			
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("-----------------------------");
					System.out.println("response content: " + EntityUtils.toString(entity));
					System.out.println("-----------------------------");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testUpload() {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost("http://localhost:9080/logininfo/upload");
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setCharset(Charset.forName("UTF-8"));
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			FileBody file = new FileBody(new File("D:/œ¬‘ÿ/Õº∆¨/55474959_p0.jpg"));
			builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, "Õº∆¨1");
			
			ContentType contentType = ContentType.create("text/plain", Consts.UTF_8);
			builder.addTextBody("text", "∫√¿≥ŒÎ", contentType);
			
			HttpEntity reqEntity = builder.build();
			httpPost.setEntity(reqEntity);
			System.out.println("executing request: " + httpPost.getRequestLine());
			
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				System.out.println("-------------------------------------");
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("response content length: " + entity.getContentLength());
				}
				EntityUtils.consume(entity);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
