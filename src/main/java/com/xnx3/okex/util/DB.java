package com.xnx3.okex.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xnx3.swing.DialogUtil;

/**
 * 数据库、数据的一些操作
 * @author 管雷鸣
 *
 */
public class DB {
	private static SqliteUtil dbUtil;
	static{
		try {
			dbUtil = new SqliteUtil();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取 db对象，操作数据库
	 * @return
	 */
	public static synchronized SqliteUtil getDatabase(){
		return dbUtil;
	}
	
	public static void closeDB(){
		dbUtil.closeConn();
		java.lang.System.out.println("clouse db conn");
	}
	
	public static void main(String[] args) {
		
	}
	
	
	//缓存当前system表数据
	private static System system;
	/**
	 * 获取所有的system表数据，通过缓存。如果缓存中没有，则从数据库读取，存入缓存
	 * @return map key:system.name
	 */
	public static System getSystemByCache(){
		if(system == null){
			refreshSystemForCache();
		}
		return system;
	}
	/**
	 * 刷新缓存中的system表数据，重新缓存
	 */
	public static void refreshSystemForCache(){
		ResultSet rs = dbUtil.select("SELECT * FROM system");
		ArrayList<System> list = dbUtil.resultToEntity(rs, System.class);
		if(list.size() > 0){
			system = list.get(0);
		}else{
			DialogUtil.showMessageDialog("非正常，system表无数据！请填充");
		}
	}
	
	/**
	 * sql count 统计
	 * @param where 传入查询条件。格式如： WHERE year = 2019 AND month = 2
	 * @return count 统计出来的条数。若是返回-1，则是出错
	 */
	public static int count(String tableName, String where){
		ResultSet rs = dbUtil.select("SELECT count(*) AS c FROM "+tableName+" "+where);
		try {
			if (rs.next()) {
				return rs.getInt("c");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}
	
	
}
