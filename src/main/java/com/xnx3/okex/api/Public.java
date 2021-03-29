package com.xnx3.okex.api;


/**
 * 公共
 * @author 管雷鸣
 *
 */
public class Public {
	
	public static void main(String[] args) {
		System.out.println(time());
	}
	
	/**
	 * 获取okex的系统时间戳，13位
	 * @return 13位时间戳，是okex系统的，不是自己本地的
	 */
	public static long time() {
		return com.xnx3.okex.util.HttpsUtil.get("/api/v5/public/time").getJSONArray("data").getJSONObject(0).getLong("ts");
	}
	
}
