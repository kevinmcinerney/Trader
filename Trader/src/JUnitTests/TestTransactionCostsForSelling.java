package JUnitTests;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;

public class TestTransactionCostsForSelling {

	@Test
	public void testTransactionsCostsForSellings() {
		File file = new File(TestBuyAction.class.getResource("/TestPackage/testTransactionsInputFile.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin", 1000, new Stock[]{stk});
		kevin.setStrategy("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
		kevin.setMasterTableData();
		
		Assert.assertEquals(764.94, kevin.getTransactionCosts(false, 1000), 0.01);
	}

}
