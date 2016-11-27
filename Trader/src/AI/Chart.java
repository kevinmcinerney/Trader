package AI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import TradingFloor.Stock;
import TradingFloor.Trader;

public class Chart {
	
	public Chart(){
		
	}
	
	public void createHillClimberChartData(){
		//System.out.println("I'm in createHCChartData method. ");
		
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
			
		Stock[] stocks = {aa,bb,cc,ee,ff,kk,ll,mm,oo,rr,ss,tt,uu}; //bb,cc,dd,ee,ff,gg,hh,ii,jj,kk,ll,mm,nn,oo,pp,qq,rr,ss,tt,uu
		
		File outputFile = new File("src/chartData/hillClimberChartData.txt");
		//System.out.println("path is: " + outputFile.getPath());
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
			for(int i = 0; i < 50; i++){
				Trader curTrader = new Trader("firstTrader",822511, stocks);
				
				HillClimber hc = new HillClimber(curTrader);
				curTrader = hc.search(curTrader);
				
				for(int x = 0; x < hc.getProfitChartArray().size(); x++){
					out.print(hc.getProfitChartArray().get(x) + ",");
				}
				
				out.println();
				
			}
			
			out.flush();
			out.close();
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		    
	
	}
	
	public static void main(String[] args){
		Chart ch = new Chart();
		ch.createHillClimberChartData();
	}

}
