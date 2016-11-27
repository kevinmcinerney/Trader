package JUnitTests;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import AI.HillClimber;
import TradingFloor.Stock;
import TradingFloor.Trader;

public class TestHillClimber {

	@Test
	public void test() {
		File file = new File(TestBuyAction.class.getResource("/TestPackage/testHillClimberInputFile.txt").getFile());
		Stock[] stocks = {new Stock("testy", file)};
		Trader curTrader = new Trader("firstTrader",100000, stocks);
		HillClimber hc = new HillClimber(curTrader);
		curTrader.setStrategy("SHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHS");
		curTrader.setMasterTableData();
		curTrader = hc.search(curTrader);
		//System.out.println("Best trader is: " + curTrader.trimString(curTrader.getStrategy().values().toString()));
		Assert.assertEquals("BHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHS", curTrader.trimString(curTrader.getStrategy().values().toString()));
	}

}
