package com.xnx3.okex.action;

import java.util.HashMap;
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
	//key:instId  value:当前的Book数据
	public static Map<String, Book> bookMap;
	static{
		bookMap = new HashMap<String, Book>();
	}

	public static void main(String[] args) {
		//加载okex.config
		OkexSet.load();
		System.out.println("搜索开启。。。。");
		
//		sell("PMA-BTC");
		
//		check("XPO-USDT", 0.05, 1005);
//		check("XUC-USDT", 0.01, 1.1);
		
//		
		JSONArray allHangqing = Ticker.allHangqing();
		while(true){
			for (int i = 0; i < allHangqing.size(); i++) {
				String instId = allHangqing.getJSONObject(i).getString("instId");
				String moneyName = InstUtil.getPriceName(instId);
				if(moneyName.equals("USDT") || moneyName.equals("BTC")){
					check(instId, 0.02, 0);
				}
			}
			
			try {
				//每20秒进行一次检测
				Thread.sleep(20 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	/**
	 * 暴跌检测
	 * @param instId
	 * @param baifenbi 暴跌暴涨的百分比，这里传入 0.01~0.99，  代表暴涨暴跌1%~99%。 当然也可以传入 0.001
	 * @param buySize 自动委托购买的数量
	 */
	public static void check(String instId, double baifenbi, double buySize){
		
		//加个延迟 50毫秒
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/******* 1. 检测出价的深度，是否出现了暴跌，暴跌百分比是否达标 ********/
		
		//获取当前深度
		Book book = Market.books(instId, 5);
		
		//上次拉取的数据
		Book upBook = bookMap.get(instId); 
		if(upBook == null){
			//当前是第一次，还没有上次的数据，那么就只是保留数据
			bookMap.put(instId, book);
			return;
		}

		/*** 之前的有数据，那么将现在的跟之前的数据进行比较，看是否有跌 ***/
		//当前买的
		PriceNumber buyPn = book.getBids().get(0);
		//上次买的
		PriceNumber buyPn_up = upBook.getBids().get(0);
		//计算是否暴跌
		double diefu = (buyPn_up.getPrice() - buyPn.getPrice())/buyPn.getPrice();	//跌幅百分比
//		System.out.println(instId+"  -- 跌幅:"+DoubleUtil.doubleToString(diefu));
		
		//缓存最新数据
		bookMap.put(instId, book);
		
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
		
		//计算当前最低购买数量
		double buyMinSize = (instrument.getMinSize()*2) + (instrument.getMinAddSize());
		
		//计算出当前最低购买数量的金额。如果是btc，换算成USDT
		double buyMinMoney = -1;
		String moneyName = InstUtil.getPriceName(instId);
		if(moneyName.equals("USDT")){
			buyMinMoney = buyMinSize * buyPrice;
		}else if(moneyName.equals("BTC")){
			//将BTC换算为USDT
			double usdtPrice = Money.BtcToUsdt(buyPrice);
			buyMinMoney = buyMinSize * usdtPrice;
		}
		if(buyMinMoney < 0){
			//计算的购买总金额不合规，退出
			return;
		}
		if(buyMinMoney > 10){
			//超过最大数的USDT太贵，风险大，不买
			return;
		}
		
		
		/******* 4. 分析完成，可以购买，进行购买操作 ********/
		
		//分析完成，合适，可以自动购买
		System.out.println(instId+ "当前最低价："+buyPn.getPrice()+" , 每次最小加价："+Public.getInstrument(instId).getMinAddPrice()+", 计算出来的购买价："+buyPrice+",  消耗USDT："+DoubleUtil.doubleToString(buyMinMoney));
		
		//创建委托订单
		String orderId = Trade.createOrder(instId, "buy", buyMinSize, buyPrice);
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
