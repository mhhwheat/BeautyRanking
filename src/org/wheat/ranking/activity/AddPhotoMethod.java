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
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AddPhotoMethod extends Activity implements OnClickListener,
		AMapLocationListener {

	/**
	 * �ߵµ�ͼ��λ��Ϣ
	 */
	private LocationManagerProxy mLocationManagerProxy;
	private MyLocation myLocation = new MyLocation();;

	private LinearLayout tvPersonInfo;
	private int privilege = 2;
	private ImageView iv = null;
	String truename;
	String birthday;
	String school;
	String description;
	String locationText;
	// private EditText edt_beauty_info_school=null;
	// private EditText edt_beauty_info_admission=null;
	// private EditText edt_beauty_info_birthday=null;//ʵ���ϸ�Ϊ΢�ź�
	private TextView textview_location = null;// ʵ�����ǵ�����Ϣ
	// private EditText edt_beauty_info_constellation=null;
	// private EditText edt_beauty_info_description=null;
	private Button btn_submit = null;
	private Button btn_cancel;

	private String photoName = null;

	File tempfile = null;
	// ���ɵ�ͼƬ���ļ�����Ϊ�˷��㣬����һ����������
	String filename = null;
	/** Called when the activity is first created. */

	Handler submitHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// �������ñ�����
		setContentView(R.layout.add_beauty_photo);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.add_photo_title);
//		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		int width = (int) (getScreenWidth()*0.9);
		int height = (int) (getScreenHeight()*0.75);
//        windowParams.width = width; // ���ÿ��
//        windowParams.height = height; // ���ø߶�
        getWindow().setLayout(width, height);
//        getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//        getWindow().setAttributes(windowParams);
		// ��ʼ��
		init();
		// ���������ʱ��Ͷ�λ
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 150, this);
		// toast handler
		submitHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == ConstantValue.operateSuccess) {
					Toast.makeText(getApplicationContext(), "�����ɹ�",
							Toast.LENGTH_SHORT).show();
				} else if (msg.what == -3) {
					Toast.makeText(getApplicationContext(), "����û��д������Ϣ�أ�",
							Toast.LENGTH_SHORT).show();
				} else if (msg.what == -2) {
					Toast.makeText(getApplicationContext(), "����û��ΪTaѡ��������",
							Toast.LENGTH_SHORT).show();
				} else if (msg.what == -5) {
					Toast.makeText(getApplicationContext(), "��������������",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"����ʧ�ܣ��������Ϊ��" + msg.what, Toast.LENGTH_SHORT)
							.show();
				}
			}
		};
	}

	/**
	 * ��ʼ������ʵ��
	 */
	private void init() {
		iv = (ImageView) findViewById(R.id.add_photo_img);
		textview_location = (TextView) findViewById(R.id.add_photo_location);
		btn_submit = (Button) findViewById(R.id.add_photo_submit);
		btn_cancel = (Button) findViewById(R.id.add_photo_cancel);
		iv.setOnClickListener(this);
		btn_submit.setOnClickListener(submitListener);
		btn_cancel.setOnClickListener(cancelListener);
	}

	OnClickListener cancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	OnClickListener submitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Thread submitThread = new Thread() {
				public void run() {
					BeautyDetail beauty = getDataFromEditText();
					uploadBeautyInfo(beauty);
					// finish();
				}
			};
			// TODO Auto-generated method stub
			submitThread.start();
		}
	};

	/**
	 * �ؼ�����¼�ʵ��
	 * 
	 * ��Ϊ�������ʲ�ͬ�ؼ��ı���ͼ�ü���ôʵ�֣� �Ҿ�������ط����������ؼ���ֻΪ���Լ���¼ѧϰ ��Ҿ���û�õĿ���������
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.add_photo_img:
			ShowPickDialog();
			break;
		case R.id.add_photo_submit:
			break;
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
		// ��ȡ����λ����Ϣ
		case 0:
			if (data != null && data.getExtras() != null) {

				Bundle bundle = data.getExtras();
				this.myLocation.setLat(bundle.getDouble("lat"));
				this.myLocation.setLng(bundle.getDouble("lng"));
				this.myLocation.setLocationMessage(bundle.getString("message"));
				textview_location.setText(this.myLocation.getLocationMessage());
				System.out.println(this.myLocation.getLocationMessage());
			}
			break;
		// �����ֱ�Ӵ�����ȡ
		case 1:
			if (data != null) {
				setPicToView(data.getData());
				String photoPath = getUriPath(data.getData());
				tempfile = new File(photoPath);
				System.out.println("tempfile.getAbsolutePath()  "
						+ tempfile.getAbsolutePath());
				System.out.println("photoPath  " + photoPath);
			}

			break;
		// ����ǵ����������ʱ
		case 2:

			if (data != null && data.getData() != null) {
				setPicToView(data.getData());
				String photoPath = getUriPath(data.getData());
				tempfile = new File(photoPath);
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
		case 5:
			if (data != null) {
				truename = data.getStringExtra("truename");
				birthday = data.getStringExtra("birthday");
				school = data.getStringExtra("school");
				description = data.getStringExtra("description");
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getUriPath(Uri originalUri) {

		// ���￪ʼ�ĵڶ����֣���ȡͼƬ��·����
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(originalUri, proj, null, null, null);
		// ���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		// ����������ֵ��ȡͼƬ·��
		String path = cursor.getString(column_index);
		return path;
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
		iv.setImageBitmap(bitmap);
	}



	/**
	 * 
	 * @Description: ���ı��ؼ��л�ȡ��������beautyDetail
	 * @author hogachen
	 * @date 2014��12��18�� ����9:16:13
	 * @version V1.0
	 * @return
	 */
	private BeautyDetail getDataFromEditText() {
		BeautyDetail beauty = new BeautyDetail();
		// String school=edt_beauty_info_school.getText().toString();
		// String birthday=edt_beauty_info_birthday.getText().toString();
		// String
		// constellation=edt_beauty_info_constellation.getText().toString();
		// String description =
		// edt_beauty_info_description.getText().toString();
		Date createTime = new Date();
		// beauty.setBirthday(birthday);
		// beauty.setConstellation(constellation);
		// beauty.setDescription(description);
		// beauty.setSchool(school);
		beauty.setCreateTime(createTime);
		beauty.setPrivilege(privilege);
		UserLoginPreference localfile = UserLoginPreference.getInstance(this);
		String userPhoneNumber = localfile.getuserPhoneNumber();
		beauty.setUserPhoneNumber(userPhoneNumber);
		beauty.setBirthday(birthday);
		beauty.setDescription(description);
		beauty.setSchool(school);
		beauty.setLocationText(locationText);
		if (description != null && !description.equals("")) {
			beauty.setTrueName(truename);
		} else {
			return null;
		}

		filename = userPhoneNumber + DateFormatTools.data2String(createTime);
		beauty.setAvatarPath(filename + ".jpg");
		beauty.setLat(myLocation.getLat());
		beauty.setLng(myLocation.getLng());
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
	private void uploadBeautyInfo(BeautyDetail beautyInfo) {
		Message msg = new Message();
		/**
		 * ������Ҫ�ȷ�һ�������������򽻵������������û��Ӧ������timeout ����Ҫ��30��
		 */

		if (beautyInfo == null) {
			msg.what = -3;// ��ʾû��ѡ��ͼƬ
			submitHandler.sendMessage(msg);
			return;
		}
		if (tempfile == null) {
			msg.what = -2;// ��ʾû��ѡ��ͼƬ
			submitHandler.sendMessage(msg);
			return;
		}
		int testCode = HttpUploadMethods.TestIfUploadSuccess();
		if (testCode != ConstantValue.operateSuccess) {
			msg.what = -5;// ��ʾû��ѡ��ͼƬ
			submitHandler.sendMessage(msg);
			return;
		}
		File thumbnailfile = new File(Environment.getExternalStorageDirectory()
				+ "/atest/tempThumbnailFile.jpg");
		BitmapTools.compressBmpToFile(tempfile, thumbnailfile);

		int code = HttpUploadMethods.UploadBeautyInfoPost(tempfile,
				beautyInfo.getAvatarPath(), thumbnailfile, filename
						+ "thumb.jpg", beautyInfo);

		msg.what = code;
		submitHandler.sendMessage(msg);

	}

	// �����Ǹߵµ�ͼ�Ķ�λ����
	@Override
	public void onLocationChanged(android.location.Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// ��λ�ɹ��ص���Ϣ�����������Ϣ

			this.myLocation.setLat(amapLocation.getLatitude());
			this.myLocation.setLng(amapLocation.getLongitude());
			this.myLocation.setLocationMessage(amapLocation.getAddress());
			locationText = amapLocation.getAddress();
			this.textview_location.setText(locationText);

		} else {
			this.textview_location.setText("��λʧ��");
		}
	}
	private int getScreenWidth() {
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}
	private int getScreenHeight() {
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getHeight();
		return width;
	}
}
