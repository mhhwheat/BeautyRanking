package org.wheat.ranking.activity;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import org.wheat.beautyranking.R;
import org.wheat.ranking.loader.HttpDeleteMethods;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.HttpUpdateMethods;
import org.wheat.ranking.loader.LoginAndRegister;
import org.wheat.ranking.loader.HttpUploadMethods;
import org.wheat.ranking.entity.BeautyDetail;
import org.wheat.ranking.entity.BeautyIntroductionList;
import org.wheat.ranking.entity.Comment;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.PhotoList;
import org.wheat.ranking.entity.Praise;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.httptools.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
//test
public class ImageActivityTest extends Activity
{
	private ImageView image;
	private Button bt;
	private Handler handler;
	private static final String FILENAME = "temp_file.txt";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atext);
		image=(ImageView)findViewById(R.id.testImage);
		bt=(Button)findViewById(R.id.showImage);
		handler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				if(msg.obj!=null)
//					image.setImageBitmap((Bitmap)msg.obj);
					System.out.println(msg.obj);
				else
					System.out.println("bmp is null----->");
			}
			
		};
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				System.out.println("in click");
//				try {
//					UploadPhoto.PostPhoto();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				new Thread()
				{

					@Override
					public void run() 
					{
//						Bitmap bmp=null;
//						try {
//							bmp=HttpDataLoader.downLoadBitmap( "1.jpg",-1,-1);
////							UploadPhoto.PostPhoto();
//						} catch (Throwable e) {
//							e.printStackTrace();
//							System.out.println("exception------->");
//						}
//						Message msg=Message.obtain();
//						msg.obj=bmp;
//						handler.sendMessage(msg);
						int result=-1;
						try {
//							Toast.makeText(ImageActivityTest.this, "post success", Toast.LENGTH_LONG);
							//******result=UploadPhotoHttpclientClient.PostPhoto("1.jpg","newname");
//							Praise praise = new Praise();
//							praise.setPhotoId("phoneId");
//							praise.setPraiseTime("praiseTime");
//							praise.setUserPhoneNumber("userPhoneNumber");
//							String beautyId="hogachen";
//							String usernumber="userPhoneNumber";
//							HttpUploadTools.getMycreatePage(usernumber);
							
							
//							String url=ConstantValue.HttpRoot+"UploadBeautyInfo";
//							Praise beautyInfo= new Praise();
//							beautyInfo.setBeautyId(1);
//							beautyInfo.setUserPhoneNumber("13325412547");
//							beautyInfo.setPraiseTime(new Date());
//							
//							beautyInfo.setPhotoId(2);
//				
//							File originFile = new File(Environment.getExternalStorageDirectory(),"1.jpg");
//							File thumbnail= new File(Environment.getExternalStorageDirectory(),"1.apk");
//							int statusCode =HttpUploadMethods.UploadPraisePost(beautyInfo);
//							if(statusCode==ConstantValue.InsertDbSuccess)
//								System.out.println("����ɹ���");
//							else 
//								System.out.println("����ʧ��!");	
//							System.out.println("�����룺"+statusCode);
							
							
//							String beautyId  = "18825162413";
//							double lat=0.0;
//							double lng = 0.0;
//							BeautyIntroductionListJson list = HttpLoderMethods.getMyNeighourPage(lat,lng);
//							if(list==null){
//								System.out.println("���ӽ���ʧ��");
//							}else if(list.getCode()==ConstantValue.getDataSuccess){
//								System.out.println("���ݻ�ȡ�ɹ�");
//								
//							}else System.out.println("�������ӳɹ������ǻ�ȡ����ʧ�ܣ�ʧ�ܴ��룺"+list.getCode());
					
//							int beautyId=4;
//							String userPhoneNumber="18825162413";
//							for(int i=0;i<1;i++){
//								int code = HttpDeleteMethods.deletePhoto(beautyId);
//								System.out.println("������Ϊ��"+code);
//							}
							
							LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
							
//						     Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						     System.out.println("latitude ");
						     LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
						     Location location = mgr.getLastKnownLocation(bundle.getString("provider"));  
						     while(location  == null)  
						     {  
						       mgr.requestLocationUpdates("gps", 60000, 1, locationListener);  
						     } 

						     double latitude = location.getLatitude();     //����   
						     double longitude = location.getLongitude(); //γ��   
						     double altitude =  location.getAltitude();     //����  
						     Log.v("tag", "latitude " + latitude + "  longitude:" + longitude + " altitude:" + altitude);
						     System.out.println("latitude " + latitude + "  longitude:" + longitude + " altitude:" + altitude);
							
//							String url=ConstantValue.HttpRoot+"UploadBeautyInfo";
//							BeautyDetail beautyInfo= new BeautyDetail();
////							beautyInfo.setBeautyId(100);
//							beautyInfo.setUserPhoneNumber("18825162413");
//							beautyInfo.setConstellation("��ţ��");
//							beautyInfo.setDescription("�����Ŀ��羰���ֵķ羰");
//							beautyInfo.setCreateTime(new Date());
//							File originFile = new File(Environment.getExternalStorageDirectory(),"1.jpg");
//							File thumbnail= new File(Environment.getExternalStorageDirectory(),"1.jpg");
//							
//							int code = HttpUploadMethods.UploadBeautyInfoPost(originFile, "originFileName.jpg", 
//									thumbnail, "thumbnailName.apk", beautyInfo);
//				
//							File originFile = new File(Environment.getExternalStorageDirectory(),"1.jpg");
//							File thumbnail= new File(Environment.getExternalStorageDirectory(),"1.apk");
//							int statusCode =HttpUpdateMethods.updateBeautyInfo(beautyInfo);
//							if(statusCode==ConstantValue.InsertDbSuccess)
//								System.out.println("���³ɹ���");
//							else 
//								System.out.println("����ʧ��!");	
//							System.out.println("�����룺"+statusCode);
							
							
							
//							Comment comment = new Comment();
//							comment.setPhotoID("photoID");
//							comment.setCommentTime("commentTime");
//							comment.setUserPhoneNumber("userPhoneNumber");
//							comment.setCommentContent("commentContent");
//							oneBeauty.setTrueName(request.getParameter("beautyName"));
					
//							
//							HttpUploadTools.uploadComment(comment);
//							UploadPhotoUrlConnectionClient.uploadFile("1.jpg");
							
//							Toast.makeText(ImageActivityTest.this, "post success", Toast.LENGTH_LONG);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg=Message.obtain();
						msg.obj=result;
						handler.sendMessage(msg);
					}
					
				}.start();
			}
		});
	}
	
}
