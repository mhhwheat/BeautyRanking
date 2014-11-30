package org.wheat.ranking.loader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpLoader 
{
	public static String get(String url,HashMap<String,String> headers) throws IOException
	{
		if (url == null) {
            return null;
        }
		
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpGet httpGet=new HttpGet(url);
		
		//http报头
		if(headers==null)
		{
			headers=new HashMap<String, String>();
		}
		addHeaders(httpGet, headers);//加入http报头
		
		//开始请求
		HttpResponse rsp=httpClient.execute(httpGet);
		
		String result=getStringContentFromHttpResponse(rsp);
		
		return result;
		
	}
	
	public static String post(String url,byte[] postData,HashMap<String,String> headers) throws IOException
	{
		if (postData == null || postData.length <= 0) {
            return null;
        }
		
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpPost httpPost=new HttpPost(url);
		
		//http报头
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addHeaders(httpPost, headers);//加入http报头
        
        //写入流
        ByteArrayEntity entity=new ByteArrayEntity(postData);
        httpPost.setEntity(entity);
        //开始请求
        HttpResponse rsp=httpClient.execute(httpPost);
        
        String result=getStringContentFromHttpResponse(rsp);
        
        return result;
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
		
		//http报头
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addHeaders(httpPost, headers);//加入http报头
        
        //写入流
        ByteArrayEntity entity=new ByteArrayEntity(postData);
        httpPost.setEntity(entity);
        //开始请求
        HttpResponse rsp=httpClient.execute(httpPost);
		return getPhotoFromHttpResponse(rsp);
	}
	*/
	
	//下载完整的图片
	public static Bitmap getFullPhoto(String url,HashMap<String,String> headers) throws IOException
	{
		URL imgUrl;
		Bitmap bitmap=null;
		 try {
	            imgUrl = new URL(url);
	            InputStream is = imgUrl.openConnection().getInputStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            bitmap = BitmapFactory.decodeStream(bis);
	            bis.close();
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return bitmap;
	}
	
	/**
	 * 当minSideLength和maxNumOfPixels同时等于-1时,将会下载完整的图片,否则下载缩略图
	 * @param context
	 * @param url
	 * @param headers
	 * @param minSideLength 最小边长
	 * @param maxNumOfPixels 像素总数
	 * @return
	 */
	public static Bitmap getPhoto(String url,HashMap<String,String> headers,int minSideLength,int maxNumOfPixels)
	{
		URL imgUrl;
		Bitmap bitmap=null;
		 try {
	            imgUrl = new URL(url);
	            InputStream is = imgUrl.openConnection().getInputStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            bitmap = BitmapFactory.decodeStream(bis);
	            bis.close();
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return bitmap;
	}
	
	
	/**
	 * 
	 * @param options BitmapFactory.options
	 * @param minSideLength 最小边长
	 * @param maxNumOfPixels 像素总数
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {  
	    int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);  
	    int roundedSize;  
	    if (initialSize <= 8) {  
	        roundedSize = 1;  
	        while (roundedSize < initialSize) {  
	            roundedSize <<= 1;  
	        }  
	    } else {  
	        roundedSize = (initialSize + 7) / 8 * 8;  
	    }  
	    return roundedSize;  
	} 
	
	/**
	 * 
	 * @param options BitmapFactory.options
	 * @param minSideLength 最小边长
	 * @param maxNumOfPixels 像素总数
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
	    double w = options.outWidth;  
	    double h = options.outHeight;  
	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
	    int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
	    if (upperBound < lowerBound) {  
	        // return the larger one when there is no overlapping zone.  
	        return lowerBound;  
	    }  
	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
	        return 1;  
	    } else if (minSideLength == -1) {  
	        return lowerBound;  
	    } else {  
	        return upperBound;  
	    }  
	} 
	
	/**
     * 往http请求加入报头
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

        }
    }
	
    /**
     * 根据http响应获取图片
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
                 //说明没有压缩 
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
    	{
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
                //说明没有压缩
                return EntityUtils.toString(entity);
            }
            return EntityUtils.toString(entity);
    	}
    	return null;
    }
    
    /**
     * 将entity转为String，通过Gzip压缩
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
	 * 创建http请求参数
	 * @param context
	 * @return
	 */
	public static HttpParams createHttpParams()
	{
		BasicHttpParams params = new BasicHttpParams();

        // 设置http超时(30秒)
        HttpConnectionParams.setConnectionTimeout(params, 30000);

        // 设置socket超时(15秒)->(30秒)-2013-05-14
        HttpConnectionParams.setSoTimeout(params, 30000);

        // 设置处理自动处理重定向
        HttpClientParams.setRedirecting(params, true);

        return params;
	}
}
