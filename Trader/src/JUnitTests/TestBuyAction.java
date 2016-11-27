package JUnitTests;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;


public class TestBuyAction {

	/* When trader buys all the time, he will buy maximum shares
	 * on 8/04/2003. This is the first date on which there is a full 
	 * Renko pattern available (sixth day). On this day, the stock price is 1.-, therefore
	 * the costs incurred are just overhead costs - (approx R93.3). All shares will be purchased
	 * in one transaction, and because we evaluate the trader at the end to test his fitness
	 * (which is ROI), we must get the value of any shares he still has left in his portfolio
	 * which will call 'sellOffAssets' function and increment the numberOfTransactions
	 * variable to 2.
	 * 
	 */
	@Test
	public void testCorrectNumberOfBuys() {
		File file = new File(TestBuyAction.class.getResource("/TestPackage/testBuyActionInputFile.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin", 10000, new Stock[]{stk});
		kevin.setStrategy("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		kevin.setMasterTableData();
		//System.out.println("Bin size is " + stk.getBinSize());
		kevin.evaluateTrader();
		Assert.assertEquals(2, kevin.getNumberOfTransactions());
	}
	
	/*
	 * Test to ensure that the first buy action occurs
	 * on the sixth day which corresponds to the first date
	 * that a full Renko pattern has been formulated.
	 * 
	 */	
	@Test
	public void testCorrectDateWithFullRenkoPattern(){
		File file = new File(TestBuyAction.class.getResource("/TestPackage/binsizetest.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin", 132.012, new Stock[]{stk});
		kevin.setStrategy("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		kevin.setMasterTableData();
		
		kevin.evaluateTrader();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		Date date = null;
		try {
			date = df.parse("08/04/03");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(date, stk.getRealStockData().keySet().toArray()[5]);
	}
	
	@Test
	public void testCorrectNumberOfSharesThatCanBeBought(){
		//System.out.println("Test for correct number shares that can be bought: ");
		File file = new File(TestBuyAction.class.getResource("/TestPackage/testTransactionsInputFile.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin", 10000, new Stock[]{stk});
		kevin.setStrategy("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		kevin.setMasterTableData();
		//System.out.println("Wallet in test is "+kevin.getWallet());
		//System.out.println("Last stock price is " + stk.getLastStockPrice());
		Assert.assertEquals(92, kevin.calculateNumberOfAffordableShares(stk.getLastStockPrice()));
		
	}

}
