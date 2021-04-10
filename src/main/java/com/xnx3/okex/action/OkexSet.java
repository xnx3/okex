package com.xnx3.okex.action;

import java.util.ArrayList;
import java.util.List;
import com.xnx3.FileUtil;
import com.xnx3.json.JSONUtil;
import com.xnx3.okex.Global;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Okex 的key等配置的读取、保存等
 * @author 管雷鸣
 *
 */
public class OkexSet {
	/**
	 * 保存配置信息到本地
	 */
	public static void save(String apiKey, String secretKey, String passphrase){
		JSONObject json = new JSONObject();
		json.put("apiKey", apiKey);
		json.put("secretKey", secretKey);
		json.put("passphrase", passphrase);
		
		FileUtil.write(Global.CONFIG_PATH+"okex.config", json.toString());
	}
	
	/**
	 * 读取，加载okex信息
	 */
	public static void load(){
		String content = FileUtil.read(Global.CONFIG_PATH+"okex.config");
		if(content == null || content.length() == 0){
			//第一次用，还没有配置文件
		}else{
			//将文件信息转化为json
			try {
				JSONObject json = JSONObject.fromObject(content);
				System.out.println(json);
				Global.OK_ACCESS_KEY = json.getString("apiKey");
				Global.OK_ACCESS_SECRET_KEY = json.getString("secretKey");
				Global.OK_ACCESS_PASSPHRASE = json.getString("passphrase");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
