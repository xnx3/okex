package com.xnx3.okex.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.media.TTSUtil;
import com.xnx3.okex.api.Market;
import com.xnx3.okex.api.Public;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.api.Trade;
import com.xnx3.okex.bean.Instrument;
import com.xnx3.okex.bean.market.Book;
import com.xnx3.okex.bean.market.Candle;
import com.xnx3.okex.bean.market.PriceNumber;
import com.xnx3.okex.suanfa.market.kLine.KLine;
import com.xnx3.okex.util.DoubleUtil;
import com.xnx3.okex.util.InstUtil;
import com.xnx3.okex.util.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 暴跌买入
 * @author 管雷鸣
 *
 */
public class BaoDieMaiRu {
	public static double buyMaxMoney = 20;	//购买最小单位的币，一次购买最大花费的总金额，单位是USDT
	
	//key:instId  value:当前的Book数据
	public static Map<String, BookBean> bookMap;
	public static Map<String, Double> instIdDayMoney;	//这个币24小时的成交量，单位是USDT
	
	static{
		bookMap = new HashMap<String, BookBean>();
		instIdDayMoney = new HashMap<String, Double>();
	}

	public static void main(String[] args) {
		//加载okex.config
		OkexSet.load();
		System.out.println("搜索开启。。。。");
		
//		sell("PMA-BTC");
		
//		check("XPO-USDT", 0.05, 1005);
//		check("XUC-USDT", 0.01, 1.1);
		
//		
		//将每个币的行情取出来，主要就是一天的成交金额
		JSONArray hangqingArray = Ticker.allHangqing();
		for (int i = 0; i < hangqingArray.size(); i++) {
			JSONObject json = hangqingArray.getJSONObject(i);
			double money = json.getDouble("volCcy24h");
			String instId = json.getString("instId");
			String moneyName = InstUtil.getPriceName(instId);
			if(moneyName.equals("BTC")){
				//将BTC转化为USDT
				instIdDayMoney.put(instId, Money.BtcToUsdt(money));
			}else if(moneyName.equals("USDT")){
				instIdDayMoney.put(instId, money);
			}
			
			System.out.println(instId+", "+instIdDayMoney.get(instId));
		}
		
		
		JSONArray allHangqing = Ticker.allHangqing();
		Map<String, String> instIdMap = new HashMap<String, String>();
		for (int i = 0; i < allHangqing.size(); i++) {
			String instId = allHangqing.getJSONObject(i).getString("instId");
			String moneyName = InstUtil.getPriceName(instId);
			if(moneyName.equals("USDT") || moneyName.equals("BTC")){
				instIdMap.put(instId, instId);
			}
		}
		
		//删除一些稳定币
		Iterator<Map.Entry<String, String>> itRemove = instIdMap.entrySet().iterator();
        while(itRemove.hasNext()){
            Map.Entry<String, String> entry = itRemove.next();
            String instId = entry.getKey();
            String name = InstUtil.getName(instId);
            if(name.equals("USDK") || name.equals("USDT") || name.equals("USDC") || name.equals("DAI") ){
            	itRemove.remove();//使用迭代器的remove()方法删除元素，后面不再扫描这个币
            	System.out.println("删除稳定币："+instId);
            }
        }
        
        //删除掉24小时成交价格小于1万USDT的币
        for (Map.Entry<String, Double> entry : instIdDayMoney.entrySet()) {
        	if(entry.getValue() != null && entry.getValue() < 10000){
        		//删除
        		if(instIdMap.get(entry.getKey()) != null){
        			instIdMap.remove(entry.getKey());
        			System.out.println("删除不到24小时成交不到1万USDT的币:"+entry.getKey());
        		}
        	}
        }
        
		System.out.println(instIdMap.size());
		
		while(true){
			
			Iterator<Map.Entry<String, String>> it = instIdMap.entrySet().iterator();
	        while(it.hasNext()){
	            Map.Entry<String, String> entry = it.next();
	            String instId = entry.getKey();
				try {
					check(instId, 0.03);
					
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
					it.remove();//使用迭代器的remove()方法删除元素，后面不再扫描这个币
				}
	        }
			
			try {
				//每20秒进行一次检测
				Thread.sleep(1 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				System.out.println("一轮完毕，"+DateUtil.dateFormat(DateUtil.timeForUnix10(), "yyyy-MM-dd hh:mm:ss"));
			} catch (NotReturnValueException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static double getInstIdDayChengjiaoMoney(String instId){
		
		
		return 0;
	}
	

	/**
	 * 暴跌检测
	 * @param instId
	 * @param baifenbi 暴跌暴涨的百分比，这里传入 0.01~0.99，  代表暴涨暴跌1%~99%。 当然也可以传入 0.001
	 * @param buySize 自动委托购买的数量
	 */
	public static void check(String instId, double baifenbi){
		/******* 1. 检测出价的深度，是否出现了暴跌，暴跌百分比是否达标 ********/
		
		//获取当前深度
		Book book = Market.books(instId, 5);
		
		//上次拉取的数据
		BookBean upBookBean = bookMap.get(instId);
		if(upBookBean == null){
			//当前是第一次，还没有上次的数据，那么就只是保留数据
			upBookBean = new BookBean();
			bookMap.put(instId, new BookBean(book, DateUtil.timeForUnix10()));
			return;
		}
		//判断上次获取到现在有没有超过75秒，超过75秒则不作数
		if(DateUtil.timeForUnix10() - upBookBean.getTime() > 75){
			System.out.println("超时，"+(DateUtil.timeForUnix10() - upBookBean.getTime())+", 不作数， -- "+upBookBean.getTime());
			bookMap.put(instId, new BookBean(book, DateUtil.timeForUnix10()));
			return;
		}
		
		Book upBook = upBookBean.getBook();

		/*** 之前的有数据，那么将现在的跟之前的数据进行比较，看是否有跌 ***/
		//当前买的
		PriceNumber buyPn = book.getBids().get(0);
		//上次买的
		PriceNumber buyPn_up = upBook.getBids().get(0);
		//计算是否暴跌
		double diefu = (buyPn_up.getPrice() - buyPn.getPrice())/buyPn.getPrice();	//跌幅百分比
//		System.out.println(instId+"  -- 跌幅:"+DoubleUtil.doubleToString(diefu));
		
		//缓存最新数据
		bookMap.put(instId, new BookBean(book, DateUtil.timeForUnix10()));
		
		if(diefu < 0 || diefu < baifenbi){
			//没有达到购买条件，退出当前。
			return;
		}
		//System.out.println("暴跌，跌幅："+DoubleUtil.doubleToString(diefu)+", 继续K线分析");
//		
		
		
		/******* 2. K线分析最近100分钟内，这个币是不是属于可以购买的，是不是危险的币、以及无交易量的币 ********/
		
		//K线分析，判断当前1分钟为区间，100区间内，这个币是否是可购买的
		if(!KLine.isDigu(instId, 1, 0.1)){
			//System.out.println(instId+", K线分析不合格，不自动购买。");
			//K线分析不符合
			return;
		}
		
		
		/******* 3. 获取这个币本身的一些属性信息，生成购买价、购买数量，以及判断总购买金额是否超过预设最大值 ********/
		
		Instrument instrument = Public.getInstrument(instId);
		
		//取当前要委托购买的最低价，也就是当前最低价在额外加一点点价
		double buyPrice = DoubleUtil.add(buyPn.getPrice(), instrument.getMinAddPrice());
		
		//计算当前购买数量(这里默认差不多是最小数量了)
		double buySize = (instrument.getMinSize()*2) + (instrument.getMinAddSize());
		
		//计算出当前购买数量的金额。如果是btc，换算成USDT
		double buyAllMoney = -1;
		String moneyName = InstUtil.getPriceName(instId);
		if(moneyName.equals("USDT")){
			buyAllMoney = buySize * buyPrice;
		}else if(moneyName.equals("BTC")){
			//将BTC换算为USDT
			double usdtPrice = Money.BtcToUsdt(buyPrice);
			buyAllMoney = buySize * usdtPrice;
		}
		if(buyAllMoney < 0){
			//计算的购买总金额不合规，退出
			return;
		}
		if(buyAllMoney > buyMaxMoney){
			//超过最大数的USDT太贵，风险大，不买
			return;
		}
		
		/***** 计算，将本次购买的总金额控制在10USDT左右。如果不够这么多，那么多加点数量 ******/
		double currentMoneyBeishu = 1;
		double instIdDayMoneyDouble = instIdDayMoney.get(instId);
		if(instIdDayMoneyDouble < 10000){
			//成交额小于10万USDT，那么就不翻倍了，就按照最小下单吧
			currentMoneyBeishu = 1;
		}else if(instIdDayMoneyDouble < 30000){
			//不高于6USDT
			currentMoneyBeishu = 4/buyAllMoney;
		}else if(instIdDayMoneyDouble < 50000){
			//不高于10USDT
			currentMoneyBeishu = 8/buyAllMoney;
		}else if(instIdDayMoneyDouble < 100000){
			//不高于20USDT
			currentMoneyBeishu = 15/buyAllMoney;
		}else if(instIdDayMoneyDouble < 1000000){
			//不高于40USDT
//			currentMoneyBeishu = 40/buyAllMoney;
			currentMoneyBeishu = 25/buyAllMoney;
		}else if(instIdDayMoneyDouble < 5000000){
			//不高于70USDT
//			currentMoneyBeishu = 70/buyAllMoney;
			currentMoneyBeishu = 35/buyAllMoney;
		}else{
			//更多，不高于200。这里以后可以在加
//			currentMoneyBeishu = 200/buyAllMoney;
			currentMoneyBeishu = 40/buyAllMoney;
		}
		
		buySize = buySize * currentMoneyBeishu;
		//重新计算总价
		if(moneyName.equals("USDT")){
			buyAllMoney = buySize * buyPrice;
		}else if(moneyName.equals("BTC")){
			//将BTC换算为USDT
			double usdtPrice = Money.BtcToUsdt(buyPrice);
			buyAllMoney = buySize * usdtPrice;
		}
		if(buyAllMoney > (40+1)){
			//这里先按照最大算吧
			//超过最大数的USDT太贵，风险大，不买。 因为是按照倍数加的数量，所以可能会有浮点误差。多加了1
			System.out.println("error - error - -------计算超过最大数："+buyAllMoney);
			return;
		}
		
		/******* 4. 分析完成，可以购买，进行购买操作 ********/
		
		//分析完成，合适，可以自动购买
		System.out.println(instId+ "当前最低价："+buyPn.getPrice()+" , 每次最小加价："+Public.getInstrument(instId).getMinAddPrice()+", 计算出来的购买价："+buyPrice+",  消耗USDT："+DoubleUtil.doubleToString(buyAllMoney));
		
		//创建委托订单
		String orderId = Trade.createOrder(instId, "buy", buySize, buyPrice);
		if(orderId == null || orderId.length() < 2){
			//创建订单失败
		}else{
			//创建订单成功后，跟踪，15分钟内没有成交，那么自动撤销
			new Thread(new Runnable() {
				public void run() {
					int xunhuan = 12; //120s
					while(xunhuan-- > 0){
						//每10s循环检测一次
						try {
							Thread.sleep(10*1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						//如果已成交，那么自动委托卖出
						JSONObject orderJson = Trade.order(instId, orderId);
						if(orderJson.getString("state").equals("filled")){
							//完全成交了，那么自动委托卖出去
							
							//买入的价格
							double mairujia = orderJson.getDouble("avgPx");
							
							/****** 计算卖出去的价格 *****/
							double sellPrice = mairujia*1.015;	//卖出去的价格，直接就是买入价格提高 1.5%
//							double allPrice = 0;	//20次的总价格
//							
//							//获取当前20分钟内，这个币卖方的卖价平均价
//							List<Candle> candleList = Market.candles(instId, "1m");
//							for (int i = 0; i < candleList.size() & i < 20; i++) {
//								Candle candle = candleList.get(i);
//								allPrice = DoubleUtil.add(allPrice, candle.getKaipanPrice());
//							}
//							
//							//平均价
//							sellPrice = allPrice/20;
//							System.out.println("20次总价:"+allPrice+", 平均价："+pingjujjia);
							
							//判断计算出来的平均价是否比买入价低，如果比买入价低，那么就先不卖了
							
							if(mairujia >= sellPrice){
								//这一单是赔本的，退出，不卖了
								System.out.println("========自动卖出时，计算的是赔本了，这一笔不卖了。买入均价："+mairujia+", 卖出计算你的平均价："+sellPrice);
								return;
							}
							
							//计算卖出数量，要减去买入扣除的手续费
							double sellSize = orderJson.getDouble("sz") * 0.998;
							Trade.createOrder(instId, "sell", sellSize, sellPrice);
							
							Log.append("已自动买入 "+instId+",单价："+mairujia+"， 自动以单价"+sellPrice+"卖出");
							//结束线程
							return;
						}
					}
					
					//如果未成交自动取消委托
					JSONObject orderJson = Trade.order(instId, orderId);
					if(!orderJson.getString("state").equals("filled")){
						//只要没有完全成交，那都撤销订单
						Trade.cancelOrder(instId, orderId);
						Log.append("自动下单==="+instId+"超时未成交，已自动撤销委托。单价："+buyPrice);
						TTSUtil.speakByThread(instId+"超时未成交已自动撤销委托");
					}
					
				}
			}).start();
		}
		
		TTSUtil.speakByThread("暴跌"+instId+"已自动委托购买");
	}
	
}
class BookBean{
	public Book book;
	public int time;	//当前book的获取时间
	
	public BookBean() {
	}
	public BookBean(Book book, int time) {
		this.book = book;
		this.time = time;
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
}
