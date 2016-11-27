package JUnitTests;

import java.io.File;
import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.Test;

import TradingFloor.Stock;
import TradingFloor.Trader;

public class TestGenerateCorrectAction {

	@Test
	public void testGenerateActions() {
		File file = new File(TestBuyAction.class.getResource("/TestPackage/testBuyActionInputFile.txt").getFile());
		Stock stk = new Stock("stk", file);
		Trader curTrader = new Trader("firstTrader",100000, new Stock[]{stk});
		curTrader.setStrategy("HBSHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
		curTrader.setMasterTableData();
		LinkedHashMap<String, Character> map = curTrader.generateNextAction("00000", curTrader.trimString(curTrader.getStrategy().values().toString()));
		LinkedHashMap<String, Character> map1 = curTrader.generateNextAction("00001", curTrader.trimString(curTrader.getStrategy().values().toString()));
		LinkedHashMap<String, Character> map2 = curTrader.generateNextAction("00010", curTrader.trimString(curTrader.getStrategy().values().toString()));
		Assert.assertEquals(map.get("00000"), 'B', 0);
		Assert.assertEquals(map1.get("00001"), 'S', 0);
		Assert.assertEquals(map2.get("00010"), 'H', 0);
	}

}
