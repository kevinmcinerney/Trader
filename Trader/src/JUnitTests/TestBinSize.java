package JUnitTests;
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;


public class TestBinSize {

	@Test
	public void testBinSize() {
		
		File file = new File(TestBinSize.class.getResource("/TestPackage/binsizetest.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin",100, new Stock[]{stk});
		kevin.setStrategy("HHBSBSBSBSBSBSBSBSBSBSBSBSBSBSBS");
		kevin.setMasterTableData();
		
		kevin.evaluateTrader();
		//System.out.println("binsize is " + stk.getBinSize());
		Assert.assertEquals(10, stk.getBinSize(), 0);
	}

}
