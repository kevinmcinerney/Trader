package AI;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import TradingFloor.Stock;
import TradingFloor.Trader;

public class EvaluateStrategies {

	String line = "";
	String cvsSplitBy = "\t";
	BufferedReader br;
	SimpleDateFormat df;
	HashMap<String, String> uniqueStrategy = new HashMap<String, String>();

	public EvaluateStrategies() {
		this.br = new BufferedReader(new InputStreamReader(this.getClass()
				.getResourceAsStream("/TestPackage/GABestStrategies.csv")));
	}

	public void testStrategies() {

		File f1 = new File(Stock.class.getResource(
				"/TestPackage/testStock1.txt").getFile());
		Stock dd = new Stock("t1", f1);
		File f2 = new File(Stock.class.getResource(
				"/TestPackage/testStock2.txt").getFile());
		Stock gg = new Stock("t2", f2);
		File f3 = new File(Stock.class.getResource(
				"/TestPackage/testStock3.txt").getFile());
		Stock hh = new Stock("t3", f3);
		File f4 = new File(Stock.class.getResource(
				"/TestPackage/testStock4.txt").getFile());
		Stock ii = new Stock("t4", f4);

		File a = new File(Stock.class.getResource("/TestPackage/a.txt")
				.getFile());
		File b = new File(Stock.class.getResource("/TestPackage/b.txt")
				.getFile());
		File c = new File(Stock.class.getResource("/TestPackage/c.txt")
				.getFile());
		File e = new File(Stock.class.getResource("/TestPackage/e.txt")
				.getFile());
		File f = new File(Stock.class.getResource("/TestPackage/f.txt")
				.getFile());

		File k = new File(Stock.class.getResource("/TestPackage/k.txt")
				.getFile());
		File l = new File(Stock.class.getResource("/TestPackage/l.txt")
				.getFile());
		File m = new File(Stock.class.getResource("/TestPackage/m.txt")
				.getFile());

		File o = new File(Stock.class.getResource("/TestPackage/o.txt")
				.getFile());

		File r = new File(Stock.class.getResource("/TestPackage/r.txt")
				.getFile());
		File s = new File(Stock.class.getResource("/TestPackage/s.txt")
				.getFile());
		File t = new File(Stock.class.getResource("/TestPackage/t.txt")
				.getFile());
		File u = new File(Stock.class.getResource("/TestPackage/u.txt")
				.getFile());

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

		Stock[] stocks = { aa, bb, cc, ee, dd, gg, hh, ii, oo, rr, ss, tt, uu,
				ff, kk, ll, mm }; //

		// Stock[] trainingStocks = {aa,dd,oo,uu};
		// Stock[] testStocks = {uu, ii, gg, tt};
		HashMap<String, Double> lines = new HashMap<String, Double>();

		for (int indexy = 0; indexy < 30; indexy++) {
			Object[] masterStockArray = new Object[20];

			for (int j = 0; j < 20; j++) {
				Stock[] newStocks = new Stock[4];

				ArrayList<Integer> randomNumsChosen = new ArrayList<Integer>();

				int i = 0;
				while (i < 4) {
					Random randomNumber = new Random();
					int randomNum = randomNumber.nextInt((16 - 0) + 1) + 0;

					if (!randomNumsChosen.contains(randomNum)) {
						// System.out.println("randomNumChosen is " +
						// randomNum);
						randomNumsChosen.add(randomNum);
						newStocks[i] = stocks[randomNum];
						i++;
					}

					else {
						continue;
					}
				}

				/*
				 * for(Stock stk: newStocks){ System.out.println(stk.getName());
				 * }
				 */

				masterStockArray[j] = newStocks;
			}

			double total = 0;

			try {
				
				while ((line = this.br.readLine()) != null) {
					for (int j = 0; j < 25; j++) {
						int counter = 0;
						double[] profitsPerStrategy = new double[20];
						
						for (Object stkarr : masterStockArray) {
							Trader newTraderA = new Trader("newTrader", 822511,
									(Stock[]) stkarr);
							newTraderA.setStrategy(newTraderA.trimString(line));
							newTraderA.setMasterTableData();
							newTraderA.evaluateTrader();

							// System.out.println(newTraderA.getProfit()/100 +
							// "\t"+
							// newTraderA.trimString(newTraderA.getStrategy().values().toString()));
							profitsPerStrategy[counter++] = newTraderA
									.getProfit() / 100;
						}

						// call mean of double arr
						System.out
								.println(line
										+ "\t"
										+ getMean(profitsPerStrategy)
										+ "\t"
										+ getStandardDeviation(
												profitsPerStrategy,
												getMean(profitsPerStrategy))
										+ "\t"
										+ ((getMean(profitsPerStrategy) / getStandardDeviation(
												profitsPerStrategy,
												getMean(profitsPerStrategy)))));
						// call sds
						if (lines.containsKey(line)) {
							double temp = lines.get(line);
							temp += ((getMean(profitsPerStrategy) / getStandardDeviation(
									profitsPerStrategy,
									getMean(profitsPerStrategy))));
							lines.put(line, temp);
						} else {
							lines.put(
									line,
									((getMean(profitsPerStrategy) / getStandardDeviation(
											profitsPerStrategy,
											getMean(profitsPerStrategy)))));
							// System.out.println((getMean(profitsPerStrategy)/getStandardDeviation(profitsPerStrategy,
							// getMean(profitsPerStrategy))));
						}

					}

				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.println();
		Iterator it = lines.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + "\t" + pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}

	}

	public double getMean(double[] arr) {
		int total = 0;

		for (int i = 0; i < arr.length; i++) {
			total += arr[i]; // this is the calculation for summing up all the
								// values
		}

		double mean = total / arr.length;
		return mean;
	}

	public double getStandardDeviation(double[] arr, double mean) {

		double[] deviations = new double[arr.length];

		for (int i = 0; i < deviations.length; i++) {
			deviations[i] = arr[i] - mean;
		}

		double[] squares = new double[arr.length];

		for (int j = 0; j < squares.length; j++) {
			squares[j] = deviations[j] * deviations[j];
		}

		double sum = 0;
		for (int k = 0; k < squares.length; k++) {
			sum += squares[k];
		}

		double result = sum / (arr.length - 1);
		double standardDev = Math.sqrt(result);

		return standardDev;
	}

	public static void main(String[] args) {

		EvaluateStrategies ev = new EvaluateStrategies();
		System.out.println("Strategy\tCumulSharpeRatio");
		ev.testStrategies();

	}
}
