package org.wheat.ranking.httptools;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import org.wheat.ranking.coders.Aes;

import android.net.Uri;
import android.util.Base64;

public class EncryptUrlParamsTools {

	/*
	 * 密钥
	 */
	private static final byte[] PSW = "AjGfpbbQmU7EAnkJ".getBytes();
	
	public  static String encryptParamsToGet(HashMap<String, String> params) throws Throwable {
        byte[] buff = encryptParamsToPost(params);
        if (buff == null) {
            return null;
        }
        return Base64.encodeToString(buff, Base64.DEFAULT);
    }
	
	/**
     * 加密参数，并将向量置于密文前头。
     *
     * @param params
     * @return
     * @throws Throwable
     */
	public static byte[] encryptParamsToPost(HashMap<String, String> params) throws Throwable
	{
		if(params==null)
		{
			return null;
		}
		StringBuilder sb=new StringBuilder();
		Iterator<Entry<String,String>> iter=params.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String,String> entry=iter.next();
			String key=entry.getKey();
			String value=entry.getValue();
			if(sb.length()>0)
			{
				sb.append('&');
			}
			sb.append(key).append('=').append(Uri.encode(value));
		}
		
		byte[] buff=sb.toString().getBytes("UTF-8");
		byte[] iv=new byte[16];
		Random rd=new Random();
		//填充16位的字节数组
		rd.nextBytes(iv);
		byte[] data=Aes.encrypt(buff, PSW, iv);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		baos.write(iv,0,iv.length);
		baos.write(data,0,data.length);
		
		byte[] out=baos.toByteArray();
		try{
			baos.close();
		}catch(Throwable e){
			
		}
		return out;
	}
}
