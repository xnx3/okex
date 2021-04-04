package com.xnx3.okex.suanfa;

import java.util.List;

import com.xnx3.DateUtil;
import com.xnx3.okex.api.Market;
import com.xnx3.okex.api.Ticker;
import com.xnx3.okex.bean.market.Book;
import com.xnx3.okex.bean.market.PriceNumber;
import com.xnx3.okex.util.DoubleUtil;

import net.sf.json.JSONObject;

/**
 * 庄家提价，买家连续出现多次很低的、一样的价格，然后出现一个大单挂在上面买，那可能就有大庄家在提价了
 * 只有币种比较小的才会出现，比如 PMA-BTC等24小时交易量几万的
 * @author 管雷鸣
 *
 */
public class SearchZhuangjiaTijia {
	
	public static void main(String[] args) {
		
		find("PMA-BTC");
		
	}
	
	
	public static void find(String instId){
		Book book = Market.books(instId, 400);
		
		List<PriceNumber> pnList = book.getBids(); 
		for (int i = 0; i < pnList.size(); i++) {
			PriceNumber pn = pnList.get(i);
			System.out.println(DoubleUtil.doubleToString(pn.getPrice())+",  number: "+pn.getNumber());
		}
		
		
	}
	
}
