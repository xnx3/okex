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
	
	/**
	 * 两个double数相加，精确相加，不会产生乱七八糟的一大长串99999
	 * @param d1 相加的第一个double 数
	 * @param d2 相加的第二个double 数
	 * @return 和
	 */
	public static double add(double d1, double d2){
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2).doubleValue();
	}
	
	/**
	 * 两个double数相减，精确相加，不会产生乱七八糟的一大长串99999
	 * @param d1 第一个double
	 * @param d2 第二个double
	 * @return 
	 */
	public static double subtract(double d1, double d2){
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();
	}
	
	public static double lastAddOne(double value){
		String str = doubleToString(value);
		int dian = str.indexOf(".");
		if(dian == -1){
			//没有小数点，那省事了
			
		}
		
		int xiaoshudian_length = str.length() - dian - 1;
		
		StringBuffer sb = new StringBuffer();
		sb.append("0.");
		for (int i = 0; i < xiaoshudian_length - 1; i++) {
			sb.append("0");
		}
		sb.append("1");
		double add = Double.parseDouble(sb.toString());
		
		//加运算
		return add(value, add);
	}
	
	public static void main(String[] args) {
		System.out.println(subtract(0.000023, 0.000023));
	}
}
