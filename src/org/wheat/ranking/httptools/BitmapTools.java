package org.wheat.ranking.httptools;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.util.Log;

public class BitmapTools {

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
			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inJustDecodeBounds=true;
			
			 try {
		            imgUrl = new URL(url);
		            InputStream is = imgUrl.openConnection().getInputStream();
		            //BufferedInputStream bis = new BufferedInputStream(is);
		            byte[] buffer=getBytes(is);
		            BitmapFactory.decodeByteArray(buffer, 0,buffer.length, opts);
		            //BitmapFactory.decodeStream(is,null,opts);
		            Log.w("HttpLoader", "图片的高为"+opts.outHeight);
		            Log.w("HttpLoader", "图片的宽为"+opts.outWidth);
		            opts.inSampleSize=computeSampleSize(opts, minSideLength, maxNumOfPixels);
		            Log.w("HttpLoader", "inSampleSize为"+opts.inSampleSize);
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
		 * 
		* @Description: 由原文件生成缩略图
		* @author hogachen   
		* @date 2014年12月18日 下午9:14:52 
		* @version V1.0  
		* @param originFile
		* @return
		 */
//		public static File genThumbnailFile(File originFile){
//			 Bitmap bitmap = ThumbnailUtils.extractThumbnail(bitmap, 51, 108); 
//			return new File("");
//		}
		
		public static void compressBmpToFile(File originFile,File thumbnailFile){
			Bitmap bmp = BitmapFactory.decodeFile(originFile.getPath());
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    int options = 80;//个人喜欢从80开始,
		    bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		    while (baos.toByteArray().length / 1024 > 100) { 
		      baos.reset();
		      options -= 10;
		      bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		    }
		    try {
		      FileOutputStream fos = new FileOutputStream(thumbnailFile);
		      fos.write(baos.toByteArray());
		      fos.flush();
		      fos.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		  }
		
		
		
		
}
