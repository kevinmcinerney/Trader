package TradingFloor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Stock{
	
	/* Declare stock-related variables */
	private String name;
	private double lastStockPrice;
	private double binSize;
	private LinkedHashMap<Date, ArrayList<Double>> realStockData;
	private List<LinkedHashMap<Date, String>> renkoStockData;
	private double minClosingPrice;
	private double maxClosingPrice;
	private double runningTotalsStockIncreases;

	/* Constructor for Stock that takes in stock name and file as parameters */
	public Stock(String name, File stockFile){
		this.name = name;
		this.runningTotalsStockIncreases = 0;
		this.realStockData = new LinkedHashMap<Date,ArrayList<Double>>();
		this.renkoStockData = new ArrayList<LinkedHashMap<Date,String>>();
		this.minClosingPrice = Double.MAX_VALUE;
		this.maxClosingPrice = Double.MIN_VALUE;
		setRealStockData(stockFile);
		setBinSize();
		setRenkoStockData();
		
	}

	/* Getter for 'name' of Stock instance */
	public String getName() {
		return name;
	}

	/* Setter for 'name' of Stock object */
	public void setName(String name) {
		this.name = name;
	}

	/* Returns the most recent or last stock price entry in the file (associated with last given date) */
	public double getLastStockPrice() {
		return lastStockPrice;
	}

	/* Sets the last stock price on last date given in stock file */
	public void setLastStockPrice(double lastStockPrice) {
		this.lastStockPrice = lastStockPrice;
	}

	/* Returns the bin size of renko chart, i.e. predetermined amount of change in stock market */
	public double getBinSize() {
		return binSize;	
	}
	
	public double getRunningTotalsStockIncreases() {
		return runningTotalsStockIncreases;
	}

	public void setRunningTotalsStockIncreases(double runningTotalsStockIncreases) {
		this.runningTotalsStockIncreases = runningTotalsStockIncreases;
	}

	/* Calculates and sets the bin or box size by finding the maximum and minimum closing price
	 * of a stock and getting the difference, rounding to the nearest cent and getting 1%
	 * of this figure. */
	public void setBinSize() {
		Iterator<Entry<Date, ArrayList<Double>>> it = realStockData.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Date, ArrayList<Double>> pair = (Map.Entry<Date, ArrayList<Double>>)it.next();
	        
	        if(pair.getValue().get(0) < this.minClosingPrice){
	        	this.minClosingPrice = pair.getValue().get(0);
	        }
	        if(pair.getValue().get(0) > this.maxClosingPrice){
	        	this.maxClosingPrice = pair.getValue().get(0);
	        }
	    }
	    
	    this.binSize = Math.round((this.maxClosingPrice - this.minClosingPrice) * .01);
	}

	/* Returns the real stock data as a linked hashmap that contains the dates as keys
	 * and the closing price on this day, high price and low price as values in an arraylist. 
	 * Because it is a linked hash map, it also stores the keys in order. */
	public LinkedHashMap<Date, ArrayList<Double>> getRealStockData() {
		return realStockData;
	}

	/* Sets or populates the real stock data hashmap */
	public void setRealStockData(File csvFile) {
		
		String line = "";
		String cvsSplitBy = "\t";
		BufferedReader br;
		
		// Reads stock files from TestPackage package in existing project
		br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/TestPackage/" + csvFile.getName())));
		SimpleDateFormat df;
		try {
			while ((line = br.readLine()) != null) {
					if(line.trim().length() > 0){
					String[] stockTimeSlice = line.split(cvsSplitBy);	
					df = new SimpleDateFormat("dd/MM/yy");
					Date date = null;
					try {
						date = df.parse(stockTimeSlice[0]);
						ArrayList<Double> tempArray = new ArrayList<Double>();
						
						tempArray.add(Double.parseDouble(stockTimeSlice[1]));
						tempArray.add(Double.parseDouble(stockTimeSlice[2]));
						tempArray.add(Double.parseDouble(stockTimeSlice[3]));	
						
						// Puts the formatted date and arraylist of doubles in the realStockData hashmap
						//System.out.println(stockTimeSlice[0] + "\t" + (int)(Double.parseDouble(stockTimeSlice[1]) * 100) + "\t" + (int)(Double.parseDouble(stockTimeSlice[2]) * 100) + "\t" + (int)(Double.parseDouble(stockTimeSlice[3]) *100));
						realStockData.put(date, tempArray);	
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Retrieve and set last closing price of stock from last hashmap entry 
		ArrayList<Double> temp = new ArrayList<Double>();
		temp = (ArrayList<Double>) this.getRealStockData().values().toArray()[getRealStockData().size()-1];
		double price = (double) temp.get(0);
		this.setLastStockPrice(price);
		
	}

	/* Returns a list of linked hashmaps that represent renko chart data
	 * where keys are dates and values are '1' or '0' that represent whether it's a
	 * hollow brick (=1) or solid brick (=0) */
	public List<LinkedHashMap<Date, String>> getRenkoStockData() {
		return renkoStockData;
	}

	/* Populates renko stock data based on real stock data */
	public void setRenkoStockData() {
		// First, retrieve date from real stock data hashmap keyset
		Date date1 = (Date) realStockData.keySet().toArray()[0];
		// Next, retrieve corresponding closing stock price 
		double stockPrice1 = realStockData.get(date1).get(0);
		
		
		// Loop through realStockData and compare date's closing price with next date's closing price
	    for(int i = 1; i < realStockData.size(); i++) {
	    	// Retrieve next date to be compared. Initially, it will be date of second hashmap entry
	    	// 	of realStockData
	    	Date date2 = (Date) realStockData.keySet().toArray()[i];
	    	// Store closing price associated with this date (first in arraylist of doubles)
	    	double stockPrice2 = realStockData.get(date2).get(0);
	    	
	    	
	    	/* If stock price associated with date1 is significantly different from
	    	 * date2, that is, the absolute difference exceeds the predetermined bin size,
	    	 * then add date2 and the direction of change(1=up, 0=down) to the renkoStockData list
	    	 * */
	    	
	    	if((stockPrice2 - stockPrice1) > this.binSize){
	    		this.runningTotalsStockIncreases += (stockPrice2 - stockPrice1);
	    	}
	    	
	    	if(Math.abs(stockPrice1 - stockPrice2) > this.binSize){
	    		LinkedHashMap<Date,String> temp = new LinkedHashMap<Date,String>();
	    		
	    		/* Stock price of date1 is less than stock price of date2,
	    		 * therefore price has gone up, so the value inserted into hashmap
	    		 * will be '1'.*/
	    		if(stockPrice1 < stockPrice2){
	    			temp.put(date2, "1");
	    			renkoStockData.add(new LinkedHashMap<Date,String>(temp));
	    			// Now, store next date to be compared as date1
	    			date1 = (Date) realStockData.keySet().toArray()[i];
	    			// Get associated date of new date1
	    			stockPrice1 = realStockData.get(date1).get(0);
	    		}
	    		
	    		/* Stock price associated with date1 is significantly less than next date's
	    		 * stock price, therefore, price has gone down. This corresponds
	    		 * to a 'solid brick' which in the present implementation is 
	    		 * represented using a value of '0' in the renko hash map with key of date2.
	    		 * */
	    		else if (stockPrice2 < stockPrice1){
	    			temp.put(date2, "0");
	    			renkoStockData.add(new LinkedHashMap<Date,String>(temp));
	    			// Fetch next date1 to be compared to a date2
	    			date1 = (Date) realStockData.keySet().toArray()[i];
	    			stockPrice1 = realStockData.get(date1).get(0);
	    		}
	    		
	    		
	    	}
	    	/* When stockPrice1 and stockPrice2 do not significantly differ,
	    	 * keep date1 the same and increment date2, looking for the next significant date on which
	    	 * there was a change greater than brick size  */
	    } 
	}
	
	/* Return the minimum closing price for stock */
	public Double getMinClosingPrice() {
		return minClosingPrice;
	}

	/* Sets the minimum closing price for stock */
	public void setMinClosingPrice(Double minClosingPrice) {
		this.minClosingPrice = minClosingPrice;
	}

	/* Return maximum closing price for stock */
	public Double getMaxClosingPrice() {
		return maxClosingPrice;
	}

	/* Setter for maximum closing price of a stock */
	public void setMaxClosingPrice(Double maxClosingPrice) {
		this.maxClosingPrice = maxClosingPrice;
	}

	/* Set realStockData linked hash map */
	public void setRealStockData(LinkedHashMap<Date, ArrayList<Double>> realStockData) {
		this.realStockData = realStockData;
	}
	
	/* Main method for testing purposes */
	public static void main(String args[]) {
		/*File aglFile = new File(Stock.class.getResource("/TestPackage/agl.csv").getFile());
		File ALSI40file = new File(Stock.class.getResource("/TestPackage/ALSI40.csv").getFile());
		File bilfile = new File(Stock.class.getResource("/TestPackage/bil.csv").getFile());
		File gfifile = new File(Stock.class.getResource("/TestPackage/gfi.csv").getFile());
		
		
		
		Stock agl = new Stock("agl", aglFile);
		Stock ALSI40 = new Stock("ALSI40",ALSI40file);
		Stock bil = new Stock("bil",bilfile);
		Stock gfi = new Stock("gfi",gfifile);*/
		
		//File a = new File(Stock.class.getResource("/TestPackage/a.txt").getFile());
		//File b = new File(Stock.class.getResource("/TestPackage/b.txt").getFile());
		//File c = new File(Stock.class.getResource("/TestPackage/c.txt").getFile());
		//File d = new File(Stock.class.getResource("/TestPackage/d.txt").getFile());
		//File e = new File(Stock.class.getResource("/TestPackage/e.txt").getFile());
		//File f = new File(Stock.class.getResource("/TestPackage/f.txt").getFile());
		
		//File k = new File(Stock.class.getResource("/TestPackage/k.txt").getFile());
		//File l = new File(Stock.class.getResource("/TestPackage/l.txt").getFile());
		//File m = new File(Stock.class.getResource("/TestPackage/m.txt").getFile());
		
		//File o = new File(Stock.class.getResource("/TestPackage/o.txt").getFile());
		//File r = new File(Stock.class.getResource("/TestPackage/r.txt").getFile());
		//File s = new File(Stock.class.getResource("/TestPackage/s.txt").getFile());
		//File t = new File(Stock.class.getResource("/TestPackage/t.txt").getFile());
		//File u = new File(Stock.class.getResource("/TestPackage/u.txt").getFile());
		
		//Stock aa = new Stock("a", a);
		//Stock bb = new Stock("b", b);
		//Stock cc = new Stock("c", c);
		//Stock dd = new Stock("d", d);
		//Stock ee = new Stock("e", e);
		//Stock ff = new Stock("f", f);
		
		//Stock kk = new Stock("k", k);
		//Stock ll = new Stock("l", l);
		//Stock mm = new Stock("m", m);
		
		//Stock oo = new Stock("o", o);
		
		//Stock rr = new Stock("r", r);
		//Stock ss = new Stock("s", s);
		//Stock tt = new Stock("t", t);
		//Stock uu = new Stock("u", u);
		
		
		
		//Trader kevin = new Trader("Kevin",10000000,"HSBSBBHSSHHHSSSBSSSHSSSSSSSSSSBH");
		
		//kevin.testTrader(new Stock[]{agl,ALSI40,bil,gfi});		
	}
	
}
