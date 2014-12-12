package org.wheat.ranking.httptools;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.wheat.ranking.entity.json.JsonBaseImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpConnectTools 
{
	private static final int SO_TIMEOUT = 0;

	/**
	 * @deprecated using the get method to post data and receive the json data
	 * @param url   the address
	 * @param data  data to post in the url ,using the map
	 * @param headers   set the request parameters ,default null
	 * @return   the response with the data
	 * @throws IOException
	 */
	public static String getReturnJsonData(String url,HashMap<String,String> data,HashMap<String,String>headers) throws IOException
	{
		if (url == null) {
            return null;
        }
		String newUrl=url;
		if(data!=null){
			try {
				newUrl=addDatatoUrl(url,data,"utf-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpGet httpGet=new HttpGet(newUrl);
		
		//http��ͷ
		if(headers==null)
		{
			headers=new HashMap<String, String>();
		}
		addHeaders(httpGet, headers);//����http��ͷ
		
		//��ʼ����
		HttpResponse rsp=httpClient.execute(httpGet);
		System.out.println("hoga"+" "+rsp.getStatusLine().getStatusCode());
		String result=getStringContentFromHttpResponse(rsp);
		
		return result;
		
	}
	/**
	 * @deprecated using the get method 
	 * @param url  this url without the data
	 * @param data
	 * @param headers
	 * @return the response code (without the data)
	 * @throws IOException
	 */
	public static int  getReturnCode(String url,HashMap<String,String> data,HashMap<String,String>headers) throws IOException
	{
		if (url == null) {
            return -1;
        }
		String newUrl=url;
		if(data!=null){
			try {
				newUrl=addDatatoUrl(url,data,"utf-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpGet httpGet=new HttpGet(newUrl);
		
		//http��ͷ
		if(headers==null)
		{
			headers=new HashMap<String, String>();
		}
		addHeaders(httpGet, headers);//����http��ͷ
		
		//��ʼ����
		HttpResponse rsp=httpClient.execute(httpGet);
		
		
		return rsp.getStatusLine().getStatusCode();
		
	}
	/**
	 * @author  hogachen
	 * @deprecated add the data which going to post  to the url paramters
	 * @param path
	 * @param params
	 * @param enc
	 * @return
	 * @throws Exception
	 */
	public static String addDatatoUrl(String path, Map<String, String> params, String enc)throws Exception{
		StringBuilder sb = new StringBuilder(path);  
        sb.append('?');  
        for(Map.Entry<String, String> entry : params.entrySet()){  
            sb.append(entry.getKey()).append('=')  
                .append(URLEncoder.encode(entry.getValue(), enc)).append('&');  
        }  
        sb.deleteCharAt(sb.length()-1);  
         System.out.println("hoga"+sb.toString()); 
         return sb.toString();
	}
	/**
	 * @author mhhwheat
	 * @description get data and return json data 
	 * @param url this url have include the data
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public static String get(String url,HashMap<String,String> headers) throws IOException
	{
		if (url == null) {
            return null;
        }
		
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpGet httpGet=new HttpGet(url);
		
		//http��ͷ
		if(headers==null)
		{
			headers=new HashMap<String, String>();
		}
		addHeaders(httpGet, headers);//����http��ͷ
		
		//��ʼ����
		HttpResponse rsp=httpClient.execute(httpGet);
		
		String result=getStringContentFromHttpResponse(rsp);
		
		return result;
		
	}
	/**
	 * @author mhhwheat
	 * @param url
	 * @param postData
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public static String post(String url,byte[] postData,HashMap<String,String> headers) throws IOException
	{
		if (postData == null || postData.length <= 0) {
            return null;
        }
		
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpPost httpPost=new HttpPost(url);
		
		//http��ͷ
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addHeaders(httpPost, headers);//����http��ͷ
        
        //д����
        ByteArrayEntity entity=new ByteArrayEntity(postData);
        httpPost.setEntity(entity);
        //��ʼ����
        HttpResponse rsp=httpClient.execute(httpPost);
        
        String result=getStringContentFromHttpResponse(rsp);
        
        return result;
	}
	/**
	 * @deprecated using  json to deliver the data
	 * @author hogachen
	 * @param url  Ҫ�����url
	 * @param headers  �����ͷ����Ϣ
	 * @param object  Ҫ���͵Ķ���
	 * @return	���صĶ�������ֻ�Ǵ���StringEntity
	 * @throws IOException
	 */
	public static Object postJsonReturnJson(String url,HashMap<String,String> headers,Object object) throws IOException
	{
		
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpPost httpPost=new HttpPost(url);
		
		//http��ͷ
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addHeaders(httpPost, headers);//����http��ͷ
        
        String objectJson=JsonTools.toJson(object);
        httpPost.setEntity(new StringEntity(objectJson));
        //��ʼ����
        System.out.println("in post withoutdata1");
        HttpResponse rsp=httpClient.execute(httpPost);
        System.out.println("in post withoutdata2");
        String result=getStringContentFromHttpResponse(rsp);
//        System.out.println(result);
        
        if(result!=null){
        	return JsonTools.fromJson(result, JsonBaseImpl.class);
        }
        
        return null;
	}
		public static int postJsonReturnCode(String url,Object object,HashMap<String,String> headers) throws IOException
	{
		
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpPost httpPost=new HttpPost(url);
		
		//http��ͷ
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addHeaders(httpPost, headers);//����http��ͷ
        
        String objectJson=JsonTools.toJson(object);
        System.out.println(objectJson);
        httpPost.setEntity(new StringEntity(objectJson));
        //��ʼ����
        System.out.println("in post withoutdata1");
        HttpResponse rsp=httpClient.execute(httpPost);
        System.out.println("in post withoutdata2");
        return rsp.getStatusLine().getStatusCode();
	}
	/*
	public static Bitmap getPhoto(String url,byte[] postData,HashMap<String,String> headers) throws  IOException
	{
		if (postData == null || postData.length <= 0 || context == null) {
            return null;
        }
        
		context = context.getApplicationContext();
		
		HttpClient httpClient=new DefaultHttpClient(createHttpParams(context));
		
		HttpPost httpPost=new HttpPost(url);
		
		//http��ͷ
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addHeaders(httpPost, headers);//����http��ͷ
        
        //д����
        ByteArrayEntity entity=new ByteArrayEntity(postData);
        httpPost.setEntity(entity);
        //��ʼ����
        HttpResponse rsp=httpClient.execute(httpPost);
		return getPhotoFromHttpResponse(rsp);
	}
	*/
	
	
	/**
     * ��http������뱨ͷ
     *
     * @param req
     * @param headers
     */
    public static void addHeaders(HttpRequest req, HashMap<String, String> headers) {
        try {
            if (headers == null || req == null) {
                return;
            }
            Iterator<Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                String key = entry.getKey();
                String value = entry.getValue();
                req.addHeader(key, value);
            }

        } catch (Throwable e) {
        	e.printStackTrace();
        }
    }
	
    /**
     * ����http��Ӧ��ȡͼƬ
     * @param rsp
     * @return Bitmap
     * @throws IOException
     */
    public static Bitmap getPhotoFromHttpResponse(HttpResponse rsp) throws IOException
    {
    	if(rsp==null)
    		return null;
    	if(rsp.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
    	{
    		HttpEntity entity=rsp.getEntity();
    		if(entity==null)
    		{
    			return null;
    		}
    		Header header=entity.getContentEncoding();
    		byte[] data;
    		if (header == null || header.getValue() == null) {
                 //˵��û��ѹ�� 
                 data=EntityUtils.toByteArray(entity);
            }
    		else
    			data=EntityUtils.toByteArray(entity);
    		return BitmapFactory.decodeByteArray(data, 0, data.length);
    	}
    	return null;
    	
    }
    
    /**
     * 
     * @param rsp
     * @return
     * @throws IOException
     */
    public static String getStringContentFromHttpResponse(HttpResponse rsp) throws IOException
    {
    	if(rsp==null)
    	{System.out.println("response is null");
    		return null;
    	}
    	if(rsp.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
    	{
    		HttpEntity entity=rsp.getEntity();
    		if(entity==null)
    		{
    			return null;
    		}
    		Header header = entity.getContentEncoding();
            if (header == null || header.getValue() == null) {
                //˵��û��ѹ��
                return EntityUtils.toString(entity);
            }
            return EntityUtils.toString(entity);
    	}
    	System.out.println("������� �� "+rsp.getStatusLine().getStatusCode());
    	return null;
    }
    
    /**
     * ��entityתΪString��ͨ��Gzipѹ��
     *
     * @param stream
     * @return
     */
    public static String loadStringFromGzipStream(InputStream stream) throws IOException {

        GZIPInputStream gzin = new GZIPInputStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] buff = new byte[1024];
        int size = buff.length;
        while ((len = gzin.read(buff, 0, size)) > 0) {
            baos.write(buff, 0, len);
        }
        baos.flush();
        gzin.close();
        String rt = baos.toString("utf-8");
        baos.close();
        return rt;
    }
    
	/**
	 * ����http�������
	 * @param context
	 * @return
	 */
	public static HttpParams createHttpParams()
	{
		BasicHttpParams params = new BasicHttpParams();

        // ����http��ʱ(30��)
        HttpConnectionParams.setConnectionTimeout(params, 30*1000);

        // ����socket��ʱ(15��)->(30��)-2013-05-14 �ȴ�����ʱ��
        HttpConnectionParams.setSoTimeout(params, 30*1000);

//        Long CONN_MANAGER_TIMEOUT = 500L; //��ֵ�������Ӳ����õ�ʱ��ȴ���ʱʱ�䣬һ��Ҫ���ã����Ҳ���̫�� ()

        //���ύ����֮ǰ ���������Ƿ����
//        params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
        // ���ô����Զ������ض���
        HttpClientParams.setRedirecting(params, true);
        
        
        return params;
	}
}
