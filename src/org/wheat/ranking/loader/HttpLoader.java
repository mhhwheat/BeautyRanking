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
import android.util.Log;

public class HttpLoader 
{
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
	
	//����������ͼƬ
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
	 * ��minSideLength��maxNumOfPixelsͬʱ����-1ʱ,��������������ͼƬ,������������ͼ
	 * @param context
	 * @param url
	 * @param headers
	 * @param minSideLength ��С�߳�
	 * @param maxNumOfPixels ��������
	 * @return
	 */
	public static Bitmap getPhoto(String url,HashMap<String,String> headers,int minSideLength,int maxNumOfPixels)
	{
		URL imgUrl;
		Bitmap bitmap=null;
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inJustDecodeBounds=true;
		
		 try {
	            imgUrl = new URL(url);
	            InputStream is = imgUrl.openConnection().getInputStream();
	            //BufferedInputStream bis = new BufferedInputStream(is);
	            byte[] buffer=getBytes(is);
	            BitmapFactory.decodeByteArray(buffer, 0,buffer.length, opts);
	            //BitmapFactory.decodeStream(is,null,opts);
	            Log.w("HttpLoader", "ͼƬ�ĸ�Ϊ"+opts.outHeight);
	            Log.w("HttpLoader", "ͼƬ�Ŀ�Ϊ"+opts.outWidth);
	            opts.inSampleSize=computeSampleSize(opts, minSideLength, maxNumOfPixels);
	            Log.w("HttpLoader", "inSampleSizeΪ"+opts.inSampleSize);
	            opts.inJustDecodeBounds=false;
	            //bitmap=BitmapFactory.decodeStream(bis, null, opts);
	            bitmap=BitmapFactory.decodeByteArray(buffer, 0, buffer.length, opts);
	            is.close();
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return bitmap;
	}
	
	private static byte[] getBytes(InputStream is) throws IOException
	{
		if(is==null)
			return null;
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] b=new byte[1024];
		int len=0;
		
		while((len=is.read(b,0,1024))!=-1)
		{
			baos.write(b,0,len);
			baos.flush();
		}
		byte[] bytes=baos.toByteArray();
		return bytes;
	}
	
	
	/**
	 * 
	 * @param options BitmapFactory.options
	 * @param minSideLength ��С�߳�
	 * @param maxNumOfPixels ��������
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
	 * @param minSideLength ��С�߳�
	 * @param maxNumOfPixels ��������
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
                //˵��û��ѹ��
                return EntityUtils.toString(entity);
            }
            return EntityUtils.toString(entity);
    	}
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
        HttpConnectionParams.setConnectionTimeout(params, 30000);

        // ����socket��ʱ(15��)->(30��)-2013-05-14
        HttpConnectionParams.setSoTimeout(params, 30000);

        // ���ô����Զ������ض���
        HttpClientParams.setRedirecting(params, true);

        return params;
	}
}
