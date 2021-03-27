package com;

import com.alibaba.fastjson.JSON;
import com.okcoin.commons.okex.open.api.bean.futures.param.Order;
import com.okcoin.commons.okex.open.api.bean.futures.result.OrderResult;
import com.okcoin.commons.okex.open.api.bean.futures.result.ServerTime;
import com.okcoin.commons.okex.open.api.config.APIConfiguration;
import com.okcoin.commons.okex.open.api.service.GeneralAPIService;
import com.okcoin.commons.okex.open.api.service.futures.FuturesTradeAPIService;
import com.okcoin.commons.okex.open.api.service.futures.impl.FuturesTradeAPIServiceImpl;
import com.okcoin.commons.okex.open.api.service.futures.impl.GeneralAPIServiceImpl;

public class Test {
	public static void main(String[] args) {
		

        APIConfiguration config = new APIConfiguration();
        config.setEndpoint("https://www.okex.com/");
        //secretKey,api注册成功后页面上有
        config.setApiKey("");
        config.setSecretKey("");
        //Passphrase忘记后无法找回
        config.setPassphrase("");
        config.setPrint(true);

        GeneralAPIService marketAPIService = new GeneralAPIServiceImpl(config);
        ServerTime time = marketAPIService.getServerTime();
        System.out.println(JSON.toJSONString(time));

        FuturesTradeAPIService tradeAPIService = new FuturesTradeAPIServiceImpl(config);

//        Order order = new Order();
//        order.setClient_oid("OkexTestFuturesOrder2020");
//        order.setInstrument_id("BTC-USD-200626");
//        order.setType("1");
//        order.setPrice("7000");
//        order.setSize("400");
//        order.setMatch_price("0");
//        order.setOrder_type("0");
//        OrderResult orderResult = tradeAPIService.order(order); 
//        System.out.println(JSON.toJSONString(orderResult));
		
	}
}
