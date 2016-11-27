package JUnitTests;
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;


public class TestRealChartData {

	@Test
	public void test() {
		File file = new File(TestRealChartData.class.getResource("/TestPackage/binsizetest.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin",100000, new Stock[]{stk});
		kevin.setStrategy("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		kevin.setMasterTableData();
		
		kevin.evaluateTrader();
		
		
		Assert.assertEquals(10, stk.getRealStockData().size(), 0);
	}

}
