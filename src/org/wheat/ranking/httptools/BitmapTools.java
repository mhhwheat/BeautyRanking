package org.wheat.ranking.httptools;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitmapTools {

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
		
}
