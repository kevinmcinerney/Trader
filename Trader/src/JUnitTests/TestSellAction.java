package JUnitTests;

import java.io.File;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;

public class TestSellAction {
	
	@Test
	public void testCorrectNumberOfSells() {
		File file = new File(TestBuyAction.class.getResource("/TestPackage/buyselltest.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin", 0, new Stock[]{stk});
		kevin.setStrategy("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
		kevin.setMasterTableData();
		
		HashMap<Stock, Integer> portfolio = new HashMap<Stock, Integer>();
		portfolio.put(stk, 10);
		kevin.setStockPortfolio(portfolio);
		
		kevin.evaluateTrader();
		Assert.assertEquals(1, kevin.getNumberOfTransactions(), 0);
	}

}
