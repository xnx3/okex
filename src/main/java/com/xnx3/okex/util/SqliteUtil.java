package com.xnx3.okex.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.xnx3.okex.bean.trade.Order;

/**
 * sqlite util
 * @author nickR
 * @date 2019年5月6日 上午11:40:17
 */
public class SqliteUtil {
	private Connection conn;
	
	/**
	 * init connectionPool
	 * @param poolSize
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public SqliteUtil() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:"+com.xnx3.okex.Global.PATH+File.separator+"res"+File.separator+"ok.db");
		conn.setAutoCommit(false);
	}
	
	/**
	 * construction without poolSize
	 * default size is 5
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static SqliteUtil getInstance() throws ClassNotFoundException, SQLException {
		return new SqliteUtil();
	}
	
	/**
	 * get connection
	 * @return
	 */
	private synchronized Connection getConn() {
		return conn;
	}
	
	/**
	 * release connection
	 * @param c
	 */
	private void releaseConn(Connection c) {
//		connectionPool.addLast(c);
	}
	
	/**
	 * get statement
	 * @param con
	 * @param sql
	 * @return
	 */
	private Statement getStatement(Connection con){
		try {
			return con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * query
	 * @param sql
	 * @return
	 */
	public ResultSet select(String sql){
		Connection c = this.getConn();
		Statement stm = this.getStatement(c);
		ResultSet rs = null;
		try {
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.releaseConn(c);
		}
		
		return rs;
	}
	
	/**
	 * select 查询，这个只是查询单个的数据表，将返回的数据封装成bean
	 * @param cls Bean类，如 {@link Order}.class
	 * @param where 条件，传入如 WHERE id = 1 如果没有条件，那传入空字符串
	 * @return 如果为空，那么size() 为0，不会出现null
	 */
	public <E> List<E> select(Class cls, String where){
		ResultSet rs = DB.getDatabase().select("SELECT * FROM \""+getTableName(cls)+"\" " + where);
		List<E> list = (List<E>) DB.getDatabase().resultToEntity(rs, cls);
		if(list == null){
			list = new ArrayList<E>();
		}
		return list;
	}
	
	/**
	 * 传入sql语句，返回一个值。这个值可以是字符串、也可以是int、double等。
	 * 最初是用来统计用的  SELECT sum(shouxufei) FROM order
	 * @param sql 要执行的sql语句
	 * @return
	 */
	public Object getOneObject(String sql){
		ResultSet rs = DB.getDatabase().select(sql);
		try {
			return rs.getObject(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * insert
	 * @param sql 如 INSERT INTO louhao(name,xiaoquid) VALUES('1号楼',3);
	 * @return 0 则是插入失败； 大于0则是插入成功，返回的插入的自增id
	 */
	public Integer insert(String sql) {
		Connection c = this.getConn();
		Statement ps = this.getStatement(c);
		try {
			int row = ps.executeUpdate(sql);
			if(row == 0){
				return 0;
			}
			
			ResultSet rs = ps.executeQuery("SELECT last_insert_rowid() AS autoid;");
			ResultSetMetaData metaData = rs.getMetaData();
            if (rs.next()) {
            	return rs.getInt("autoid");
            }
			return row;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				c.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.releaseConn(c);
		}
	}
	

	/**
	 * insert
	 * @param bean java bean
	 * @return 0 则是插入失败； 大于0则是插入成功，返回的插入的自增id
	 */
	public Integer insert(Object bean) {
		String sql = genInsertSqlByBean(bean);
		if(sql.length() < 1){
			return 0;
		}
		return insert(sql);
	}
	
	/**
	 * update
	 * @param sql
	 * @return
	 */
	public Integer update(String sql) {
		return insert(sql);
	}
	
	/**
	 * delete
	 * @param sql
	 * @return
	 */
	public Integer delete(String sql) {
		return insert(sql);
	}
	
	/**
	 * conn 关闭
	 */
	public void closeConn(){
		try {
			if(conn != null && !conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将  数据库查询的 ResultSet 转化为java bean
	 * @param rs
	 * @param entity java bean 。注意，数据库命名如 xiaoquLouhao  Java 实体类 XiaoquLouhao
	 * @return 若是失败出错，返回null
	 */
	public static <T> ArrayList<T> resultToEntity(ResultSet rs, Class<T> entity) {
        try {
            ArrayList<T> arrayList = new ArrayList<T>();
            ResultSetMetaData metaData = rs.getMetaData();
            //获取总列数,确定为对象赋值遍历次数
            int count = metaData.getColumnCount();
            while (rs.next()) {
                // 创建对象实例
                T newInstance = entity.newInstance();
                // 开始为一个对象赋值
                for (int i = 1; i <= count; i++) {
                    // 给对象的某个属性赋值
//                    String name = metaData.getColumnName(i).toLowerCase();
                    String name = metaData.getColumnName(i);
                    // 改变列名格式成java命名格式
                    //name = toJavaField(name);
                    // 首字母大写
                    String substring = name.substring(0, 1);
                    String replace = name.replaceFirst(substring, substring.toUpperCase());
                    // 获取字段类型
                    Class<?> type = entity.getDeclaredField(name).getType();
                    Method method = entity.getMethod("set" + replace, type);
                    //判断读取数据的类型
                    if (type.isAssignableFrom(String.class)) {
                        method.invoke(newInstance, rs.getString(i));
                    } else if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
                        method.invoke(newInstance, rs.getInt(i));
                    } else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
                        method.invoke(newInstance, rs.getBoolean(i));
                    } else if (type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)) {
                    	method.invoke(newInstance, rs.getDouble(i));
                    } else if (type.isAssignableFrom(Date.class)) {
                        method.invoke(newInstance, rs.getDate(i));
                    } else if (type.isAssignableFrom(BigDecimal.class)) {
                        method.invoke(newInstance, rs.getBigDecimal(i));
                    } else if (type.isAssignableFrom(long.class)){
                    	method.invoke(newInstance, rs.getLong(i));
                    }
                }
                arrayList.add(newInstance);
            }
            return arrayList;
        } catch (Exception e) {
           e.printStackTrace();
        }
		return null;
	}
	
	
	
	/**
	 * 获取 insert的插入语句，通过javabean
	 * table name 便是 javabean类的名字。
	 * 大小写全部原样不会进行任何转换
	 * @return 返回如 insert into "order" ("id","instId","price","size","side","createTime") values (123456,null,0.0,0.0,null,23545);
	 */
	public static String genInsertSqlByBean(Object obj){
		StringBuffer name = new StringBuffer();
		StringBuffer value = new StringBuffer();
		
		Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
            	if(name.length() > 0){
            		name.append(",");
            		value.append(",");
            	}
            	name.append("\""+field.getName()+"\"");
                 //判断读取数据的类型
            	if (field.get(obj).getClass().isAssignableFrom(String.class)) {
            		value.append("\""+field.get(obj)+"\"");
                }else{
                	value.append(field.get(obj));
                }
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return "";
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return "";
			}
        }
        
        return "insert into \""+getTableName(obj.getClass())+"\" ("+name.toString()+") values ("+value.toString()+");";
	}
	
	/**
	 * 获取数据表名字
	 * @param bean bean类的class，如 Order.class
	 */
	public static String getTableName(Class cls){
		return cls.getSimpleName().toLowerCase();
	}
	
	public static void main(String[] args)  {
		Order order = new Order();
		order.setId("123456");
		order.setCreateTime(23545);
		
		
	}
	
}
