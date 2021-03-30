package com.xnx3.okex.bean;

/**
 * 账单、订单
 * @author 管雷鸣
 *
 */
public class Bill {
	
	public String billId;	//账单ID
	public String orderId;	//订单ID,对应接口的ordId
	public String instType;	//产品类型
	public String type;		//账单类型
	public long createTime;		//账单创建时间，Unix时间戳的毫秒数格式，如 1597026383085,对应接口的 ts
	public double size;		//数量 ，对应接口的 sz
	public double fee;		//手续费，正数代表平台返佣 ，负数代表平台扣除
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
	@Override
	public String toString() {
		return "Bill [billId=" + billId + ", orderId=" + orderId + ", instType=" + instType + ", type=" + type
				+ ", createTime=" + createTime + ", size=" + size + ", fee=" + fee + "]";
	}
	
}
