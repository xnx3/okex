package com.xnx3.okex.api;

import java.util.HashMap;
import java.util.Map;

import com.xnx3.CacheUtil;
import com.xnx3.okex.EyeInstId;
import com.xnx3.okex.bean.Instrument;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 公共
 * @author 管雷鸣
 *
 */
public class Public {
	
	public static void main(String[] args) {
		System.out.println(getInstrument("PMA-BTC"));
	}
	
	/**
	 * 获取okex的系统时间戳，13位
	 * @return 13位时间戳，是okex系统的，不是自己本地的
	 */
	public static long time() {
		return com.xnx3.okex.util.HttpsUtil.get("/api/v5/public/time").getJSONArray("data").getJSONObject(0).getLong("ts");
	}
	
	
	/**
	 * 获取所有交易产品基础信息,从缓存中取，不是从接口
	 */
	public static Map<String, JSONObject> instruments() {
		Map<String, JSONObject> instrumentMap = (Map<String, JSONObject>)CacheUtil.get("public:instruments");
		if(instrumentMap == null){
			//从接口取
			JSONArray array = com.xnx3.okex.util.HttpsUtil.get("/api/v5/public/instruments?instType=SPOT").getJSONArray("data");
			instrumentMap = new HashMap<String, JSONObject>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject json = array.getJSONObject(i);
				instrumentMap.put(json.getString("instId"), json);
			}
			
			CacheUtil.set("public:instruments", instrumentMap, 3600);	//缓存一小时
		}
		return instrumentMap;
	}
	
	
	/**
	 * 获取某个交易产品基础信息，从缓存中取，不是从接口
	 * @param instId 传入如 PMA-USDT
	 * @return
	 */
	public static Instrument getInstrument(String instId){
		JSONObject json = instruments().get(instId);
		return new Instrument(json);
	}
	
	
}
