package com.xnx3.okex.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.xnx3.DateUtil;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.net.HttpsUtil.TrustAnyHostnameVerifier;
import com.xnx3.net.HttpsUtil.TrustAnyTrustManager;
import com.xnx3.okex.Global;

import net.sf.json.JSONObject;

public class HttpsUtil{
	public static HttpUtil http;
	public static com.xnx3.net.HttpsUtil https;
	static{
		http = new HttpUtil();
	}
	
	/**
	 * 判断当前是否使用的是https的，是则是返回true
	 * @param url
	 * @return
	 */
	public static boolean isHttps(String url){
		if(url.indexOf("https://") == 0){
			return true;
		}
		
		return false;
	} 
	
	/**
	 * 请求，获取接口数据，返回JSON格式
	 * @param url 传入如 /api/v5/market/ticker?instId=BTC_USDT
	 * @return
	 */
	public static JSONObject get(String url){
		HttpResponse hr;
		if(isHttps(url)){
			hr = https.get(Global.OKEX_DOMAIN+url);
		}else{
			hr = http.get(Global.OKEX_DOMAIN+url);
		}
		
		JSONObject json = JSONObject.fromObject(hr.getContent());
		return json;
	}
	
	/**
	 * 登录请求
	 * @param url 传入如 "/api/v5/account/balance"
	 * @param queryString get参数，传入如 a=1&b=2
	 */
	public static JSONObject getLoginRequest(String url, String queryString){
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
		
		long currentTime = DateUtil.timeForUnix13();//当前北京时间
		//减去8小时的时间，取伦敦时间
		long time = currentTime - 60*60*8*1000;
//		long time = currentTime;
		
//		String timeS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'").format(new Date())
		Date date = new Date(time);
		String timeS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'").format(date);
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("OK-ACCESS-KEY", Global.OK_ACCESS_KEY);
		headers.put("OK-ACCESS-TIMESTAMP", timeS);
		headers.put("OK-ACCESS-PASSPHRASE", Global.OK_ACCESS_PASSPHRASE);
		
		String sign = null;
		try {
			sign = HmacSHA256Base64Utils.sign(timeS, "GET", url, queryString, "", Global.OK_ACCESS_SECRET_KEY);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		headers.put("OK-ACCESS-SIGN", sign);
		
		if(queryString != null && queryString.length() > 0){
			queryString = "?"+queryString;
		}else{
			queryString = "";
		}
		
		HttpResponse hr;
		if(isHttps(url)){
			hr = https.get(Global.OKEX_DOMAIN+url+queryString, headers);
		}else{
			hr = http.get(Global.OKEX_DOMAIN+url+queryString, null, headers);
		}
		
		if(hr.getCode() != 200){
			Log.log(hr.toString());
		}
		JSONObject json = JSONObject.fromObject(hr.getContent());
		if(!json.getString("code").equals("0")){
			Log.log("接口响应失败： \t"+json.toString());
		}
		return json;
	}
	

	/**
	 * 登录请求
	 * @param url 传入如 "/api/v5/account/balance"
	 * @param body get参数，传入如 a=1&b=2
	 */
	public static JSONObject postLoginRequest(String url, String body){
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
		
		long currentTime = DateUtil.timeForUnix13();//当前北京时间
		//减去8小时的时间，取伦敦时间
		long time = currentTime - 60*60*8*1000;
//		long time = currentTime;
		
//		String timeS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'").format(new Date())
		Date date = new Date(time);
		String timeS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'").format(date);
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("OK-ACCESS-KEY", Global.OK_ACCESS_KEY);
		headers.put("OK-ACCESS-TIMESTAMP", timeS);
		headers.put("OK-ACCESS-PASSPHRASE", Global.OK_ACCESS_PASSPHRASE);
//		headers.put("Content_Type", "application/json");
		
		String sign = null;
		try {
			sign = HmacSHA256Base64Utils.sign(timeS, "POST", url, null, body, Global.OK_ACCESS_SECRET_KEY);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		headers.put("OK-ACCESS-SIGN", sign);
		
		HttpResponse hr = null;
		if(isHttps(Global.OKEX_DOMAIN)){
			try {
				hr = postPayload(Global.OKEX_DOMAIN+url, body, headers);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			hr = https.post(,null, headers);
		}else{
			System.out.println("必须https");
//			hr = http.post(Global.OKEX_DOMAIN+url+queryString, null, headers);
		}
		
		if(hr.getCode() != 200){
			Log.log(hr.toString());
		}
		JSONObject json = JSONObject.fromObject(hr.getContent());
		if(!json.getString("code").equals("0")){
			Log.log("接口响应失败： \t"+json.toString());
		}
		return json;
	}
	
	

    /**
     * post发送payload数据
     * @param url 请求的url
     * @param postData POST要提交的数据。可为null，为不提交数据。
     * @param headers header头
     * @return {@link HttpResponse}
     * @throws Exception 异常
     */
    public static HttpResponse postPayload(String url,String postData,Map<String, String> headers) throws Exception {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        URL console = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setRequestProperty("Content-Type", "application/json");
        
//        if(post != null && post.length()>0){
//        	headers.put("Content-Length", post.length()+"");
//        }
        
        if(headers != null){
        	for (Map.Entry<String, String> entry : headers.entrySet()) {  
            	conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        
		conn.setRequestMethod("POST");
		// 设置通用请求类型
		conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		conn.setRequestProperty("Charset", "UTF-8");
        // 设置链接状态
		conn.setRequestProperty("connection", "keep-alive");
		// post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
		conn.setDoOutput(true);
		// 设置是否从httpUrlConnection读入，默认情况下是true;
		conn.setDoInput(true);
		// Post 请求不能使用缓存
		conn.setUseCaches(false);
        // 设置本次连接是否自动处理重定向
		conn.setInstanceFollowRedirects(true);
		
		 // 基本类型和字符串使用DataOutputStream
		DataOutputStream dataOutputStreamSend = new DataOutputStream(conn.getOutputStream());
		dataOutputStreamSend.write(postData.getBytes());
		 dataOutputStreamSend.flush();
		
        conn.connect();
		
		
//		 OutputStream os = conn.getOutputStream(); 
//		 PrintWriter pw = new PrintWriter(new OutputStreamWriter(os)); 
//		 pw.write(postData); 
//		 pw.close(); 
        
//		 
//         OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//         osw.write(post);
//         osw.flush();
//         osw.close();
//         System.out.println(conn.getResponseCode());
		
		
//		if(post != null && post.length()>0){
//			PrintWriter writer = new PrintWriter(conn.getOutputStream());
//			writer.print(post);
//			writer.flush();
//			writer.close();
//		}

		String line;
		BufferedReader bufferedReader;
		StringBuilder sb = new StringBuilder();
		InputStreamReader streamReader = null;
		
		try {
			streamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
		} catch (IOException e) {
			streamReader = new InputStreamReader(conn.getErrorStream(), "UTF-8");
		} finally {
			if (streamReader != null) {
				bufferedReader = new BufferedReader(streamReader);
				sb = new StringBuilder();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
			}
		}
		
		HttpResponse hr = makeContent(url, conn,sb.toString());
		return hr;
    }
    

    /**
     * 得到响应对象
     * @param urlConnection
     * @param content 网页内容
     * @return 响应对象
     * @throws IOException
     */ 
    private static HttpResponse makeContent(String urlString, HttpURLConnection urlConnection, String content) throws IOException { 
        HttpResponse httpResponser = new HttpResponse(); 
        try { 
            String ecod = urlConnection.getContentEncoding(); 
            httpResponser.urlString = urlString; 
            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort(); 
            httpResponser.file = urlConnection.getURL().getFile(); 
            httpResponser.host = urlConnection.getURL().getHost(); 
            httpResponser.path = urlConnection.getURL().getPath(); 
            httpResponser.port = urlConnection.getURL().getPort(); 
            httpResponser.protocol = urlConnection.getURL().getProtocol(); 
            httpResponser.query = urlConnection.getURL().getQuery(); 
            httpResponser.ref = urlConnection.getURL().getRef(); 
            httpResponser.userInfo = urlConnection.getURL().getUserInfo(); 
            httpResponser.content = content;
            httpResponser.contentEncoding = ecod; 
            httpResponser.code = urlConnection.getResponseCode(); 
            httpResponser.message = urlConnection.getResponseMessage(); 
            httpResponser.contentType = urlConnection.getContentType(); 
            httpResponser.method = urlConnection.getRequestMethod(); 
            httpResponser.connectTimeout = urlConnection.getConnectTimeout(); 
            httpResponser.readTimeout = urlConnection.getReadTimeout(); 
            httpResponser.headerFields = urlConnection.getHeaderFields();
            return httpResponser; 
        } catch (IOException e) { 
            throw e; 
        } finally { 
            if (urlConnection != null) 
                urlConnection.disconnect(); 
        } 
    } 
	
	public static void main(String[] args) {
		System.out.println(getLoginRequest("/api/v5/account/bills", ""));
		
	}
}
