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
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ImageLoader 
{
	
	private final boolean DEBUG=true;
	private static ImageLoader instance;
	
	private ExecutorService executorService;	//线程池
	private ImageMemoryCache memoryCache;		//内存缓存
	private ImageFileCache fileCache;			//文件缓存
	private Map<String,View> taskMap;		//存放任务
	private boolean allowLoad=true;				//是否允许加载图片
	private static Context context;
	
	private ImageLoader(Context context)
	{
		this.context=context;
		// 获取当前系统的CPU数目
		int cupNums=Runtime.getRuntime().availableProcessors();
		//根据系统资源情况灵活定义线程池大小
		this.executorService=Executors.newFixedThreadPool(cupNums+1);
		
		this.memoryCache=new ImageMemoryCache(context);
		this.fileCache=new ImageFileCache();
		this.taskMap=new HashMap<String, View>();
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
	
	
	
	
	public void addTask(PhotoParameters parameters,View view)
	{
		Bitmap bitmap=null;
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
			if(view instanceof ImageView)
			{
				ImageView imageView=(ImageView)view;
				if(parameters.isFixWidth()&&parameters.getImageViewWidth()>0)
				{
					//如果请求的是原图,在固定宽度的情况下，是ImageView.(width:height)==Bitmap.(width:heigh)
					int width=parameters.getImageViewWidth();			
					int picWidth=bitmap.getWidth();
					int picHeight=bitmap.getHeight();
					int height = (int) (width * 1.0 / picWidth * picHeight);
					LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)imageView.getLayoutParams();
					params.width=width;
					params.height=height;
					imageView.setLayoutParams(params);
				}
				imageView.setImageBitmap(bitmap);
			}
			if(view instanceof RelativeLayout)
			{

				RelativeLayout layout=(RelativeLayout)view;
				if(parameters.isFixWidth()&&parameters.getImageViewWidth()>0)
				{
					int width=parameters.getImageViewWidth();
					int picWidth=bitmap.getWidth();
					int picHeight=bitmap.getHeight();
					int height = (int) (width * 1.0 / picWidth * picHeight);
					RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)layout.getLayoutParams();
//					RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width, height);
					params.width=width;
					params.height=height;
					layout.setLayoutParams(params);
				}
				layout.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));
			}
			
			synchronized (taskMap) {
				taskMap.remove(view.hashCode());
			}
		}
		else
		{
			synchronized (taskMap) {
				/**
                 * 因为ListView或GridView的原理是用上面移出屏幕的item去填充下面新显示的item,
                 * 这里的img是item里的内容，所以这里的taskMap保存的始终是当前屏幕内的所有ImageView。
                 */
				view.setTag(parameters);
				taskMap.put(Integer.toString(view.hashCode()), view);
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
		synchronized (taskMap) {
			Collection<View> con=taskMap.values();
			for(View view:con)
			{
				if(view!=null)
				{
					if(view.getTag()!=null)
					{
						loadImage((PhotoParameters)view.getTag(), view);
					}
				}
			}
			taskMap.clear();
		}
	}
	
	private void loadImage(PhotoParameters parameters,View view)
	{
		this.executorService.submit(new TaskWithResult(new TaskHandler(parameters, view), parameters));
	}
	private Bitmap getBitmap(PhotoParameters parameters)
	{
		Bitmap result=null;
		
		
		// 从内存缓存中获取图片
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
					if(DEBUG)
					{
						Log.d("ImageLoader", "result为null!!!!!!");
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
		View view;
		
		public TaskHandler(PhotoParameters parameters,View view)
		{
			this.parameters=parameters;
			this.view=view;
		}

		@Override
		public void handleMessage(Message msg) 
		{
			/*** 查看ImageView需要显示的图片是否被改变  ***/
			if(view.getTag().equals(parameters))
			{
				if(msg.obj!=null)
				{
					Bitmap bitmap=(Bitmap)msg.obj;
					
					if(view instanceof ImageView)
					{
						ImageView imageView=(ImageView)view;
						if(parameters.isFixWidth()&&parameters.getImageViewWidth()>0)
						{
							//如果请求的是原图,在固定宽度的情况下，是ImageView.(width:height)==Bitmap.(width:heigh)
							int width=parameters.getImageViewWidth();			
							int picWidth=bitmap.getWidth();
							int picHeight=bitmap.getHeight();
							int height = (int) (width * 1.0 / picWidth * picHeight);
							LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)imageView.getLayoutParams();
							params.width=width;
							params.height=height;
							imageView.setLayoutParams(params);
						}
						imageView.setImageBitmap(bitmap);
					}
					
					if(view instanceof RelativeLayout)
					{
						RelativeLayout layout=(RelativeLayout)view;
						if(parameters.isFixWidth()&&parameters.getImageViewWidth()>0)
						{
							int width=parameters.getImageViewWidth();
							int picWidth=bitmap.getWidth();
							int picHeight=bitmap.getHeight();
							int height = (int) (width * 1.0 / picWidth * picHeight);
							RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)layout.getLayoutParams();
//							RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width, height);
							params.width=width;
							params.height=height;
							layout.setLayoutParams(params);
						}
						layout.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));
					}
					
				}
			}
		}
		
		
	}
	
	
	
	
}
