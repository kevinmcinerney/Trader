package AI;

import java.io.File;
import java.util.Arrays;
import AI.HillClimber;
import TradingFloor.Stock;
import TradingFloor.Trader;

public class GeneticAlgorithm {
	
	private int popSize, maxGen, selectionType, crossoverType;
	private double mutationRate, crossOverP, GenGap, tournamentP;
	private Trader[] population;
	private Stock[] stocks;
	boolean lowToMed, lowToHigh, changeCrossOverType;
	
	/*
	 * Selection Type: 1) random 2) tournament 3) fitness-proportional selection 4) rank-based selection

	 * CrossoverType: 1) single-point 2) two-point
	 * 
	 */
	public GeneticAlgorithm(int popSize, int maxGen, int selectionType, int crossoverType, double mutationRate,
			double crossOverP, double GenGap, double tournamentP, boolean changeCrossOverType, boolean lowToMed,
			boolean lowToHigh){
		
		// Stocks is US Dollars
		File a = new File(Stock.class.getResource("/TestPackage/a.txt").getFile());
		File u = new File(Stock.class.getResource("/TestPackage/u.txt").getFile());
		File o = new File(Stock.class.getResource("/TestPackage/o.txt").getFile());
		File f1 = new File(Stock.class.getResource("/TestPackage/testStock1.txt").getFile());
		
		//Stocks in US Dollars
		Stock dd = new Stock("t2", f1);
		Stock oo = new Stock("o", o);
		Stock aa = new Stock("a", a);
		Stock uu = new Stock("u", u);
		
		/*File b = new File(Stock.class.getResource("/TestPackage/b.txt").getFile());
		File c = new File(Stock.class.getResource("/TestPackage/c.txt").getFile());
		File e = new File(Stock.class.getResource("/TestPackage/e.txt").getFile());
		File f = new File(Stock.class.getResource("/TestPackage/f.txt").getFile());		
		File k = new File(Stock.class.getResource("/TestPackage/k.txt").getFile());
		File l = new File(Stock.class.getResource("/TestPackage/l.txt").getFile());
		File m = new File(Stock.class.getResource("/TestPackage/m.txt").getFile());	
		File r = new File(Stock.class.getResource("/TestPackage/r.txt").getFile());
		File s = new File(Stock.class.getResource("/TestPackage/s.txt").getFile());
		File t = new File(Stock.class.getResource("/TestPackage/t.txt").getFile());
		File f2 = new File(Stock.class.getResource("/TestPackage/testStock2.txt").getFile());
		File f3 = new File(Stock.class.getResource("/TestPackage/testStock3.txt").getFile());
		File f4 = new File(Stock.class.getResource("/TestPackage/testStock4.txt").getFile());*/
		
		/*Stock bb = new Stock("b", b);
		Stock cc = new Stock("c", c);
		Stock ee = new Stock("e", e);
		Stock ff = new Stock("f", f);
		Stock kk = new Stock("k", k);
		Stock ll = new Stock("l", l);
		Stock rr = new Stock("r", r);
		Stock ss = new Stock("s", s);
		Stock tt = new Stock("t", t);
		Stock mm = new Stock("m", m);
		Stock ii = new Stock("t1", f4);
		Stock gg = new Stock("t3", f2);
		Stock hh = new Stock("t4", f3);*/
		
		
		// Test Stocks in US Dollars
		Stock[] stocks = {aa,dd,oo,uu}; // aa,bb,cc,ee,dd,gg,hh,ii,oo,rr,ss,tt,uu
		
		this.stocks = stocks;
		this.popSize = popSize; 
		this.maxGen = maxGen; 
		this.selectionType = selectionType; 
		this.crossoverType = crossoverType;
		this.mutationRate = mutationRate;
		this.crossOverP = crossOverP;
		this.GenGap = GenGap;
		this.tournamentP = tournamentP;
		this.population = new Trader[popSize];
		this.lowToHigh = lowToMed;
		this.lowToHigh = lowToHigh;
		this.changeCrossOverType = changeCrossOverType;
		for(int x = 0; x < popSize; x++){
			population[x] = new Trader("anon", 822511, this.stocks);
			population[x].setStrategy(population[x].randomlyGenerateStrategy());
			population[x].setMasterTableData();
			population[x].evaluateTrader();
		}
	}
	
	// Start Genetic Optimization Search
	private void search(){
		
		int genCount = 0;
		while(genCount < maxGen){
			
			// Reinitialize population for each new generation
			if(genCount != 0){
				for(int w = 0; w < popSize; w++){
					String prevStrategy = population[w].trimString(population[w].getStrategy().values().toString());
					population[w] = new Trader("anon", 822511, this.stocks);
					population[w].setStrategy(prevStrategy);
					population[w].setMasterTableData();
					population[w].evaluateTrader();
				}
			}
			
			// Select parents from the current generation
			switch(selectionType){
				case 1: this.randomSelection(population); break;
				case 2: this.tournamentSelection(population, (int)(popSize*tournamentP));break;
				case 3: this.fitnessProportionalSelection(population); break;
				case 4: this.rankBasedSelection(population); break;
			}
			
			// Mutate the population
			mutate(population);
			
			// Converge selection pressure, if necessary
			if(lowToMed && genCount % 100 == 0){
				this.mutationRate -= .005;
				this.crossOverP -= .04;
				this.GenGap += .0075;
				this.tournamentP += .045;	
			}
			
			// Converge selection pressure, if necessary
			if(lowToHigh && genCount % 100 == 0){
				this.mutationRate -= .019;
				this.crossOverP -= .09;
				this.GenGap += .0975;
				this.tournamentP += .095;
			}
			
			// Change Crossever type from double to single, if necessary
			if(changeCrossOverType && genCount == 500){
				this.crossoverType = 1;
			}
			
			genCount++;
			
			Arrays.sort(population);
			
			// See Profits Growing of most profitable trader from each generation
			System.out.println(population[99].getProfit());
		}	
	}

	private void rankBasedSelection(Trader[] population) {
		System.out.println("Rank");
		
	}

	/* Scales fitness values to ensure a positive representation
	 * of each selection probability for each individual on the graph.
	 * 
	 * */
	private void fitnessProportionalSelection(Trader[] population) {
		
		Arrays.sort(population);
		
		// Initialize array of traders to be replaced in current generation
		Trader[] deadTraders = new Trader[(int)(this.GenGap * this.popSize)];
		
		// Dead traders will the first ones inthe sorted population
		for(int j = 0; j < (int)(this.GenGap * this.popSize); j++){		
			deadTraders[j] = population[j];
		}
		
		// Initialize array of parents for new children in population
		Trader[] chosenParents = new Trader[(int)(this.GenGap * this.popSize)];
		
		// 1) Scale traders' profits between 0 and 1 to avoid negative numbers
		// 2) Add up the total profit of all tradersin population
		double runningTotalDenominator = 0;	
		for(int i = 0; i < population.length; i++){
			double traderScaledProfit = population[i].scale(population[i].getProfit(), -822511, 10000000, 0, 1);
			runningTotalDenominator += traderScaledProfit;
			population[i].setTradersScaledProfit(traderScaledProfit);
			
		}
		
		// Set proportional fitness as ratio of profit to total profit of population
		double totalFitnessSelectionProbabilities = 0;
		for(int i = 0; i < population.length; i++){
			totalFitnessSelectionProbabilities += population[i].getTradersScaledProfit() / runningTotalDenominator;
			population[i].setRouletteSelectionFitness(totalFitnessSelectionProbabilities);
		}
		
		
		// Choose parents using there proportional fitness
		for(int k = 0; k < (int)(this.GenGap * this.popSize); k++){
			double randNumber = 0 + Math.random() * ((1 - 0));
			for(int i = 0; i < population.length; i++){
				if(randNumber <= population[i].getRouletteSelectionFitness()){
					chosenParents[k] = population[i];
					break;
				}
				else if(i == (population.length - 1)){
					chosenParents[k] = population[i];
					break;
				}
			}
		}
		
		// Is the generation gap odd?
		Boolean odd = (int)(this.GenGap * this.popSize) % 2 != 0;
		
		// Do Cross over if probability says to do so
		for(int i = 0; i < (int)(this.GenGap * this.popSize); i+=2){
			if(Math.random() > crossOverP)
				break;
			
			// In odd generation gaps, one parent gets to mate twice
			if(odd && i == (int)(this.GenGap * this.popSize)-1){
				this.crossOver(chosenParents[chosenParents.length-1], chosenParents[chosenParents.length-2], i, i+1, this.crossoverType);
				break;
			}
			
			//Crossover call
			this.crossOver(chosenParents[i], chosenParents[i+1], i, i+1, this.crossoverType);
		}
	}

	private void tournamentSelection(Trader[] population, int tournamentSize) {

		// Stores best (most profitable) trader so far
		Trader bestTraderSoFar = new Trader("anonymous", 822511, this.stocks);	
		
		// Initialize array for choosen parents of new children
		Trader[] chosenParents = new Trader[(int)(this.GenGap * this.popSize)];
		
		// Until generationgap is filled. keep selecting a best trader from 
		// a sample of k traders in the population
		for(int k = 0; k < (int)(this.GenGap * this.popSize); k++){
			Trader[] parents = new Trader[tournamentSize];
			bestTraderSoFar.setProfit(Double.NEGATIVE_INFINITY);
			
			for(int i = 0; i < tournamentSize; i++){
				int randIdx = 0 + (int)(Math.random() * (((population.length-1) - 0) + 1));
				parents[i] = population[randIdx];
				if(parents[i].compareTo(bestTraderSoFar) > 0){
					bestTraderSoFar.setProfit(parents[i].getProfit());
					chosenParents[k] = parents[i];
				}
			}	
		}
			
		Arrays.sort(population);
		
		// Is the generation gap odd? 
		Boolean odd = (int)(this.GenGap * this.popSize) % 2 != 0;
		
		// Do crossover if the probability allows it
		for(int i = 0; i < (int)(this.GenGap * this.popSize); i+=2){
			if(Math.random() > crossOverP)
				continue;
			
			if(odd && i == (int)(this.GenGap * this.popSize)-1){
				this.crossOver(chosenParents[chosenParents.length-1], chosenParents[chosenParents.length-2], i, i, this.crossoverType);
				break;
			}

			// Call crossover
			this.crossOver(chosenParents[i], chosenParents[i+1], i, i+1, this.crossoverType);
		}	
	}

	private void crossOver(Trader parent1, Trader parent2, int deadTrader1, int deadTrader2, int crossoverType) {
		
		// Get both straties of parents
		String parent1Strat = parent1.trimString(parent1.getStrategy().values().toString());
		String parent2Strat = parent2.trimString(parent2.getStrategy().values().toString());
		
		// Generate random crossover point
		int randCrossOverPoint1 = 0 + (int)(Math.random() * ((parent1Strat.length() - 1 - 0) + 1));
		
		// Initialize new strategy strings
		String child1 = "";
		String child2 = "";
		
		// Single crossover point 
		if(crossoverType >= 1){
			child1 = parent1Strat.substring(0, randCrossOverPoint1) + parent2Strat.substring(randCrossOverPoint1);
			child2 = parent2Strat.substring(0, randCrossOverPoint1) + parent1Strat.substring(randCrossOverPoint1);
			population[deadTrader1] = new Trader("anon", 822511, this.stocks);
			population[deadTrader1].setStrategy(child1);
			population[deadTrader2] = new Trader("anon", 822511, this.stocks);
			population[deadTrader2].setStrategy(child2);
		}

		
		// Double crossover point
		if(crossoverType > 1){
			int randCrossOverPoint2 = randCrossOverPoint1 + (int)(Math.random() * ((parent1Strat.length() - 1 - randCrossOverPoint1) + 1));
			child1 = child1.substring(0, randCrossOverPoint2) + child2.substring(randCrossOverPoint2);
			child2 = child2.substring(0, randCrossOverPoint2) + child1.substring(randCrossOverPoint2);
			population[deadTrader1] = new Trader("anon", 822511, this.stocks);
			population[deadTrader1].setStrategy(child1);
			population[deadTrader2] = new Trader("anon", 822511, this.stocks);
			population[deadTrader2].setStrategy(child2);
		}
	}

	
	private void randomSelection(Trader[] population) {
		
		// Initialize array for parents of new children
		Trader[] chosenParents = new Trader[(int)(this.GenGap * this.popSize)];
		
		// Select random parents
		for(int k = 0; k < (int)(this.GenGap * this.popSize); k++){
			int randIdx = 0 + (int)(Math.random() * (((population.length-1) - 0) + 1));
			chosenParents[k] = population[randIdx];
		}
		
		Arrays.sort(population);
		
		// Isgeneration gap odd?
		Boolean odd = (int)(this.GenGap * this.popSize) % 2 != 0;
		
		// Do cross over as appropriate
		for(int i = 0; i < (int)(this.GenGap * this.popSize); i+=2){
			if(Math.random() > crossOverP)
				break;
			
			if(odd && i == (int)(this.GenGap * this.popSize)-1){
				this.crossOver(chosenParents[chosenParents.length-1], chosenParents[chosenParents.length-2], i, i+1, this.crossoverType);
				break;
			}
		
			// Call crossover
			this.crossOver(chosenParents[i], chosenParents[i+1], i, i+1, this.crossoverType);
		}
	}
	
	private void mutate(Trader[] population){
		
		// How many mutations required
		int numberOfMutations = (int) Math.round(this.mutationRate * population.length);
		
		
		while(numberOfMutations > 0){
			
			// Rndom number for mutating part of strategy
			int randomNumber = (int)(Math.random() * ((population.length -1) + 1));
			int randomIndexInStrategy = 0 + (int)(Math.random() * ((31 - 0) + 1));
			
			
			String strategyToBeMutated = (population[randomNumber].trimString(population[randomNumber].getStrategy().values().toString()));
			String mutatedStragety = "";
			
			// Carry out mutation
			for(int i = 0; i < strategyToBeMutated.length(); i++){
				if(i == randomIndexInStrategy){
					switch(strategyToBeMutated.charAt(randomIndexInStrategy)){
						case 'B': mutatedStragety += (Math.random() > 0.5) ? 'S' : 'H';break;
						case 'S': mutatedStragety += (Math.random() > 0.5) ? 'B' : 'H';break;
						case 'H': mutatedStragety += (Math.random() > 0.5) ? 'B' : 'S';break;
					}	
				}
				else{
					mutatedStragety += strategyToBeMutated.charAt(i);
				}
			}
			// Set strategy with new mutation
			population[randomNumber].setStrategy(mutatedStragety);
			numberOfMutations--;
		}
	}
	
	public static void main(String args[]){
		
		//1) popSize, 2) maxGen, 3) selectionType, 4) crossoverType, 5)mutationRate, 6)crossOverP, 7) GenGap, 8) tournamentSize) 9)changeCrossOverType 10)lowToMed,
		// 11)lowToHigh
		
		/*int popSize, int maxGen, int selectionType, int crossoverType, double mutationRate,
			double crossOverP, double GenGap, double tournamentP, boolean changeCrossOverType, boolean lowToMed,
			boolean lowToHigh*/
		
		int popSize = Integer.parseInt(args[0]);
		int maxGen = Integer.parseInt(args[1]);
		int selectionType = Integer.parseInt(args[2]);
		int crossOverType = Integer.parseInt(args[3]);
		double mutationRate = Double.parseDouble(args[4]);
		double crossOverP = Double.parseDouble(args[5]);
		double genGap = Double.parseDouble(args[6]);
		double tournamentSize = Double.parseDouble(args[7]);
		boolean changeCrossOverType = Boolean.valueOf(args[8]);
		boolean lowToMed = Boolean.valueOf(args[9]);
		boolean lowToHigh = Boolean.valueOf(args[10]);
		
		
		GeneticAlgorithm ga1  = new GeneticAlgorithm(popSize, maxGen, selectionType, crossOverType, mutationRate, crossOverP, genGap, tournamentSize, changeCrossOverType, lowToMed, lowToHigh);
		ga1.search();

		
	}
}
