package org.wheat.ranking.loader;

import java.io.IOException;
import java.util.HashMap;

import org.wheat.ranking.entity.ConstantValue;

public class HttpUpdateTools {

	public int updateBeautyInfo(HashMap<String ,String> values){
		if(values == null)return -1;
		int resultCode =-1;
		try {
			resultCode = HttpLoader.getCode(ConstantValue.HttpRoot+"UpdateBeautyInfo", values, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultCode;
		
	}
}
