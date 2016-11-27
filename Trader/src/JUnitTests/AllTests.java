package JUnitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestBinSize.class, TestBuyAction.class, TestBuySell.class,
		TestCorrestNumberOfNeighbours.class, TestGenerateCorrectAction.class,
		TestHillClimber.class, TestRealChartData.class, TestRenkoChart.class,
		TestSellAction.class, TestSellOffAssets.class,
		TestTransactionCostsForBuying.class,
		TestTransactionCostsForSelling.class, TestMaxProfitFromHillClimber.class, })
public class AllTests {

}
