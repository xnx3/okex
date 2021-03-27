package com.xnx3.okex;

import com.alibaba.fastjson.JSON;
import com.okcoin.commons.okex.open.api.bean.futures.result.ServerTime;
import com.okcoin.commons.okex.open.api.config.APIConfiguration;
import com.okcoin.commons.okex.open.api.service.GeneralAPIService;
import com.okcoin.commons.okex.open.api.service.futures.FuturesTradeAPIService;
import com.okcoin.commons.okex.open.api.service.futures.impl.FuturesTradeAPIServiceImpl;
import com.okcoin.commons.okex.open.api.service.futures.impl.GeneralAPIServiceImpl;

public class Test {
	public static void main(String[] args) {
        APIConfiguration config = new APIConfiguration();
        config.setEndpoint("http://192.168.80.14:8118");
        config.setApiKey("75c60758-be16-4acc-8f2d-66403e53072c");
        config.setSecretKey("8DF095FE9C662F787A60F3133A06414C");
        config.setPassphrase("19205A%9980");
        config.setPrint(false);

        GeneralAPIService marketAPIService = new GeneralAPIServiceImpl(config);
        ServerTime time = marketAPIService.getServerTime();
        // eg: {"epoch":"1520848286.633","iso":"2018-03-12T09:51:26.633Z"}
        System.out.println(JSON.toJSONString(time));

        FuturesTradeAPIService tradeAPIService = new FuturesTradeAPIServiceImpl(config);

//        Order order = new Order();
//        order.setClient_oid("TYPb040b3daa40f793e69f86344a1839");
//        order.setType(FuturesTransactionTypeEnum.OPEN_SHORT.code());
//        order.setProduct_id("BTC-USD-0331");
//        order.setPrice(99900.00);
//        order.setAmount(123);
//        order.setMatch_price(0);
//        order.setLever_rate(10.00);
//        OrderResult orderResult = tradeAPIService.newOrder(order);
//        // eg : {"client_oid":"TYPb040b3daa40f793e69f86344a1839","order_id":"400574928061440","result":true}
//        System.out.println(JSON.toJSONString(orderResult));
//		
		
	}
}
