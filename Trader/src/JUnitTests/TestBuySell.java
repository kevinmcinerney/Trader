package JUnitTests;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;

public class TestBuySell {

	@Test
	public void test() {
		File file = new File(TestBuyAction.class.getResource("/TestPackage/buyselltest.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin", 1000, new Stock[]{stk});
		kevin.setStrategy("BHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHS");
		kevin.setMasterTableData();
		
		//System.out.println("Bin size is: " + stk.getBinSize());
		//System.out.println("Master table is: " + kevin.getMasterTable().entrySet());
		kevin.evaluateTrader();
		Assert.assertEquals(230568.97, kevin.getWallet(), 0.01);
	}

}
