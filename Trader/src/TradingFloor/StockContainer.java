package TradingFloor;


// A container class to hold stocks belonging to particular pont in time
public class StockContainer implements Comparable<StockContainer>{
	
	Stock stock;
	private int currentRenkoSize;
	private double currentWallet;
	private double currentStockPrice;
	
	public StockContainer(Stock stock, int currentRenkoSize, double currentWallet, double currentStockPrice){
		
		this.stock = stock;
		this.currentRenkoSize = currentRenkoSize;
		this.currentWallet = currentWallet;
		this.currentStockPrice = currentStockPrice;
	}
	
	

	public double getCurrentStockPrice() {
		return currentStockPrice;
	}

	public double getCurrentWallet() {
		return currentWallet;
	}
	
	public void setCurrentWallet(double currentWallet) {
		this.currentWallet = currentWallet;
	}

	public Stock getStock() {
		return stock;
	}

	
	// Stocks are sorted according to the amount of change they leave behind in your wallet, if bought.
	// Stocks which leave less change are favoured.
	// If there is a tie, stocks with more volatility are favoured. Volatility must
	// be different by 10% and is measured as the size of the renko chart of that stock
	// up to this point in time.
	@Override
	public int compareTo(StockContainer o) {
		
		if((currentWallet % currentStockPrice) > (o.currentWallet % o.currentStockPrice))
			return 1; //1
		else if((currentWallet % currentStockPrice) < (o.currentWallet % o.currentStockPrice))
			return -1; //-1			
	
		else{
			if(this.currentRenkoSize < o.currentRenkoSize*.9)
				return -1; //1
			else if (.9 * this.currentRenkoSize > o.currentRenkoSize)
				return 1; //-1
			return 0; //0
		}
	}
}
