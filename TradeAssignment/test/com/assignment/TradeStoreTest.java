package com.assignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;

import com.assignment.TradeBean;
import com.assignment.TradeStore;

/**
 * Test Class to test Trade Data Store
 * 
 * @author SONY
 *
 */
public class TradeStoreTest {

	TradeBean tradeBeanLowerVersion = null;
	TradeBean tradeBeanSameVersion = null;
	TradeBean tradeBeanlessMaturityDate = null;
	
	/**
	 * Prepare test data
	 */
	@Before
	public void storeTestData(){
		tradeBeanLowerVersion = new TradeBean();
		tradeBeanLowerVersion.setBookId("B2");
		tradeBeanLowerVersion.setCounterPartyId("CP-3");
		tradeBeanLowerVersion.setCreatedDate(new Date());
		tradeBeanLowerVersion.setExpired('Y');
		tradeBeanLowerVersion.setMaturityDate(new Date("20/5/2014"));
		tradeBeanLowerVersion.setTradeId("T3");
		tradeBeanLowerVersion.setVersion(1);

		tradeBeanSameVersion = new TradeBean();
		tradeBeanSameVersion.setBookId("B2");
		tradeBeanSameVersion.setCounterPartyId("CP-34");
		tradeBeanSameVersion.setCreatedDate(new Date());
		tradeBeanSameVersion.setExpired('Y');
		tradeBeanSameVersion.setMaturityDate(new Date("20/5/2050"));
		tradeBeanSameVersion.setTradeId("T3");
		tradeBeanSameVersion.setVersion(3);

		tradeBeanlessMaturityDate = new TradeBean();
		tradeBeanlessMaturityDate.setBookId("B2");
		tradeBeanlessMaturityDate.setCounterPartyId("CP-34");
		tradeBeanlessMaturityDate.setCreatedDate(new Date());
		tradeBeanlessMaturityDate.setExpired('Y');
		tradeBeanlessMaturityDate.setMaturityDate(new Date("20/5/2010"));
		tradeBeanlessMaturityDate.setTradeId("T3");
		tradeBeanlessMaturityDate.setVersion(5);
	}
	
	/**
	 * Test a Trade with lower version
	 * 
	 * @throws InvalidTradeException
	 */
	@Test(expected=InvalidTradeException.class)
	public void testStoreTradeInfoForLowerVersion() throws InvalidTradeException{
		TradeStore tradeStore = new TradeStore();
		tradeStore.storeTradeInfo(tradeBeanLowerVersion);
	}

	/**
	 * Test a trade with same version
	 * 
	 * @throws InvalidTradeException
	 */
	@Test
	public void testStoreTradeInfoForSameVersion() throws InvalidTradeException{
		TradeStore tradeStore = new TradeStore();
		List<TradeBean> tradeBeanList = tradeStore.storeTradeInfo(tradeBeanSameVersion);
		
		int version = 0;
		for(TradeBean tradeBean : tradeBeanList) {
			if(tradeBean.getTradeId().equals("T3")) {
				version = tradeBean.getVersion();
			}
		}
		Assert.assertEquals(3, version);
	}

	/***
	 * Test a trade with maturity date less than today's date
	 * 
	 * @throws InvalidTradeException
	 */
	@Test
	public void testStoreTradeInfoForLessMaturityDate() throws InvalidTradeException{
		Date todayDate = new Date();
		TradeStore tradeStore = new TradeStore();
		List<TradeBean> tradeBeanList = tradeStore.storeTradeInfo(tradeBeanlessMaturityDate);
		
		for(TradeBean tradeBean : tradeBeanList) {
			Assert.assertEquals(true, tradeBean.getMaturityDate().compareTo(todayDate) > 0);
		}
	}

}
