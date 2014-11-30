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

public class ImageLoader 
{
	
	
	private static ImageLoader instance;
	
	private ExecutorService executorService;	//线程池
	private ImageMemoryCache memoryCache;		//内存缓存
	private ImageFileCache fileCache;			//文件缓存
	private Map<String,ImageView> taskMap;		//存放任务
	private boolean allowLoad=true;				//是否允许加载图片
	private Context context;
	
	private ImageLoader(Context context)
	{
		this.context=context;
		// 获取当前系统的CPU数目
		int cupNums=Runtime.getRuntime().availableProcessors();
		//根据系统资源情况灵活定义线程池大小
		this.executorService=Executors.newFixedThreadPool(cupNums+1);
		
		this.memoryCache=new ImageMemoryCache(context);
		this.fileCache=new ImageFileCache();
		this.taskMap=new HashMap<String, ImageView>();
	}
	
	/**
	 * 使用单例，保证整个应用中只有一个线程池和一份内存缓存和文件缓存
	 * @param context
	 * @return 唯一的ImageLoader对象
	 */
	public static ImageLoader getInstance(Context context)
	{
		if(instance==null)
			instance=new ImageLoader(context);
		return instance;
	}
	
	/**
	 * 恢复为初始可加载图片的状态
	 */
	public void restore()
	{
		this.allowLoad=true;
	}
	
	/**
     * 锁住时不允许加载图片,滑平不加载
     */
	public void lock()
	{
		this.allowLoad=false;
	}
	
	/**
     * 解锁时加载图片,停止滑屏的时候加载图片
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
			img.setImageBitmap(bitmap);
			Log.w("ImageLoader","从memoryCache获取图片--------"+parameters.getUrl());
			synchronized (taskMap) {
				taskMap.remove(img.hashCode());
			}
		}
		else
		{
			synchronized (taskMap) {
				/**
                 * 因为ListView或GridView的原理是用上面移出屏幕的item去填充下面新显示的item,
                 * 这里的img是item里的内容，所以这里的taskMap保存的始终是当前屏幕内的所有ImageView。
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
     * 加载存放任务中的所有图片
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
		
		
		// 从内存缓存中获取图片
		Log.w("ImageLoader","从内存缓存中获取图片------"+parameters.getUrl());
		if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
		{
			result=memoryCache.getBitmapFromCache(parameters.getUrl());
		}
		else
		{
			result=memoryCache.getBitmapFromCache("thumbnail"+parameters.getUrl());
		}
		if(result==null){
			// 文件缓存中获取
			Log.w("ImageLoader","从文件缓存中获取图片------"+parameters.getUrl());
			
			if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
			{
				result=fileCache.getImage(parameters.getUrl());
			}
			else
			{
				result=fileCache.getImage("thumbnail"+parameters.getUrl());
			}
			
			if(result==null){
				// 从网络获取
				Log.w("ImageLoader","从网络获取图片------"+parameters.getUrl());
				try {
					result=HttpDataLoader.downLoadBitmap(parameters.getUrl(),parameters.getMinSideLength(),parameters.getMaxNumOfPixels());
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
			}
			else
			{
				
				// 添加到内存缓存
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
	 * 子线程任务
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
	
	//更新ui
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
			/*** 查看ImageView需要显示的图片是否被改变  ***/
			if(img.getTag().equals(parameters))
			{
				if(msg.obj!=null)
				{
					Bitmap bitmap=(Bitmap)msg.obj;
					Log.w("ImageLoader", "setImageBitmap in handler-------->");
					img.setImageBitmap(bitmap);
				}
			}
		}
		
		
	}
	
	
	
	
}
