package org.wheat.ranking.testActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.wheat.beautyranking.R;
import org.wheat.ranking.data.UserLoginPreference;
import org.wheat.ranking.entity.BeautyDetail;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.Location;
import org.wheat.ranking.httptools.BitmapTools;
import org.wheat.ranking.httptools.DateFormatTools;
import org.wheat.ranking.loader.HttpUploadMethods;
import org.wheat.ranking.location.NetLocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
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

public class PicCutDemoActivity extends Activity implements OnClickListener {

	private ImageView iv = null;
	private EditText edt_beauty_info_school=null;
	private EditText edt_beauty_info_admission=null;
	private EditText edt_beauty_info_birthday=null;//ʵ���ϸ�Ϊ΢�ź�
	private TextView textview_location=null;//ʵ�����ǵ�����Ϣ
	private EditText edt_beauty_info_constellation=null;
	private EditText edt_beauty_info_description=null;
	private Button btn_submit=null;
	private String tp = null;

	private String photoName = null;
	private Location location =null;

	
	
	File tempfile =null;
	//���ɵ�ͼƬ���ļ�����Ϊ�˷��㣬����һ����������
	String filename=null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_new_beauty);
		// ��ʼ��
		init();
	}

	/**
	 * ��ʼ������ʵ��
	 */
	private void init() {
		iv = (ImageView) findViewById(R.id.imageView1);
		edt_beauty_info_school=(EditText)findViewById(R.id.beauty_info_school);
		edt_beauty_info_admission=(EditText)findViewById(R.id.beauty_info_admission);
		edt_beauty_info_birthday=(EditText)findViewById(R.id.beauty_info_birthday);
		edt_beauty_info_constellation=(EditText)findViewById(R.id.beauty_info_constellation);
		edt_beauty_info_description=(EditText)findViewById(R.id.beauty_info_description);
		textview_location=(TextView)findViewById(R.id.textview_location);
		btn_submit= (Button)findViewById(R.id.submit);
		iv.setOnClickListener(this);
		threadToGetLocation();
	}
	/**
	 * 
	* @Description: ���� һ���߳�ȥ��ȡ����λ��
	* @author hogachen   
	* @date 2014��12��18�� ����8:47:23 
	* @version V1.0
	 */
	private void threadToGetLocation(){
		Intent intent = new Intent();
		intent.setClass(PicCutDemoActivity.this, NetLocation.class);
		startActivityForResult(intent,0);  
	}
	/**
	 * �ؼ�����¼�ʵ��
	 * 
	 * ��Ϊ�������ʲ�ͬ�ؼ��ı���ͼ�ü���ôʵ�֣� �Ҿ�������ط����������ؼ���ֻΪ���Լ���¼ѧϰ ��Ҿ���û�õĿ���������
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.imageView1:
			ShowPickDialog();
			break;
		case R.id.submit:
			BeautyDetail beauty=getDataFromEditText();
			uploadBeautyInfo(beauty);
		default:
			break;
		}
	}

	/**
	 * ѡ����ʾ�Ի���
	 */
	private void ShowPickDialog() {
		new AlertDialog.Builder(this)
				.setTitle("��ȡ��Ƭ")
				.setNegativeButton("���", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_PICK, null);

						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 1);

					}
				})
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();

						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);

						// ������Ҫ�ֶ���ȡͼƬ������Ҫ����Ƭ�����ƴ洢��ȫ�ֱ�����
						SimpleDateFormat timeStampFormat = new SimpleDateFormat(
								"yyyy_MM_dd_HH_mm_ss");
						photoName = timeStampFormat.format(new Date()) + ".jpg";

						// �������ָ������������պ����Ƭ�洢��·��
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										photoName)));
						startActivityForResult(intent, 2);
					}
				}).show();
	}

	/**
	 * @description ��ȡ��Ƭ���ͼƬ����ص�����
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		//��ȡ����λ����Ϣ
		case 0:
			Bundle bundle =data.getExtras(); 
			if(bundle!=null){
				 this.location.setLat(bundle.getDouble("lat"));
		         this.location.setLng(bundle.getDouble("lng"));
		         this.location.setLocationMessage(bundle.getString("message"));
		         textview_location.setText(this.location.getLocationMessage());
			}	
		// �����ֱ�Ӵ�����ȡ	
		case 1:
			if (data != null) {
				setPicToView(data.getData());
				tempfile = new File(data.getData().getPath());
				System.out.println("tempfile.getAbsolutePath()  "+tempfile.getAbsolutePath());
			}

			break;
		// ����ǵ����������ʱ
		case 2:

			if (data != null && data.getData() != null) {
				setPicToView(data.getData());
			}
			if (data == null) {
				tempfile = new File(Environment.getExternalStorageDirectory()
						+ "/" + photoName);
				setPicToView(Uri.fromFile(tempfile));
			}
			break;
		// ȡ�òü����ͼƬ
		case 3:
			if (data != null) {
				setPicToView(data.getData());
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ����ü�֮���ͼƬ����
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
			 * ����ע�͵ķ����ǽ��ü�֮���ͼƬ��Base64Coder���ַ���ʽ�� ������������QQͷ���ϴ����õķ������������
			 */

			/*
			 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 * photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); byte[] b
			 * = stream.toByteArray(); // ��ͼƬ�����ַ�����ʽ�洢����
			 * 
			 * tp = new String(Base64Coder.encodeLines(b));
			 * ����ط���ҿ���д�¸��������ϴ�ͼƬ��ʵ�֣�ֱ�Ӱ�tpֱ���ϴ��Ϳ����ˣ� ����������ķ����Ƿ������Ǳߵ����ˣ����
			 * 
			 * ������ص��ķ����������ݻ�����Base64Coder����ʽ�Ļ������������·�ʽת�� Ϊ���ǿ����õ�ͼƬ���;�OK��...���
			 * Bitmap dBitmap = BitmapFactory.decodeFile(tp); Drawable drawable
			 * = new BitmapDrawable(dBitmap);
			 */
			setPictureAdjustToView(bitmap2);
		}
	}

	/**
	 * 
	* @Description: TODO ����imageview ����ͼƬ
	* @author hogachen   
	* @date 2014��12��18�� ����7:37:58 
	* @version V1.0  
	* @param bitmap
	 */
	private void setPictureAdjustToView(Bitmap bitmap) {
		int width = getScreenWidth();
		int picWidth = bitmap.getWidth();
		int picHeight = bitmap.getHeight();
		int height = (int) (width * 1.0 / picWidth * picHeight);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
				height);
		iv.setLayoutParams(params);
		iv.setImageBitmap(bitmap);
	}

	/**
	 * 
	* @Description: ��ȡ��Ļ���
	* @author hogachen   
	* @date 2014��12��18�� ����7:39:14 
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
	* @Description: ���ı��ؼ��л�ȡ��������beautyDetail
	* @author hogachen   
	* @date 2014��12��18�� ����9:16:13 
	* @version V1.0  
	* @return
	 */
	private BeautyDetail getDataFromEditText(){
		BeautyDetail beauty= new BeautyDetail();
		String  school=edt_beauty_info_school.getText().toString();
		String  birthday=edt_beauty_info_birthday.getText().toString();
		String  constellation=edt_beauty_info_constellation.getText().toString();
		String  description = edt_beauty_info_description.getText().toString();
		Date createTime=new Date();
		beauty.setBirthday(birthday);
		beauty.setConstellation(constellation);
		beauty.setDescription(description);
		beauty.setSchool(school);
		beauty.setCreateTime(createTime);
		
		UserLoginPreference localfile=UserLoginPreference.getInstance(this);
		String userPhoneNumber = localfile .getuserPhoneNumber();
		beauty.setUserPhoneNumber(userPhoneNumber);
		filename = userPhoneNumber+DateFormatTools.data2String(createTime);
		beauty.setAvatarPath(filename+".jpg");
		beauty.setLat(location.getLat());
		beauty.setLng(location.getLng());
		return beauty;
	}
	/**
	 * 
	* @Description: ���´���beauty �ϴ���������
	* @author hogachen   
	* @date 2014��12��18�� ����7:40:13 
	* @version V1.0  
	* @param beautyInfo
	* @return
	 */
	private void uploadBeautyInfo(BeautyDetail beautyInfo){
		File thumbnailfile = new File(Environment.getExternalStorageDirectory()
				+ "/tempThumbnailFile" );
		BitmapTools.compressBmpToFile(tempfile,thumbnailfile);
		int code = HttpUploadMethods.UploadBeautyInfoPost
		(tempfile, beautyInfo.getAvatarPath(), thumbnailfile, filename+"thumb.jpg", beautyInfo);
		if(code == ConstantValue.operateSuccess){
			Toast.makeText(getApplicationContext(), "�����ɹ�",
				     Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(), "����ʧ��,�������Ϊ:"+code,
				     Toast.LENGTH_SHORT).show();
		}

	}
	
}
