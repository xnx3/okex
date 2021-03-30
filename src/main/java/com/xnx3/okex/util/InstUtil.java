package com.xnx3.okex.util;

/**
 * 购买的币的名字，相关的工具类
 * @author 管雷鸣
 *
 */
public class InstUtil {
	
	/**
	 * 获取币价格的单位，如 PMA-USDK 返回的是USDK
	 * @param instId 传入如 如 PMA-USDK
	 */
	public static String getPriceName(String instId){
		if(instId == null || instId.indexOf("-") == -1){
			return "";
		}
		return instId.split("-")[1];
	}
	
}
