package JUnitTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import AI.HillClimber;
import TradingFloor.Stock;
import TradingFloor.Trader;

public class TestMaxProfitFromHillClimber {

	@Test
	public void testMaxProfitFromHillClimberAGL() {
		System.out.println("Testing agl file");
		File aglFile = new File(Stock.class.getResource("/TestPackage/agl.csv").getFile());
		
		Stock agl = new Stock("agl", aglFile);
		
		Stock[] stks = {agl};
		
		// Set wallet to default R100,000 initially
		Trader curTrader = new Trader("firstTrader",822511, stks);
		
		HillClimber hc = new HillClimber(curTrader);
		curTrader = hc.search(curTrader);
		System.out.println("Final Profit "+(curTrader.getProfit()+"\nFinal Wallet " +curTrader.getWallet()));
		System.out.println("Max profit should be: " + ((agl.getRunningTotalsStockIncreases()/9975) * 822511));
		
		Assert.assertTrue(curTrader.getProfit() < ((agl.getRunningTotalsStockIncreases()/9975) * 822511));
	}
	
	@Test
	public void testMaxProfitFromHillClimberALSI() {
		System.out.println("Testing alsi file");
		File alsiFile = new File(Stock.class.getResource("/TestPackage/ALSI40.csv").getFile());
		
		Stock alsi = new Stock("alsi", alsiFile);
		
		Stock[] stks = {alsi};
		
		// Set wallet to default R100,000 initially
		Trader curTrader = new Trader("firstTrader",822511, stks);
		
		HillClimber hc = new HillClimber(curTrader);
		curTrader = hc.search(curTrader);
		System.out.println("Final Profit "+ (curTrader.getProfit() +"\nFinal Wallet " + curTrader.getWallet()));
		System.out.println("Max profit should be: " + ((alsi.getRunningTotalsStockIncreases()/6764) * 822511));
		System.out.println(alsi.getRunningTotalsStockIncreases()/6764);
		
		Assert.assertTrue(curTrader.getProfit() < ((alsi.getRunningTotalsStockIncreases()/6764) * 822511));
	}
	
	@Test
	public void testMaxProfitFromHillClimberBIL() {
		System.out.println("Testing bil file");
		File bilFile = new File(Stock.class.getResource("/TestPackage/bil.csv").getFile());
		
		Stock bil = new Stock("bil", bilFile);
		
		Stock[] stks = {bil};
		
		// Set wallet to default R100,000 initially
		Trader curTrader = new Trader("firstTrader",822511, stks);
		
		HillClimber hc = new HillClimber(curTrader);
		curTrader = hc.search(curTrader);
		System.out.println("Final Profit "+ (curTrader.getProfit() +"\nFinal Wallet " + curTrader.getWallet()));
		System.out.println("Max profit should be: " + ((bil.getRunningTotalsStockIncreases()/3535) * 822511));
		
		Assert.assertTrue(curTrader.getProfit() < ((bil.getRunningTotalsStockIncreases()/3535) * 822511));
	}
	
	@Test
	public void testMaxProfitFromHillClimberGFI() {
		System.out.println("Testing gfi file");
		File gfiFile = new File(Stock.class.getResource("/TestPackage/gfi.csv").getFile());
		
		Stock gfi = new Stock("gfi", gfiFile);
		
		Stock[] stks = {gfi};
		
		// Set wallet to default R100,000 initially
		Trader curTrader = new Trader("firstTrader",822511, stks);
		
		HillClimber hc = new HillClimber(curTrader);
		curTrader = hc.search(curTrader);
		System.out.println("Final Profit "+ (curTrader.getProfit() +"\nFinal Wallet " + curTrader.getWallet()));
		System.out.println("Max profit should be: " + ((gfi.getRunningTotalsStockIncreases()/5509) * 822511));
		
		Assert.assertTrue(curTrader.getProfit() < ((gfi.getRunningTotalsStockIncreases()/5509) * 822511));
	}

}
