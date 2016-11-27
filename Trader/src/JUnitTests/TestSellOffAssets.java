package JUnitTests;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;

public class TestSellOffAssets {

	@Test
	public void testSellOffAssets() {
		File file = new File(TestBuyAction.class.getResource("/TestPackage/buyselltest.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin", 1000, new Stock[]{stk});
		kevin.setStrategy("BHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
		kevin.setMasterTableData();
		
		kevin.evaluateTrader();
		Assert.assertEquals(230568.978, kevin.getWallet(), 0.01);
	}

}
