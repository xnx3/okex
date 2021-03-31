package com.xnx3.okex.bean.trade;


import com.xnx3.okex.action.Money;
import com.xnx3.okex.util.InstUtil;

import net.sf.json.JSONObject;

/**
 * 订单
 * @author 管雷鸣
 *
 */
public class Order {
	private String id;		//订单id，对应接口的 ordId
	private String instId;
	private double price;	//单价，对应接口的 px
	private double size;	//交易数量，对应接口的 sz
	private String side;	//是买还是卖， 买 buy， 卖sell
	private long createTime;	//订单创建时间，13位时间戳，对应接口的cTime
	private long updateTime;	//订单最后更改时间，13位时间戳，对应接口的uTime
	private String feeCcy;		//买卖的币，如 PMA-USDK ，那这里就是PMA
	private String rebateCcy;	//买卖使用的货币，如 PMA-USDK,那这里就是USDK
	private String state;	//订单状态， finish 已成交，已完成，  load 进行中 
	private double shouxufei;	//这笔订单的手续费，手续费扣除是负数。单位统一是 USDT
	private double money;		//这笔订单流通的金额，这是计算成 USDT 为单位的金额。 买的，钱出去了就是负的，  卖的钱进来了就是正的
	
	public Order() {
	}
	
	public Order(JSONObject json){
		this.id = json.getString("ordId");
		this.instId = json.getString("instId");
		this.price = json.getDouble("px");
		this.size = json.getDouble("sz");
		this.createTime = json.getLong("cTime");
		this.updateTime = json.getLong("uTime");
		this.feeCcy = json.getString("feeCcy");
		this.rebateCcy = json.getString("rebateCcy");
		
		//state状态
		this.state = json.getString("state");
		if(this.state.equals("live")){
			this.state = "进行中";
		}else if(state.equals("filled")){
			this.state = "已成交";
		}
		
		
		/* 计算手续费 */
		//先赋予，以支付的币种为单位，如 PMA-BTC，这里算出的单位就是BTC
		if(json.getString("feeCcy").toUpperCase().equals(this.rebateCcy)){
			//就是USDT、BTC这种了
			this.shouxufei = json.getDouble("fee");
		}else{
			//不是，那么是购买的目的币，要换算成USDT、BTC这种
			this.shouxufei = json.getDouble("fee") * this.price;
		}
		
		//如果是BTC，那么将BTC换算为USDT
		String moneyName = InstUtil.getPriceName(this.instId).toUpperCase();	//取出来的是USDT、BTC这种的可以用来交易的货币
		if(moneyName.equals("BTC")){
			//手续费
			this.shouxufei = Money.BtcToUsdt(this.shouxufei);
			
			// 计算这笔订单变动的金额，也是USDT为单位
			this.money = Money.BtcToUsdt((this.size * this.price));
			
			//这里要延迟0.3秒，避免接口请求太快
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if(moneyName.equals("USDT") || moneyName.equals("USDK")){
			//手续费，上面已经计算了，就是USDT
			this.money = this.size * this.price;
		}else{
			//手续费不支持
			System.out.println("Order bean -->   手续费不支持！ "+moneyName+",  "+json.toString());
		}
		
		
		this.side = json.getString("side");
		if(this.side.equals("sell")){
			//卖出的
			this.side = "卖出";
		}else if(this.side.equals("buy")){
			//买入的
			this.side = "买入";
			//买入，那money就是少了，减去
			this.money = -money;
		}
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getFeeCcy() {
		return feeCcy;
	}

	public void setFeeCcy(String feeCcy) {
		this.feeCcy = feeCcy;
	}

	public String getRebateCcy() {
		return rebateCcy;
	}

	public void setRebateCcy(String rebateCcy) {
		this.rebateCcy = rebateCcy;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getShouxufei() {
		return shouxufei;
	}

	public void setShouxufei(double shouxufei) {
		this.shouxufei = shouxufei;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	@Override
	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
	
}
