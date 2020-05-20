package com.assignment;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class will store the new trade information post validation of all rules in TradeAssignment doc
 * 
 * @author SONY
 *
 */
public class TradeStore {

	// Assumption: tradeBeanList is a datastore of trades.
	private static List<TradeBean> tradeBeanList = null;
	
	static {
/*		CopyOnWriteArrayList is used so that it won't throw ConcurrentModificationException 
		when we replace new bean which has same version at line no: 76
*/	    
		tradeBeanList = new CopyOnWriteArrayList<>();
		
		TradeBean tradeBean = new TradeBean();
		tradeBean.setBookId("B1");
		tradeBean.setCounterPartyId("CP-1");
		tradeBean.setCreatedDate(new Date());
		tradeBean.setExpired('N');
		tradeBean.setMaturityDate(new Date("20/5/2022"));
		tradeBean.setTradeId("T1");
		tradeBean.setVersion(1);
		tradeBeanList.add(tradeBean);

		tradeBean = new TradeBean();
		tradeBean.setBookId("B1");
		tradeBean.setCounterPartyId("CP-2");
		tradeBean.setCreatedDate(new Date());
		tradeBean.setExpired('N');
		tradeBean.setMaturityDate(new Date("20/5/2021"));
		tradeBean.setTradeId("T2");
		tradeBean.setVersion(2);
		tradeBeanList.add(tradeBean);

		tradeBean = new TradeBean();
		tradeBean.setBookId("B2");
		tradeBean.setCounterPartyId("CP-3");
		tradeBean.setCreatedDate(new Date());
		tradeBean.setExpired('Y');
		tradeBean.setMaturityDate(new Date("20/5/2040"));
		tradeBean.setTradeId("T3");
		tradeBean.setVersion(3);
		tradeBeanList.add(tradeBean);

		System.out.println(tradeBeanList);

		for(TradeBean tdBean : tradeBeanList) {
			if(tdBean.getMaturityDate().compareTo(new Date()) < 0) {
				tdBean.setExpired('N');
			}
		}
	}
		public List<TradeBean> storeTradeInfo(TradeBean tradeBeanToInsert) throws InvalidTradeException {
			System.out.println(tradeBeanList);
			int counter = 0;
			Date todayDate = new Date();
			boolean validTrade = true;
			for(TradeBean tradeBean : tradeBeanList){
				
				if(tradeBean.getTradeId().equals(tradeBeanToInsert.getTradeId())){
					
					if(tradeBeanToInsert.getVersion() < tradeBean.getVersion()) {
						validTrade = false;
						throw new InvalidTradeException("Invalid Trade! Version should not be lower than existing trade version");
					}
					if(tradeBeanToInsert.getVersion() == tradeBean.getVersion()) {
						tradeBeanList.add(counter, tradeBeanToInsert);
					}
				}
				counter++;
			}

			if(tradeBeanToInsert.getMaturityDate().compareTo(todayDate) < 0){
				validTrade = false;
			}
			
			if(validTrade) {
				tradeBeanList.add(tradeBeanToInsert);
			}

			return tradeBeanList;
            			
	}
}
