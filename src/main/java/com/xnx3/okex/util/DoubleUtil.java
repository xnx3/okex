package com.xnx3.okex.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Double的操作
 * @author 管雷鸣
 *
 */
public class DoubleUtil {
	
	/**
	 * double 保留1位小数，四舍五入
	 * @param d 要四舍五入的原本的Double
	 * @return 保留1位小数的double
	 */
	public static double doubleSplit(double d){
		double dou = d * 10;
		return (Math.round(dou)+0.0)/10;
	}
	
	/**
	 * double 保留几位小数，四舍五入
	 * @param d 要四舍五入的原本的Double
	 * @param jiweixiaoshu 保留几位小数，比如传入2
	 * @return 保留小数的double
	 */
	public static double doubleSplit(double d, int jiweixiaoshu){
		int x = 1;
		for (int i = 0; i < jiweixiaoshu; i++) {
			x = x * 10;
		}
		double dou = d * x;
		return (Math.round(dou)+0.0)/x;
	}
	
	/**
	 * 将 double 转为 String 显示，其实就是为了去掉科学计数法的E
	 * @param value double值
	 * @return String值
	 */
	public static String doubleToString(double value){
		DecimalFormat decimalFormat = new DecimalFormat("###################.################");  
		return decimalFormat.format(value);
	}
	
	public static void main(String[] args) {
		System.out.println(doubleToString(0.0000000000000047));
	}
}
