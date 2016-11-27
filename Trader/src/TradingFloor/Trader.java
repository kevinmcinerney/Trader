package TradingFloor;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.TreeMap;


public class Trader implements Comparable<Trader>{
    
    /* Declare Trader-related variables */
    private String name;
    private double wallet;
    private double profit;
    // Declare linked hash map for binary representations (e.g. 00001, 11111) and characters
    // that represent decisions for a given pattern key (e.g. 'B', 'S', or 'H')
    private LinkedHashMap<String,Character> strategyMap;
    // HashMap that contains stocks as keys and number of stocks as values 
    private HashMap<Stock, Integer> stockPortfolio;
    private int numberOfTransactions;
    private double initialWallet = 0;
    private TreeMap<Date, HashMap<Character, ArrayList<StockContainer>>> masterTable;
    private double tradersScaledProfit = 0;
    private double rouletteSelectionFitness = 0;

	Stock[] testingStocks;
    

    /* Constructor for Trader instances */
    public Trader(String name, double wallet, Stock[] testingStocks){
        this.name = name;
        this.wallet = wallet;
        this.strategyMap = new LinkedHashMap<String,Character>();
        this.stockPortfolio = new HashMap<Stock,Integer>();
        this.initialWallet = wallet;
        this.testingStocks = testingStocks;
        this.masterTable = new TreeMap<Date, HashMap<Character, ArrayList<StockContainer>>>();
        this.setStrategy(this.randomlyGenerateStrategy()); 
        //this.setStrategy("SHBHBBBHHHBBBHHBHHBHHHHHHHBHHHBH");     				  
        this.setMasterTableData();   
    }
    
    /* Returns the hashmap representing stock portfolios */
    public HashMap<Stock, Integer> getStockPortfolio() {
        return stockPortfolio;
    }

    /* Sets stock portfolio hashmap that was passed in params */
    public void setStockPortfolio(HashMap<Stock, Integer> stockPortfolio) {
        this.stockPortfolio = stockPortfolio;
    }

    /* Setter for trader's profit */
    public void setProfit(double profit) {
        this.profit = profit;
    }

    /* Setter for strategy map */
    public void setStrategyMap(LinkedHashMap<String, Character> strategy) {
        this.strategyMap = strategy;
    }
    
    public Stock[] getTestingStocks() {
        return testingStocks;
    }
    
  
    public double getRouletteSelectionFitness() {
		return rouletteSelectionFitness;
	}
    
	public void setRouletteSelectionFitness(double rouletteSelectionFitness) {
		this.rouletteSelectionFitness = rouletteSelectionFitness;
	}
    
    public void setMasterTableData(){
    	
        this.masterTable = new TreeMap<Date, HashMap<Character, ArrayList<StockContainer>>>();
        
        for(Stock stock: this.testingStocks){ 
            for(int i = 0; i < stock.getRenkoStockData().size()-4; i++){
                
                String renkoPattern = "";
                renkoPattern += stock.getRenkoStockData().get(i).values().toArray()[0];
                renkoPattern += stock.getRenkoStockData().get(i+1).values().toArray()[0];
                renkoPattern += stock.getRenkoStockData().get(i+2).values().toArray()[0];
                renkoPattern += stock.getRenkoStockData().get(i+3).values().toArray()[0];
                renkoPattern += stock.getRenkoStockData().get(i+4).values().toArray()[0];
                // First date inserted in RenkoStockDataList will be date key at index 4 in
                // realStockData map - but since this corresponds to 'date1' when comparing
                // data for entry into Renko map, it will never be used, as 'date2' data will
                // always be inserted, that is, the latter date's data when comparing date1 and date2
                // cf. setRenkoStockData method for implementation.
                Date date = (Date)stock.getRenkoStockData().get(i+4).keySet().toArray()[0];                
                Character key = this.getStrategy().get(renkoPattern);
                StockContainer sc = new StockContainer(stock, i+4, 1,(stock.getRealStockData().get(date).get(1) + stock.getRealStockData().get(date).get(2))/2);
                
                if(key == 'H'){
                    continue;
                }
                
                if(this.masterTable.containsKey(date)){
                    if(this.masterTable.get(date).containsKey(key)){
                        this.masterTable.get(date).get(key).add(sc); 
                    }
                    else{
                    	ArrayList<StockContainer> stocklist = new ArrayList<StockContainer>();
                        stocklist.add(sc);
                        this.masterTable.get(date).put(key, stocklist);
                    }
                }
                else{
                    HashMap<Character, ArrayList<StockContainer>> innerMap = new HashMap<Character, ArrayList<StockContainer>>();
                    ArrayList<StockContainer> stocklist = new ArrayList<StockContainer>();
                    stocklist.add(sc);
                    innerMap.put(key, stocklist);
                    this.masterTable.put(date, innerMap);
                }
            }
        }
    }
    
    public double getTradersScaledProfit() {
		return tradersScaledProfit;
	}
    
	public void setTradersScaledProfit(double tradersScaledProfit) {
		this.tradersScaledProfit = tradersScaledProfit;
	}
    
    public TreeMap<Date, HashMap<Character, ArrayList<StockContainer>>> getMasterTable() {
        return masterTable;
    }
    
    /*
     * Calculates and returns number of stocks that trader can purchase based on
     * current wallet and accounting for overhead costs, too. Returns
     * -1 if it cannot afford to buy at least one stock, or stock price
     * is equal to or below zero (test datasets may contain zero price stocks).
     * 
     * */
    public int calculateNumberOfAffordableShares(double stockPriceOnThisDay){
    	
        if(wallet < stockPriceOnThisDay || stockPriceOnThisDay <= 0){
            return -1;
        }
        
        // First, calculate how many stocks can be purchased pre-overhead costs
        int hypotheticalNumberStocksToBuy = (int) (wallet / (stockPriceOnThisDay));
        
        // Get trade amount from multiplying number of stocks pre-overheads by stock price on this day
        double tradeAmount = hypotheticalNumberStocksToBuy * stockPriceOnThisDay;
        
        // Calculate transaction costs on this quoted amount
        double tempcost = getTransactionCosts(true, tradeAmount);
        
        // Now, calculate actual number of stocks to buy accounting for transaction costs
        int actualNumberStocksToBuy = (int) ((wallet - tempcost) / (stockPriceOnThisDay));
        //System.out.println("Actual no of stocks to buy: "+ actualNumberStocksToBuy);
        
        // Return final number of stocks that may be purchased
        return actualNumberStocksToBuy;
    }

    /* This method depends on calclateNumberOfAffordableShares method
     * to determine the maximum potential number of shares that may be bought with a
     * given wallet and stock price. It then purchases as many stocks as it can afford, adds 
     * these number of stocks to the trader's portfolio and subtracts the total costs 
     * from the trader's wallet.
     * 
     * */
    public boolean buy(Stock stock, double stockPriceOnThisDay){
    	
        // Stocks less than or equal to 0.00Euro cannot be purchased (CHECK)
        if(stockPriceOnThisDay <= 0){
            return false;
        }
        
        // Counter to keep track of number of stocks bought per transaction 
        int numberOfSharesBought = 0;
        
        // Store possible number of stocks to buy before transaction costs 
        int hypotheticalNumberStocksToBuy = (int) (wallet / (stockPriceOnThisDay)) ;
        
        // Get trade amount as possible no. stocks to buy times stock price on that day
        double tradeAmount = hypotheticalNumberStocksToBuy * stockPriceOnThisDay;
        
        // Calculate actual no. of stocks to buy incl. transaction costs
        int actualNumberStocksToBuy = calculateNumberOfAffordableShares(stockPriceOnThisDay);
        
        // Dont' make purchases of less than 4 stocks
        if(actualNumberStocksToBuy <= 4){
            return false;
        }
        
        // If trader can afford to buy stocks, subtract costs from current wallet
        wallet -= stockPriceOnThisDay * actualNumberStocksToBuy;
        wallet -= getTransactionCosts(true, tradeAmount);
        
        // Reset new profit after purchasing stocks
        this.setProfit(this.getWallet() - this.initialWallet);
        
        // Loop as many times as you have actual stocks left to purchase
        // solely to increment numberOfSharesBought value in stock portfolio
        int i = 0;
        while(i < actualNumberStocksToBuy){
            
            if(!stockPortfolio.containsKey(stock)){
                stockPortfolio.put(stock, ++numberOfSharesBought);
            }else{
                stockPortfolio.put(stock, ++numberOfSharesBought);
            }
            i++;
        }
        
        // Maximum stocks that can be purchased will be bought in one transaction 
        // thus lowering transaction costs. Precisely, each time the buy method is called
        // will increment numberOfTransactions by 1, if allowed to purchase > 1 stock.
        setNumberOfTransactions(getNumberOfTransactions() + 1);
        
        return true;
        
    }
    
    /* Method to sell given stock for a given stock at a given price.
     * Returns if trader has no stock to sell. If trader has stock in portfolio,
     * it will sell maximum stocks it has and accounting for transaction costs,
     * it will add its profit from selling to wallet. Again, selling all
     * its share will result in just one increment to numberOfTransactions variable, 
     * and also removing the stock entry from the trader's portfolio.
     * 
     * */
    public boolean sell(Stock stock, double stockPriceOnThisDay, boolean individualSaleTransaction){
        
        // Returns when trader has no stock to sell
        if(!stockPortfolio.containsKey(stock)){
            return false;
        }
                
        // Calculate amount made from selling number of stocks at certain stock price
        double tradeAmount = stockPriceOnThisDay * stockPortfolio.get(stock).intValue();
        
        // Add this profit to wallet
        wallet += tradeAmount;
        
        // without forgetting to subtract transaction costs
        wallet -= getTransactionCosts(false, tradeAmount);
        
        // Now, reset the profit 
        this.setProfit(this.getWallet() - this.initialWallet);
        
        // Decrement number of stocks in portfolio by one each time until 0
        while(stockPortfolio.containsKey(stock) && stockPortfolio.get(stock) > 0){
            stockPortfolio.put(stock, stockPortfolio.get(stock)-1);     
        }
        
        // Increment numberOfTransactions count by 1
        setNumberOfTransactions(getNumberOfTransactions() + 1);
        
        // Remove stock entry from portfolio
        if(individualSaleTransaction){
            stockPortfolio.remove(stock);
        }
        
        return true;
    }

    
    /* 
     * Calculate and return total transaction costs on a given
     * trade amount. Buying incurs additional costs, namely, Securities
     * Transfer Tax SST.
     * 
     * */
    public double getTransactionCosts(Boolean buying, double tradeAmount){
        
        double stt = 0;
        double brokerageFee = 0;
        double strate = 0;
        double ipl = 0;
        double vat = 0;
        
        // Securities Transfer Tax (SST)
        if(buying == true){
            stt = (.0025 * tradeAmount);
        }
        
        //Brokerage Fee
        brokerageFee = (.005 * tradeAmount);
        
        // Ensure a minimum brokerage fee of R70 converted to $
        if(brokerageFee < 576){
            brokerageFee = 576;
        }
        
        //STRATE tax in $
        strate = 95;
        
        //Investors Protection Levy
        ipl = (.000002 * tradeAmount);
        
        //VAT
        vat = 0.14 * (stt + brokerageFee + strate + ipl);
        
        // Return total cumulative cost of transaction
        return stt + brokerageFee + strate + ipl + vat;
        
    }

    /* Returns name of trader instance */
    public String getName() {
        return name;
    }

    /* Sets name of trader instance */
    public void setName(String name) {
        this.name = name;
    }

    /* Getter for trader wallet */
    public double getWallet() {
        return wallet;
    }

    /* Setter for trader wallet */
    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    /* Getter for trader's profit balance */
    public double getProfit() {
        return profit;
    }

    /* Getter for trader strategy */
    public LinkedHashMap<String, Character> getStrategy() {
        return strategyMap;
    }

    /* Create strategy hashmap for a generated binary representation pattern
     * e.g. 00001, 00010, 00011, etc. and the character representing a
     * decision to be made on encountering this pattern in the renko chart.
     * */
    public void setStrategy(String string) {
        for (int i = 0; i < 32; i++) {
            String line = "";
            for (int k = 5; k >= 0; k--) {
                line += ((i >> k) & 1) == 1 ? "1" : "0";
            }
            strategyMap.put(line.substring(1), string.charAt(i));
        }
    }
    
    /* Sets the overall return on investment (ROI) for a trader given a particular
     * strategy. It does this by looping through stocks available to this 
     * trader, and buying or selling or holding stocks depending on strategy.
     * 
     * */
    public void evaluateTrader(){
        
        /* For each stock, loop through stocks renko data.
         * Get renko pattern from last five days & perform chosen operation
         * on starting on the sixth day (B,H,S) and then everyday thereafter.
         * */
        for(Date date: this.masterTable.keySet()){  
        	
            HashMap<Character, ArrayList<StockContainer>> innerMap = this.masterTable.get(date);
            ArrayList<StockContainer> stocklist = null;
        
            if(!this.getStockPortfolio().isEmpty() && innerMap.containsKey('S')){
                stocklist = innerMap.get('S');   
                for(StockContainer sc: stocklist){
                	if(stockPortfolio.containsKey(sc.stock)){            
                		sc.setCurrentWallet(this.getWallet());
	                    double priceOnThisDay = sc.getCurrentStockPrice();
	                    this.sell(sc.stock, priceOnThisDay, true);      
                	}
                }   
            }
            
            // If there are stocks to be bought onthis day,but one
            if(innerMap.containsKey('B')){      
            	stocklist = new ArrayList<StockContainer>();
            	for(StockContainer sc: innerMap.get('B')){
            		sc.setCurrentWallet(this.getWallet());
            		stocklist.add(sc);
            		}
                
                int num = generateRandomInt(0,stocklist.size()-1);
                double priceOnThisDay = stocklist.get(num).getCurrentStockPrice();
                this.buy(stocklist.remove(num).stock, priceOnThisDay);   
             } 
        }
        
        // In case where trader still has assets leftover at the end of strategy,
        // perform sell action on these to get proper indication of profit/evaluation
        sellOffAssets(this);
        
    }
    
    /* Sell off assets at end of strategy for a proper 
     * evaluation of fitness */
    public void sellOffAssets(Trader trader){
        if(trader.getStockPortfolio().size() > 0){
            for(Stock s: trader.getStockPortfolio().keySet()){
                trader.sell(s,s.getLastStockPrice(), false);
            }
            trader.setStockPortfolio(null);
        }
        trader.stockPortfolio = new HashMap<Stock,Integer>();
    }

    /* Getter for numberOfTransactions */
    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    /* Setter for numberOfTransactions */
    public void setNumberOfTransactions(int numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }
    
    /* Generates random decision strategy - 32 character String -  for trader
     * useful for HillClimber algorithm */
    public String randomlyGenerateStrategy(){
        
        char[] possibleDecisions = {'B', 'S', 'H'};
        String randomStrategy = "";
        
        for(int i = 0; i < 32; i++){
            int randomChar = generateRandomInt(0,2);
            randomStrategy += possibleDecisions[randomChar];
        }   
        return randomStrategy; 
    }
    
    /* Generates random numbers within a given range */
    public static int generateRandomInt(int min, int max) {

        Random randomNumber = new Random();
        int randomNum = randomNumber.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    /*
     * Returns arraylist of neighbours which are
     * trader objects
     * */   
    public ArrayList<Trader> generateNeighbours() {
        
        Trader curTrader = this;
        String tempStrategy = "";
        
        // TempStrategy stores initial strategy at beginning
        for(Character c: curTrader.getStrategy().values()){
            tempStrategy += c;
        }
        
        // define strategy constant that will be immutable
        final String strategy = tempStrategy;
        
        // declare arraylist of traders for neighbours function
        ArrayList<Trader> neighbours = new ArrayList<Trader>();
        
        // initialize strategy hashmap 
        LinkedHashMap<String, Character> newStrategy = null;

        // Generate all possible neighbours (64) for a trader
        for(int i = 0; i < curTrader.getStrategy().size(); i++){
            for(int j = 0; j < 2; j++){
                if(j == 0)
                    newStrategy = this.generateNextAction(curTrader.getStrategy().keySet().toArray()[i].toString(), strategy);
                else{
                    tempStrategy = "";
                    for(Character c: newStrategy.values()){
                        tempStrategy += c;
                    }
                    newStrategy = this.generateNextAction(curTrader.getStrategy().keySet().toArray()[i].toString(), tempStrategy);
                
                }
                
                // Add new neighbour to neighbours list
                Trader newTrader = new Trader("anonTrader", 822511, curTrader.getTestingStocks());
                newTrader.setStrategyMap(newStrategy);
                newTrader.setStrategy(newTrader.trimString(newTrader.getStrategy().values().toString()));
                newTrader.setMasterTableData();
                
                neighbours.add(newTrader);
            }        
        }
        return neighbours;
    }
    
    /*
     * Generates, sets and returns the an alternate strategy based on hill-climbing approach
     * that alters the value of a given key (e.g. 00111) in a strategy. This is done systematically,
     * such that a 'B' char will always be changed to an 'S', an 'S' character will always be changed
     * to a 'H' and a 'H' char will consistently be changed to a 'B' character. 
     * */
    public LinkedHashMap<String, Character> generateNextAction(String key, String strategy){
        Trader neighbour = new Trader("anon",822511, this.getTestingStocks());
        neighbour.setStrategy(strategy);
        switch(neighbour.getStrategy().get(key)){
            case 'B': neighbour.getStrategy().put(key,'S');break;
            case 'S': neighbour.getStrategy().put(key,'H');break;
            case 'H': neighbour.getStrategy().put(key,'B');break;
        }
        return neighbour.getStrategy();
        
    }
    
    /* Tests whether the newly generated neighbour trader
     * has changed characters (from the original decision strategy)
     * in the correct index - which is passed in as a parameter. Returns true if it has correctly
     * changed characters at the correct index, and false if not.
     * 
     * */
    public Boolean compareStrings(String s1, String s2, int changedIndex){
        char[] string1arr = s1.toCharArray();
        char[] string2arr = s2.toCharArray();
        
        for(int i = 0; i < string1arr.length; i++){
            if(i == changedIndex && string1arr[i] == string2arr[i]){
                return false;
            }
            
            else if(i != changedIndex && string1arr[i] != string2arr[i]){
                return false;
            }
        }     
        return true;
    }
    
    /* Trims all square brackets, whitespaces and commas from a list
     * of values (String) returned from 'values' method of a HashMap
     * */
    public String trimString(String s){
        
        String finalString = "";      
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) != '[' && s.charAt(i) != ',' && s.charAt(i) != ']' && s.charAt(i) != ' '){
                finalString += s.charAt(i);
            }   
        }     
        return finalString;
    }
    
    // Compare traders based on profit
    public int compareTo(Trader otherTrader) {
        
        if(this.getProfit() > otherTrader.getProfit()){
            return 1;
        }
        
        else if (this.getProfit() < otherTrader.getProfit()){
            return -1;
        }
        return 0;
    }
    
    // Scales profit for certain selection methods
    public static double scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin, final  double limitMax){
		return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
	}
    
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
		
		File f1 = new File(Stock.class.getResource("/TestPackage/testStock1.txt").getFile());
		Stock t1 = new Stock("t1", f1);
		File f2 = new File(Stock.class.getResource("/TestPackage/testStock2.txt").getFile());
		Stock t2 = new Stock("t2", f2);
		File f3 = new File(Stock.class.getResource("/TestPackage/testStock3.txt").getFile());
		Stock t3 = new Stock("t3", f3);
		File f4 = new File(Stock.class.getResource("/TestPackage/testStock4.txt").getFile());
		Stock t4 = new Stock("t3", f4);
		
		File O1 = new File(Stock.class.getResource("/TestPackage/agl.csv").getFile());
		Stock S1 = new Stock("t1", f1);
		File O2 = new File(Stock.class.getResource("/TestPackage/ALSI40.csv").getFile());
		Stock S2 = new Stock("t2", f2);
		File O3 = new File(Stock.class.getResource("/TestPackage/bil.csv").getFile());
		Stock S3 = new Stock("t3", f3);
		File O4 = new File(Stock.class.getResource("/TestPackage/gfi.csv").getFile());
		Stock S4 = new Stock("t3", f4);
		
		//Stock[] stocks = {aa,bb,cc,ee,ff,kk,ll,mm,oo,rr,ss,tt,uu}; //bb,cc,dd,ee,ff,gg,hh,ii,jj,kk,ll,mm,nn,oo,pp,qq,rr,ss,tt,uu
		//Stock[] testStockArray = {t1,t2,t3,t4};
        Stock[] st = {S1,S2,S3,S4};
        
        Trader kevin = new Trader("Kevin", 822511, st);
        kevin.setMasterTableData();
        kevin.evaluateTrader();
        System.out.println(kevin.getProfit()/100);
		        
    } 
}
