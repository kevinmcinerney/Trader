package JUnitTests;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;


public class TestRenkoChart {

	@Test
	public void testNumberOfRenkoEntries() {
		File file = new File(TestBinSize.class.getResource("/TestPackage/binsizetest.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin",100000, new Stock[]{stk});
		kevin.setStrategy("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		kevin.setMasterTableData();
		
		kevin.evaluateTrader();
		Assert.assertEquals(9, stk.getRenkoStockData().size(), 0);
	}
	
	@Test
	public void testLastDateInRenkoChartMap(){
		File file = new File(TestBinSize.class.getResource("/TestPackage/binsizetest.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin",100000, new Stock[]{stk});
		kevin.setStrategy("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		kevin.setMasterTableData();
		
		kevin.evaluateTrader();
		
		//System.out.println("Last Date is " + stk.getRenkoStockData().get(stk.getRenkoStockData().size() - 1).keySet());
		Assert.assertEquals("[Sat Apr 12 00:00:00 SAST 2003]", stk.getRenkoStockData().get(stk.getRenkoStockData().size() - 1).keySet().toString());
		
	}
	
	@Test
	public void testRenkoChartPattern(){
		File file = new File(TestBinSize.class.getResource("/TestPackage/binsizetest.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader kevin = new Trader("Kevin",100000, new Stock[]{stk});
		kevin.setStrategy("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		kevin.setMasterTableData();
		
		kevin.evaluateTrader();
		
		List<LinkedHashMap<Date, String>> renkoHashMaps = stk.getRenkoStockData();
		
		String renkoPattern = "";
		

		for(int i = 0; i < renkoHashMaps.size(); i++){
			
			renkoPattern += stk.getRenkoStockData().get(i).values().toArray()[0];
			//System.out.println("Result is " + renkoPattern);
			
		}
		
		String expected = "010101010";
		Assert.assertEquals(expected, renkoPattern);
		
	}

}
