package com.xnx3.okex;

import java.util.LinkedList;
import java.util.List;

import com.xnx3.okex.suanfa.market.kLine.KBuyBean;
import com.xnx3.okex.suanfa.market.kLine.KLine;

/**
 * 实时监控的 instid 低价
 * @author 管雷鸣
 *
 */
public class EyeInstId {
	
	public static List<String> instIdList = new LinkedList<String>();
	static{
		instIdList.add("PMA-BTC");
		instIdList.add("FAIR-USDT");
		instIdList.add("TOPC-USDT");
		instIdList.add("ROAD-USDT");
		instIdList.add("DNA-USDT");
		instIdList.add("SOC-USDT");
		instIdList.add("XSR-USDT");
		instIdList.add("XPO-USDT");
		instIdList.add("GTO-USDT");
		instIdList.add("PLG-USDT");
		instIdList.add("ACT-USDT");
		instIdList.add("ALV-USDT");
		instIdList.add("HYC-USDT");
		instIdList.add("XUC-USDT");
		instIdList.add("TMTG-USDT");
		instIdList.add("LAMB-USDT");
	}
}
