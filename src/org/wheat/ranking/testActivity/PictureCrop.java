package org.wheat.ranking.testActivity;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;



import org.wheat.beautyranking.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PictureCrop extends Activity implements OnClickListener {

//	private ImageButton ib = null;
	private ImageView iv = null;
//	private Button btn = null;
	private String tp = null;

	private String photoName = null;

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
//		ib = (ImageButton) findViewById(R.id.imageButton1);
		iv = (ImageView) findViewById(R.id.imageView1);
//		btn = (Button) findViewById(R.id.button1);
//		ib.setOnClickListener(this);
		iv.setOnClickListener(this);
//		btn.setOnClickListener(this);
	}

	/**
	 * �ؼ�����¼�ʵ��
	 * 
	 * ��Ϊ�������ʲ�ͬ�ؼ��ı���ͼ�ü���ôʵ�֣� �Ҿ�������ط����������ؼ���ֻΪ���Լ���¼ѧϰ ��Ҿ���û�õĿ���������
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.imageButton1:
//			ShowPickDialog();
//			break;
		case R.id.imageView1:
			ShowPickDialog();
			break;
//		case R.id.button1:
//			ShowPickDialog();
//			break;

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
						/**
						 * �տ�ʼ�����Լ�Ҳ��֪��ACTION_PICK�Ǹ���ģ�����ֱ�ӿ�IntentԴ�룬
						 * ���Է�������ܶණ����Intent�Ǹ���ǿ��Ķ��������һ����ϸ�Ķ���
						 */
						Intent intent = new Intent(Intent.ACTION_PICK, null);

						/**
						 * ������仰����������ʽд��һ����Ч���������
						 * intent.setData(MediaStore.Images
						 * .Media.EXTERNAL_CONTENT_URI);
						 * intent.setType(""image/*");������������
						 * ���������Ҫ�����ϴ�����������ͼƬ����ʱ����ֱ��д��
						 * ��"image/jpeg �� image/png�ȵ�����"
						 * ����ط�С���и����ʣ�ϣ�����ֽ���£������������URI������ΪʲôҪ��������ʽ��дѽ����ʲô����
						 */
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 1);

					}
				})
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						/**
						 * ������仹�������ӣ����ÿ������չ��ܣ�����Ϊʲô�п������գ���ҿ��Բο����¹ٷ�
						 * �ĵ���you_sdk_path/docs/guide/topics/media/camera.html
						 * �Ҹտ���ʱ����Ϊ̫�������濴����ʵ�Ǵ�ģ�����������õ�̫���ˣ����Դ�Ҳ�Ҫ��Ϊ
						 * �ٷ��ĵ�̫���˾Ͳ����ˣ���ʵ�Ǵ�ģ�����ط�С��Ҳ���ˣ��������
						 */
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// �����ֱ�Ӵ�����ȡ
		case 1:
			if (data != null)
				startPhotoZoom(data.getData());
			break;
		// ����ǵ����������ʱ
		case 2:

			if (data != null && data.getData() != null) {
				Uri uri = data.getData();
				startPhotoZoom(uri);
			}

			// һЩ�����޷���getData�л�ȡuri�������ֶ�ָ�����պ�洢��Ƭ��Uri
			if (data == null) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/" + photoName);
				startPhotoZoom(Uri.fromFile(temp));
			}
			break;
		// ȡ�òü����ͼƬ
		case 3:
			/**
			 * �ǿ��жϴ��һ��Ҫ��֤���������֤�Ļ��� �ڼ���֮��������ֲ����⣬Ҫ���²ü�������
			 * ��ǰ����ʱ���ᱨNullException��С��ֻ ������ط����£���ҿ��Ը��ݲ�ͬ����ں��ʵ� �ط����жϴ����������
			 * 
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * �����������Intent��ACTION����ô֪���ģ���ҿ��Կ����Լ�·���µ�������ҳ
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * ֱ��������Ctrl+F�ѣ�CROP ��֮ǰС��û��ϸ��������ʵ��׿ϵͳ���Ѿ����Դ�ͼƬ�ü�����, ��ֱ�ӵ����ؿ�ģ�С����C C++
		 * ���������ϸ�˽�ȥ�ˣ������Ӿ������ӣ������о���������ô ��������...���
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 2);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		// ����ȡ����ͼƬ����ϵͳ��intent��������ͼ����
		startActivityForResult(intent, 3);
	}

	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);

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
//			ib.setBackgroundDrawable(drawable);
			iv.setBackgroundDrawable(drawable);
		}
	}

}