package org.wheat.ranking.httptools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;
/**
 * @author hogachen
 * @deprecated 使用java自带的urlConnection上传文件
 */
public class UploadPhotoUrlConnectionClient {
	
	
	public static void uploadFile(String FILENAME) {
		String gid="hogachen";
		String actionUrl = "http://192.168.202.236:8080/BeautyRankingServer/UploadPhotoUrlCon"+ "?gid=" + gid;
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);

			con.setRequestMethod("POST");

			con.setRequestProperty("Connection", "Keep-Alive");

			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			File file = new File(Environment.getExternalStorageDirectory(),
					FILENAME);

			FileInputStream fStream = new FileInputStream(file);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			int length = -1;

			while ((length = fStream.read(buffer)) != -1) {

				ds.write(buffer, 0, length);
			}

			fStream.close();
			ds.flush();

			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}

			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}