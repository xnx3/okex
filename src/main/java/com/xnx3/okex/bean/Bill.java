package com.xnx3.okex.bean;

/**
 * 不知道干嘛的，订单用 trade.order
 * @author 管雷鸣
 *
 */
public class Bill {
	
	public String billId;	//账单ID
	public String orderId;	//订单ID,对应接口的ordId
	public String instId;	//买的什么币，比如 PMA-USDK
	public String instType;	//产品类型
	public String type;		//账单类型
	public long createTime;		//账单创建时间，Unix时间戳的毫秒数格式，如 1597026383085,对应接口的 ts
	public double size;		//数量 ，对应接口的 sz
	public double fee;		//手续费，正数代表平台返佣 ，负数代表平台扣除
	public int subType;		//子账单类型。包括：1：买入 2：卖出 3：开多 4：开空 5：平多 6：平空 9：扣息 11：转入 12：转出 160：手动追加保证金 161：手动减少保证金 162：自动追加保证金 110：强制换币买入 111：强制换币卖出 100：强减平多 101：强减平空 102：强减买入 103：强减卖出 104：强平平多 105：强平平空 106：强平买入 107：强平卖出 109：强平惩罚费 110：强平换币转入 111：强平换币转出 125：自动减仓平多 126：自动减仓平空 127：自动减仓买入 128：自动减仓卖出 170：到期行权 171：到期被行权 172：到期作废 112：交割平多 113：交割平空 117：交割/期权穿仓代偿 173：资金费支出 174：资金费收入
	public String ccy;		//交易的币种，买卖的是什么币，如 PMA-USDK 这里就是PMA
	
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getInstType() {
		return instType;
	}
	public void setInstType(String instType) {
		this.instType = instType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public int getSubType() {
		return subType;
	}
	public void setSubType(int subType) {
		this.subType = subType;
	}
	
	public String getCcy() {
		return ccy;
	}
	public void setCcy(String ccy) {
		this.ccy = ccy;
	}
	@Override
	public String toString() {
		return "Bill [billId=" + billId + ", orderId=" + orderId + ", instId=" + instId + ", instType=" + instType
				+ ", type=" + type + ", createTime=" + createTime + ", size=" + size + ", fee=" + fee + ", subType="
				+ subType + "]";
	}
	
}
