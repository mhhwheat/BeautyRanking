package org.wheat.ranking.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


import org.wheat.beautyranking.R;
import org.wheat.ranking.wheelview.*;
import org.wheat.ranking.data.UserLoginPreference;
import org.wheat.ranking.entity.BeautyDetail;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.MyLocation;
import org.wheat.ranking.httptools.BitmapTools;
import org.wheat.ranking.httptools.DateFormatTools;
import org.wheat.ranking.loader.HttpUploadMethods;
import org.wheat.ranking.location.NetLocation;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class CreateBeauty extends Activity implements OnClickListener, AMapLocationListener {


	/**
	 * 高德地图定位信息
	 */
	private LocationManagerProxy mLocationManagerProxy;
	private  MyLocation myLocation = new MyLocation();;
	
	private LinearLayout tvPersonInfo;
	private int privilege=2;
	private ImageView iv = null;
	String truename;
	String birthday;
	String school;
	String description;
	String locationText;
//	private EditText edt_beauty_info_school=null;
//	private EditText edt_beauty_info_admission=null;
//	private EditText edt_beauty_info_birthday=null;//实际上改为微信号
	private TextView textview_location=null;//实际上是地理信息
//	private EditText edt_beauty_info_constellation=null;
//	private EditText edt_beauty_info_description=null;
	private Button btn_submit=null;
	private String tp = null;

	private String photoName = null;
	
	
	private LinearLayout llPrivilege; 
	private TextView tvPrivilegeShow;
	private String[] names ={" 仅自己可添加 "," 仅通讯录好友可添加 "," 所有人可添加 "};// {" 浙江 "," 江苏 "," 山东 "," 江西 "," 湖南 "," 广东 "};//
	File tempfile =null;
	//生成的图片的文件名，为了方便，设置一个公共变量
	String filename=null;
	/** Called when the activity is first created. */
	
	Handler submitHandler=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//请求设置标题栏
		setContentView(R.layout.create_new_beauty);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.create_page_title);
		// 初始化
		init();
		// 开启界面的时候就定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 150, this);
		// toast handler
		submitHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == ConstantValue.operateSuccess) {
					Toast.makeText(getApplicationContext(), "发布成功",
							Toast.LENGTH_SHORT).show();
				} else if(msg.what==-3){
					Toast.makeText(getApplicationContext(),
							"您还没填写个人信息呢！", Toast.LENGTH_SHORT)
							.show();
				}else if(msg.what==-2){
					Toast.makeText(getApplicationContext(),
							"您还没有为Ta选择靓照呢", Toast.LENGTH_SHORT)
							.show();
				}else if(msg.what==-5){
					Toast.makeText(getApplicationContext(),
							"服务器不可链接", Toast.LENGTH_SHORT)
							.show();
				}else{
					Toast.makeText(getApplicationContext(),
							"发布失败，错误代码为："+msg.what, Toast.LENGTH_SHORT)
							.show();
				}
			}
		};
	}

	/**
	 * 初始化方法实现
	 */
	private void init() {
		iv = (ImageView) findViewById(R.id.imageView1);
		tvPersonInfo=(LinearLayout)findViewById(R.id.llPersonInfo);
//		edt_beauty_info_school=(EditText)findViewById(R.id.beauty_info_school);
//		edt_beauty_info_admission=(EditText)findViewById(R.id.beauty_info_admission);
//		edt_beauty_info_birthday=(EditText)findViewById(R.id.beauty_info_birthday);
//		edt_beauty_info_constellation=(EditText)findViewById(R.id.beauty_info_constellation);
//		edt_beauty_info_description=(EditText)findViewById(R.id.beauty_info_description);
		textview_location=(TextView)findViewById(R.id.beauty_info_location);
		btn_submit= (Button)findViewById(R.id.submit);
		llPrivilege=(LinearLayout)findViewById(R.id.linear3);
		tvPrivilegeShow=(TextView)findViewById(R.id.beauty_info_privilege_text);
		llPrivilege.setOnClickListener(PrivilegeListener);
		iv.setOnClickListener(this);
//		threadToGetLocation();
		tvPersonInfo.setOnClickListener(personinfolistener);
		btn_submit.setOnClickListener(submitListener);
	}
	OnClickListener PrivilegeListener = new OnClickListener(){
		public void onClick(View v) {
			final AlertDialog dialog = new AlertDialog.Builder(CreateBeauty.this).create();
			dialog.setTitle("权限设置");
			final WheelView catalogWheel = new WheelView(CreateBeauty.this);
			dialog.setButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					privilege=catalogWheel.getCurrentItem();
					System.out.println("privilege "+privilege);
					tvPrivilegeShow.setText(names[privilege]);
					dialog.dismiss();
					// 实现下ui的刷新
				}
			});
			dialog.setButton2("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		
			catalogWheel.setVisibleItems(5);
			catalogWheel.setCyclic(true);
			catalogWheel.setAdapter(new ArrayWheelAdapter<String>(names));
			dialog.setView(catalogWheel);
			dialog.show();
			}
	};
	OnClickListener personinfolistener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent personIntent = new Intent();
			personIntent.setClass(CreateBeauty.this, PersonInfo.class);
			startActivityForResult(personIntent,5);
		}
		
	};
	OnClickListener submitListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Thread submitThread= new Thread(){
				public void run(){
					BeautyDetail beauty=getDataFromEditText();
					uploadBeautyInfo(beauty);
//					finish();
				}
			};
			// TODO Auto-generated method stub
			submitThread.start();
		}
	};

	
	/**
	 * 控件点击事件实现
	 * 
	 * 因为有朋友问不同控件的背景图裁剪怎么实现， 我就在这个地方用了三个控件，只为了自己记录学习 大家觉得没用的可以跳过啦
	 */
	@Override
	public void onClick(View v) { 
		switch (v.getId()) {

		case R.id.imageView1:
			ShowPickDialog();
			break;
		case R.id.submit:
			
			break;
		default:
			break;
		}
	}

	/**
	 * 选择提示对话框
	 */
	private void ShowPickDialog() {
		new AlertDialog.Builder(this)
				.setTitle("获取照片")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_PICK, null);

						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 1);

					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();

						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);

						// 由于需要手动获取图片所以需要将照片的名称存储在全局变量中
						SimpleDateFormat timeStampFormat = new SimpleDateFormat(
								"yyyy_MM_dd_HH_mm_ss");
						photoName = timeStampFormat.format(new Date()) + ".jpg";

						// 下面这句指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										photoName)));
						startActivityForResult(intent, 2);
					}
				}).show();
	}

	/**
	 * @description 获取照片后的图片处理回调函数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		//获取地理位置信息
		case 0:
			System.out.println("return the map");
			if(data!=null&&data.getExtras()!=null){
				
				Bundle bundle =data.getExtras(); 
				 this.myLocation.setLat(bundle.getDouble("lat"));
		         this.myLocation.setLng(bundle.getDouble("lng"));
		         this.myLocation.setLocationMessage(bundle.getString("message"));
		         textview_location.setText(this.myLocation.getLocationMessage());
		         System.out.println(this.myLocation.getLocationMessage());
			}	
			break;
		// 如果是直接从相册获取	
		case 1:
			if (data != null) {
				setPicToView(data.getData());
				
				String photoPath=getUriPath(data.getData());
				tempfile = new File(photoPath);
				System.out.println("tempfile.getAbsolutePath()  "+tempfile.getAbsolutePath());
				System.out.println("photoPath  "+photoPath);
			}

			break;
		// 如果是调用相机拍照时
		case 2:

			if (data != null && data.getData() != null) {
				setPicToView(data.getData());
				String photoPath=getUriPath(data.getData());
				tempfile = new File(photoPath);
			}
			if (data == null) {
				tempfile = new File(Environment.getExternalStorageDirectory()
						+ "/" + photoName);
				setPicToView(Uri.fromFile(tempfile));
			}
			break;
		// 取得裁剪后的图片
		case 3:
			if (data != null) {
				setPicToView(data.getData());
			}
			break;
		case 5:
			if(data!=null){
				truename = data.getStringExtra("truename");
				birthday=data.getStringExtra("birthday");
				school = data.getStringExtra("school");
				description = data.getStringExtra("description");
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getUriPath(Uri originalUri){
		
		// 这里开始的第二部分，获取图片的路径： 
		String[] proj = {MediaStore.Images.Media.DATA}; 
		Cursor cursor = managedQuery(originalUri, proj, null, null, null); 
		//按我个人理解 这个是获得用户选择的图片的索引值 
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
		cursor.moveToFirst(); 
		//最后根据索引值获取图片路径 
		String path = cursor.getString(column_index);
		return path;
	}
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Uri picdata) {

		if (picdata != null) {
			Bitmap bitmap2 = null;
			ContentResolver contentResolver = this.getContentResolver();
			try {
				bitmap2 = BitmapFactory.decodeStream(contentResolver
						.openInputStream(picdata));
				System.out.println("image get " + bitmap2.getWidth());
			} catch (Exception e) {
				e.printStackTrace();
			}

			/**
			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
			 */

			/*
			 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 * photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); byte[] b
			 * = stream.toByteArray(); // 将图片流以字符串形式存储下来
			 * 
			 * tp = new String(Base64Coder.encodeLines(b));
			 * 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼
			 * 
			 * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型就OK啦...吼吼
			 * Bitmap dBitmap = BitmapFactory.decodeFile(tp); Drawable drawable
			 * = new BitmapDrawable(dBitmap);
			 */
			setPictureAdjustToView(bitmap2);
		}
	}

	/**
	 * 
	* @Description: TODO 设置imageview 设配图片
	* @author hogachen   
	* @date 2014年12月18日 下午7:37:58 
	* @version V1.0  
	* @param bitmap
	 */
	private void setPictureAdjustToView(Bitmap bitmap) {
//		int width = getScreenWidth();
//		int picWidth = bitmap.getWidth();
//		int picHeight = bitmap.getHeight();
//		int height = (int) (width * 1.0 / picWidth * picHeight);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
//				height);
//		iv.setLayoutParams(params);
		iv.setImageBitmap(bitmap);
	}

	/**
	 * 
	* @Description: 获取屏幕宽度
	* @author hogachen   
	* @date 2014年12月18日 下午7:39:14 
	* @version V1.0  
	* @return
	 */
	private int getScreenWidth() {
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}

	/**
	 * 
	* @Description: 从文本控件中获取数据生成beautyDetail
	* @author hogachen   
	* @date 2014年12月18日 下午9:16:13 
	* @version V1.0  
	* @return
	 */
	private BeautyDetail getDataFromEditText(){
		BeautyDetail beauty= new BeautyDetail();
//		String  school=edt_beauty_info_school.getText().toString();
//		String  birthday=edt_beauty_info_birthday.getText().toString();
//		String  constellation=edt_beauty_info_constellation.getText().toString();
//		String  description = edt_beauty_info_description.getText().toString();
		Date createTime=new Date();
//		beauty.setBirthday(birthday);
//		beauty.setConstellation(constellation);
//		beauty.setDescription(description);
//		beauty.setSchool(school);
		beauty.setCreateTime(createTime);
		beauty.setPrivilege(privilege);
		UserLoginPreference localfile=UserLoginPreference.getInstance(this);
		String userPhoneNumber = localfile .getuserPhoneNumber();
		beauty.setUserPhoneNumber(userPhoneNumber);
		beauty.setBirthday(birthday);
		beauty.setDescription(description);
		beauty.setSchool(school);
		beauty.setLocationText(locationText);
		if(description!=null&&!description.equals("")){
			beauty.setTrueName(truename);
		}else{
			return null;
		}
		
		filename = userPhoneNumber+DateFormatTools.data2String(createTime);
		beauty.setAvatarPath(filename+".jpg");
		beauty.setLat(myLocation.getLat());
		beauty.setLng(myLocation.getLng());
		return beauty;
	}
	/**
	 * 
	* @Description: 将新创建beauty 上传到服务器
	* @author hogachen   
	* @date 2014年12月18日 下午7:40:13 
	* @version V1.0  
	* @param beautyInfo
	* @return
	 */
	private void uploadBeautyInfo(BeautyDetail beautyInfo){
		Message msg = new Message();
		/**
		 * 这里需要先发一个包跟服务器打交道，如果服务器没反应，马上timeout ，不要等30秒
		 */
		
		
		if(beautyInfo==null){
			msg.what=-3;//表示没有选择图片
			submitHandler.sendMessage(msg);
			return;
		}
		if(tempfile == null ){
			msg.what=-2;//表示没有选择图片
			submitHandler.sendMessage(msg);
			return;
		}
		int testCode = HttpUploadMethods.TestIfUploadSuccess();
		if(testCode!=ConstantValue.operateSuccess){
			msg.what=-5;//表示没有选择图片
			submitHandler.sendMessage(msg);
			return;
		}
		File thumbnailfile = new File(Environment.getExternalStorageDirectory()
				+ "/atest/tempThumbnailFile.jpg" );
		BitmapTools.compressBmpToFile(tempfile,thumbnailfile);
		
		int code = HttpUploadMethods.UploadBeautyInfoPost
		(tempfile, beautyInfo.getAvatarPath(), thumbnailfile, filename+"thumb.jpg", beautyInfo);
		
		msg.what=code;
		submitHandler.sendMessage(msg);

	}

	//下面是高德地图的定位方法
	@Override
	public void onLocationChanged(android.location.Location location) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation!=null&&amapLocation.getAMapException().getErrorCode() == 0) {
			// 定位成功回调信息，设置相关消息
			
			this.myLocation.setLat(amapLocation.getLatitude());
			this.myLocation.setLng(amapLocation.getLongitude());
			this.myLocation.setLocationMessage(amapLocation.getAddress());
			locationText=amapLocation.getAddress();
			this.textview_location.setText(locationText);
			
		}else{
			this.textview_location.setText("定位失败");
		}
	}
	
}
