package com.xnx3.okex.thread;

import com.xnx3.net.HttpResponse;
import com.xnx3.okex.Global;
import com.xnx3.okex.util.HttpsUtil;

import net.sf.json.JSONObject;

/**
 * domain切换，自动切换到合适的适合访问的节点
 * @author 管雷鸣
 *
 */
public class DomainChange extends Thread{
	public static String[] domains = {"http://hk.okex.zvo.cn","http://xinjiapo.okex.zvo.cn","http://meiguo.okex.zvo.cn"};
	
	public void run() {
		
		while (true) {
			
			for (int i = 0; i < domains.length; i++) {
				try {
					HttpResponse hr = HttpsUtil.http.get(domains[i]+"/api/v5/public/time");
					JSONObject json = JSONObject.fromObject(hr.getContent());
					long ts = json.getJSONArray("data").getJSONObject(0).getLong("ts");
					if(ts > 1){
						//接口正常，当前请求的接口设置为这个
						Global.OKEX_DOMAIN = domains[i];
						System.out.println("set domain : "+Global.OKEX_DOMAIN);
						break;
					}
				} catch (Exception e) {
					//接口出问题，继续试下一个
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
