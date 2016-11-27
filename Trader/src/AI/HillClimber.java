package AI;

import java.io.File;
import java.util.ArrayList;

import TradingFloor.Stock;
import TradingFloor.Trader;

public class HillClimber {

	/* Declare hill-climber related variables */
	Trader startTrader;
	Stock[] stocks;
	ArrayList<Object> profitChartArray = new ArrayList<Object>();

	/* Constructor for a HillClimber instance */
	public HillClimber(Trader trader) {
		this.startTrader = trader;
		
		// Generate random decision strategy for this trader
		trader.setStrategy(trader.randomlyGenerateStrategy());
		
		// Uses stocks from test data stored in TestPackage java package
		
		this.setStocks(trader.getTestingStocks());
		
	}
	
	public Stock[] getStocks() {
		return stocks;
	}


	public void setStocks(Stock[] stocks) {
		this.stocks = stocks;
	}
	
	public ArrayList<Object> getProfitChartArray() {
		return profitChartArray;
	}

	public void setProfitChartArray(ArrayList<Object> profitChartArray) {
		this.profitChartArray = profitChartArray;
	}

	
	/**
	 * Performs basic hill-climbing algorithm
	 * 
	 * @param trader
	 * @return Trader  - most profitable trader
	 */
	public Trader search(Trader trader) {
		
		// define variable that stores current highest profit from a trader
		// and is initialized to the value returned by Double.longBitsToDouble(0xfff0000000000000L)
		double currentBestProfitFromATrader = Double.NEGATIVE_INFINITY;

		Trader curTrader = trader;
		curTrader.evaluateTrader();
		Trader nextTrader = null;
		// Use flag to indicate whether a more profitable trader has been detected
		Boolean foundBetterTrader;
		
		do {
			foundBetterTrader = false;
			ArrayList<Trader> neighbours = curTrader.generateNeighbours();
			for (Trader tr : neighbours) {
				tr.evaluateTrader();
				// If trader being tested has higher ROI than at present,
				// set currentBestProfitFromATrader equal to that trader's ROI
				// and carry on looking for a further profitable trader
				if ((tr.getProfit() + tr.getWallet()) > currentBestProfitFromATrader) {
					nextTrader = tr;
					currentBestProfitFromATrader = tr.getProfit() + tr.getWallet();
					profitChartArray.add(currentBestProfitFromATrader/100);
					// Print out profitable strategy detected
					// Assign profitable trader to curTrader
					curTrader = tr;
					// Update flag variable
					foundBetterTrader = true;
					// Break out of for-loop and re-generate neighbours for new best trader
					break;
				}
			}
			// Stopping condition: If we have not found a better neighbour/trader, return last best trader since testing neighbours
			if (!foundBetterTrader){
				// Return nextTrader since no better neighbors exist
				profitChartArray.add(nextTrader.trimString(nextTrader.getStrategy().values().toString()));
				return nextTrader;
			}		
		} while (true);
	}

	
	/* Main method for testing HillClimber algorithm before JUnit testing */
	public static void main(String[] args){
		
		File a = new File(Stock.class.getResource("/TestPackage/a.txt").getFile());
		File b = new File(Stock.class.getResource("/TestPackage/b.txt").getFile());
		File c = new File(Stock.class.getResource("/TestPackage/c.txt").getFile());
		File e = new File(Stock.class.getResource("/TestPackage/e.txt").getFile());
		File f = new File(Stock.class.getResource("/TestPackage/f.txt").getFile());
		
		File k = new File(Stock.class.getResource("/TestPackage/k.txt").getFile());
		File l = new File(Stock.class.getResource("/TestPackage/l.txt").getFile());
		File m = new File(Stock.class.getResource("/TestPackage/m.txt").getFile());
		File o = new File(Stock.class.getResource("/TestPackage/o.txt").getFile());
		
		File r = new File(Stock.class.getResource("/TestPackage/r.txt").getFile());
		File s = new File(Stock.class.getResource("/TestPackage/s.txt").getFile());
		File t = new File(Stock.class.getResource("/TestPackage/t.txt").getFile());
		File u = new File(Stock.class.getResource("/TestPackage/u.txt").getFile());
		
		Stock aa = new Stock("a", a);
		Stock bb = new Stock("b", b);
		Stock cc = new Stock("c", c);
		Stock ee = new Stock("e", e);
		Stock ff = new Stock("f", f);
		
		Stock kk = new Stock("k", k);
		Stock ll = new Stock("l", l);
		Stock mm = new Stock("m", m);
		
		Stock oo = new Stock("o", o);
		
		Stock rr = new Stock("r", r);
		Stock ss = new Stock("s", s);
		Stock tt = new Stock("t", t);
		Stock uu = new Stock("u", u);
		
		File aglFile = new File(Stock.class.getResource("/TestPackage/agl.csv").getFile());
        File ALSI40file = new File(Stock.class.getResource("/TestPackage/ALSI40.csv").getFile());
        File bilfile = new File(Stock.class.getResource("/TestPackage/bil.csv").getFile());
        File gfifile = new File(Stock.class.getResource("/TestPackage/gfi.csv").getFile());
        
        Stock agl = new Stock("agl", aglFile);
        Stock ALSI40 = new Stock("ALSI",ALSI40file);
        Stock bil = new Stock("bil",bilfile);
        Stock gfi = new Stock("gfi",gfifile);
        
    	File f1 = new File(Stock.class.getResource("/TestPackage/testStock1.txt").getFile());
		Stock dd = new Stock("t1", f1);
		File f2 = new File(Stock.class.getResource("/TestPackage/testStock2.txt").getFile());
		Stock gg = new Stock("t2", f2);
		File f3 = new File(Stock.class.getResource("/TestPackage/testStock3.txt").getFile());
		Stock hh = new Stock("t3", f3);
		File f4 = new File(Stock.class.getResource("/TestPackage/testStock4.txt").getFile());
		Stock ii = new Stock("t3", f4);
	
				
		Stock[] stocks = {aa,bb,cc,ee, dd, gg, hh, ii,oo,rr,ss,tt,uu, ff,kk,ll,mm}; //bb,cc,dd,ee,ff,gg,hh,ii,jj,kk,ll,mm,nn,oo,pp,qq,rr,ss,tt,uu
		
		Stock[] trainingStocks = {bb, cc, ll, mm};
		Stock[] testStocks = {uu, ii, gg, tt};
	
		
		double totalProfit = 0;
		
		System.out.println("2003-2008 \t Priority \t New Stocks");
		System.out.println("Profit\tStrategy");
		for(int x = 0; x < 10; x++){
			//System.out.println(".");
			// Set wallet to default R100,000 (in $) initially
			
			
			Trader curTrader = new Trader("firstTrader",822511,trainingStocks);
			HillClimber hc = new HillClimber(curTrader);
			curTrader = hc.search(curTrader);
			
			System.out.println(((curTrader.getProfit())/100) +"\t"+curTrader.trimString((curTrader.getStrategy().values().toString())));
			totalProfit += curTrader.getProfit();
		}
		
		System.out.println("Average profit = R" + (totalProfit/1000));
		
		
	}

}
