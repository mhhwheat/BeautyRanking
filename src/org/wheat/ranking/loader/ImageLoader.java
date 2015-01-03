package org.wheat.ranking.loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.wheat.ranking.cache.ImageFileCache;
import org.wheat.ranking.cache.ImageMemoryCache;
import org.wheat.ranking.entity.PhotoParameters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageLoader 
{
	
	
	private static ImageLoader instance;
	
	private ExecutorService executorService;	//�̳߳�
	private ImageMemoryCache memoryCache;		//�ڴ滺��
	private ImageFileCache fileCache;			//�ļ�����
	private Map<String,ImageView> taskMap;		//�������
	private boolean allowLoad=true;				//�Ƿ��������ͼƬ
	private Context context;
	
	private ImageLoader(Context context)
	{
		this.context=context;
		// ��ȡ��ǰϵͳ��CPU��Ŀ
		int cupNums=Runtime.getRuntime().availableProcessors();
		//����ϵͳ��Դ��������̳߳ش�С
		this.executorService=Executors.newFixedThreadPool(cupNums+1);
		
		this.memoryCache=new ImageMemoryCache(context);
		this.fileCache=new ImageFileCache();
		this.taskMap=new HashMap<String, ImageView>();
	}
	
	/**
	 * ʹ�õ�������֤����Ӧ����ֻ��һ���̳߳غ�һ���ڴ滺����ļ�����
	 * @param context
	 * @return Ψһ��ImageLoader����
	 */
	public static ImageLoader getInstance(Context context)
	{
		if(instance==null)
			instance=new ImageLoader(context);
		return instance;
	}
	
	/**
	 * �ָ�Ϊ��ʼ�ɼ���ͼƬ��״̬
	 */
	public void restore()
	{
		this.allowLoad=true;
	}
	
	/**
     * ��סʱ���������ͼƬ,��ƽ������
     */
	public void lock()
	{
		this.allowLoad=false;
	}
	
	/**
     * ����ʱ����ͼƬ,ֹͣ������ʱ�����ͼƬ
     */
	public void unlock()
	{
		this.allowLoad=true;
		doTask();
	}
	
	public void addTask(PhotoParameters parameters,ImageView img)
	{
		Bitmap bitmap=null;
		Log.w("ImageLoader","addTask-------------->");
		Log.w("ImageLoader","ImageView hashCode:"+img.hashCode());
		Log.w("ImageLoader","url:"+parameters.getUrl());
		if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
		{
			bitmap=memoryCache.getBitmapFromCache(parameters.getUrl());
		}
		else
		{
			bitmap=memoryCache.getBitmapFromCache("thumbnail"+parameters.getUrl());
		}
			
		if(bitmap!=null)
		{
			Log.w("ImageLoader", "setImageBitmap in addTask-------->");
			if(parameters.isFixWidth())
			{
				//����������ԭͼ,�ڹ̶���ȵ�����£���ImageView.(width:height)==Bitmap.(width:heigh)
				int width=parameters.getMinSideLength();
				Log.w("ImageLoader", "parameters.getMinSideLength()="+width);
				int picWidth=bitmap.getWidth();
				int picHeight=bitmap.getHeight();
				int height = (int) (width * 1.0 / picWidth * picHeight);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
				img.setLayoutParams(params);
			}
			img.setImageBitmap(bitmap);
			
			Log.w("ImageLoader","��memoryCache��ȡͼƬ--------"+parameters.getUrl());
			synchronized (taskMap) {
				taskMap.remove(img.hashCode());
			}
		}
		else
		{
			synchronized (taskMap) {
				/**
                 * ��ΪListView��GridView��ԭ�����������Ƴ���Ļ��itemȥ�����������ʾ��item,
                 * �����img��item������ݣ����������taskMap�����ʼ���ǵ�ǰ��Ļ�ڵ�����ImageView��
                 */
				img.setTag(parameters);
				taskMap.put(Integer.toString(img.hashCode()), img);
			}
			if(allowLoad)
			{
				doTask();
			}
		}
	}
	
	/**
     * ���ش�������е�����ͼƬ
     */
	public void doTask()
	{
		Log.w("ImageLoader","doTask-------------->");
		synchronized (taskMap) {
			Collection<ImageView> con=taskMap.values();
			for(ImageView img:con)
			{
				if(img!=null)
				{
					if(img.getTag()!=null)
					{
						loadImage((PhotoParameters)img.getTag(), img);
					}
				}
			}
			taskMap.clear();
		}
	}
	
	private void loadImage(PhotoParameters parameters,ImageView img)
	{
		this.executorService.submit(new TaskWithResult(new TaskHandler(parameters, img), parameters));
	}
	private Bitmap getBitmap(PhotoParameters parameters)
	{
		Bitmap result=null;
		
		
		// ���ڴ滺���л�ȡͼƬ
		Log.w("ImageLoader","���ڴ滺���л�ȡͼƬ------"+parameters.getUrl());
		if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
		{
			result=memoryCache.getBitmapFromCache(parameters.getUrl());
		}
		else
		{
			result=memoryCache.getBitmapFromCache("thumbnail"+parameters.getUrl());
		}
		if(result==null){
			// �ļ������л�ȡ
			Log.w("ImageLoader","���ļ������л�ȡͼƬ------"+parameters.getUrl());
			
			if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
			{
				result=fileCache.getImage(parameters.getUrl());
			}
			else
			{
				result=fileCache.getImage("thumbnail"+parameters.getUrl());
			}
			
			if(result==null){
				// �������ȡ
				Log.w("ImageLoader","�������ȡͼƬ------"+parameters.getUrl());
				try {
					result=HttpLoderMethods.downLoadBitmap(parameters.getUrl(),parameters.getMinSideLength(),parameters.getMaxNumOfPixels());
				} catch (Throwable e) {
					e.printStackTrace();
				}
				if(result!=null)
				{
					if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
					{
						fileCache.saveBitmap(result, parameters.getUrl());
						memoryCache.addBitmapToCache(parameters.getUrl(), result);
					}
					else
					{
						fileCache.saveBitmap(result, "thumbnail"+parameters.getUrl());
						memoryCache.addBitmapToCache("thumbnail"+parameters.getUrl(), result);
					}
					
					
				}
				else
				{
					Log.w("ImageLoader", "resultΪnull!!!!!!");
				}
			}
			else
			{
				
				// ��ӵ��ڴ滺��
				if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
				{
					memoryCache.addBitmapToCache(parameters.getUrl(), result);
				}
				else
				{
					memoryCache.addBitmapToCache("thumbnail"+parameters.getUrl(), result);
				}
			}
			
		}
		return result;
	}
	
	/**
	 * ���߳�����
	 * @author wheat
	 *
	 */
	private class TaskWithResult implements Callable<String>
	{
		private PhotoParameters parameters;
		private Handler handler;
		
		public TaskWithResult(Handler handler,PhotoParameters parameters)
		{
			this.parameters=parameters;
			this.handler=handler;
		}

		@Override
		public String call() throws Exception {
			Message msg=new Message();
			msg.obj=getBitmap(parameters);
			if(msg.obj!=null)
			{
				handler.sendMessage(msg);
			}
			return parameters.getUrl();
		}
	}
	
	//����ui
	private static class TaskHandler extends Handler
	{
		PhotoParameters parameters;
		ImageView img;
		
		public TaskHandler(PhotoParameters parameters,ImageView img)
		{
			this.parameters=parameters;
			this.img=img;
		}

		@Override
		public void handleMessage(Message msg) 
		{
			/*** �鿴ImageView��Ҫ��ʾ��ͼƬ�Ƿ񱻸ı�  ***/
			if(img.getTag().equals(parameters))
			{
				if(msg.obj!=null)
				{
					Bitmap bitmap=(Bitmap)msg.obj;
					Log.w("ImageLoader", "setImageBitmap in handler-------->");
					if(parameters.isFixWidth())
					{
						img.measure(0, 0);
						Log.w("ImageLoader", "img.getMeasureWidth="+img.getMeasuredWidth());
						//����������ԭͼ,�ڹ̶���ȵ�����£���ImageView.(width:height)==Bitmap.(width:heigh)
						int width=parameters.getMinSideLength();
						Log.w("ImageLoader", "parameters.getMinSideLength()="+width);
						int picWidth=bitmap.getWidth();
						int picHeight=bitmap.getHeight();
						int height = (int) (width * 1.0 / picWidth * picHeight);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
						img.setLayoutParams(params);
					}
					img.setImageBitmap(bitmap);
				}
			}
		}
		
		
	}
	
	
	
	
}
