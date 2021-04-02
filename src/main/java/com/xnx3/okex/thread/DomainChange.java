package com.xnx3.okex.thread;

import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.okex.Global;
import net.sf.json.JSONObject;

/**
 * domain切换，自动切换到合适的适合访问的节点
 * @author 管雷鸣
 *
 */
public class DomainChange extends Thread{
	public static String[] domains = {"https://www.okex.win","http://hk.okex.zvo.cn","http://xinjiapo.okex.zvo.cn","http://meiguo.okex.zvo.cn"};
	public static HttpUtil http;
	static{
		http = new HttpUtil();
		http.setTimeout(10);
	}
	
	public static void main(String[] args) {
		new DomainChange().start();
	}
	
	public void run() {
		
		while (true) {
			
			for (int i = 0; i < domains.length; i++) {
				try {
					HttpResponse hr = http.get(domains[i]+"/api/v5/public/time");
					JSONObject json = JSONObject.fromObject(hr.getContent());
					long ts = json.getJSONArray("data").getJSONObject(0).getLong("ts");
					if(ts > 1){
						//接口正常，当前请求的接口设置为这个
						Global.OKEX_DOMAIN = domains[i];
						break;
					}
				} catch (Exception e) {
					//接口出问题，继续试下一个
//					System.out.println("DomainChange Thread -- "+domains[i]+"\t not use..");
					continue;
				}
			}
			
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
}
