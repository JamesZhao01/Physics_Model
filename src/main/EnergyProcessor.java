package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import components.Planet;

public class EnergyProcessor {
	ArrayList<BigDecimal> nums = new ArrayList<BigDecimal>();
	public EnergyProcessor() {
		try(BufferedReader br = new BufferedReader(new FileReader("data345600.dat"))) {
			String str;
			while(((str = br.readLine()) != null)) {
				nums.add(new BigDecimal(str));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BigDecimal mean = new BigDecimal(0);
		for(int i = 0; i < nums.size(); i++) {
			mean = mean.add(nums.get(i).subtract(nums.get(0)).abs());
		}
		mean = mean.divide(new BigDecimal(nums.size() - 1), Planet.PREC);
		mean = mean.divide(nums.get(0), Planet.PREC);
		System.out.println(mean);
		System.out.println(mean.divide(nums.get(0), Planet.PREC).multiply(new BigDecimal(100)));
		
		
	}
	public static void main(String[] args) {
		new EnergyProcessor();
	}

}
