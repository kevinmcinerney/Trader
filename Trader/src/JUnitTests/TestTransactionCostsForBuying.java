package JUnitTests;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;

public class TestTransactionCostsForBuying {

	@Test
	public void testTransactionCostsForBuying() {
		File file = new File(TestBuyAction.class.getResource("/TestPackage/testTransactionsInputFile.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin", 1000, new Stock[]{stk});
		kevin.setStrategy("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		kevin.setMasterTableData();
		
		Assert.assertEquals(767.7922, kevin.getTransactionCosts(true, 1000), 0.0001);
	}

}
