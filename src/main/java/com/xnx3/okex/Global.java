package com.xnx3.okex;

import java.io.File;

import com.xnx3.okex.ui.JiHuaWeiTuoJframe;
import com.xnx3.okex.util.SystemUtil;

/**
 * 全局
 * @author 管雷鸣
 *
 */
public class Global {
	public static final String CONFIG_PATH;	//配置文件所在的文件夹
	static{
		//判断当前系统是否是mac
		if(SystemUtil.isMacOS()){
			//是mac
			CONFIG_PATH = System.getProperty("user.home")+File.separator+"okex"+File.separator+"config"+File.separator;	//配置文件所在的文件夹
		}else {
			CONFIG_PATH = SystemUtil.getCurrentDir()+File.separator+"config"+File.separator;	//配置文件所在的文件夹
		}
		//如果配置文件目录不存在，那么自动创建
		File file = new File(CONFIG_PATH);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	
	//OKEX接口的域名
	public static String OKEX_DOMAIN = "https://www.okex.win";
	
	//当前项目路径
	public static final String PATH = SystemUtil.getCurrentDir();	
	
	//从 okex.config 自动加载
	public static String OK_ACCESS_KEY = null;
	public static String OK_ACCESS_PASSPHRASE = null;
	public static String OK_ACCESS_SECRET_KEY = null;
	
	
	/***** 界面 ******/
	public static JiHuaWeiTuoJframe jihuaweituoJframe;
	static{
		jihuaweituoJframe = new JiHuaWeiTuoJframe();
	}
	
	
	//当前的版本
	public static final String VERSION = "1.0";
	//版本更新url
	public static final String VERSION_CHECK_URL = "http://version.xnx3.com/yunbackups.html";
}
