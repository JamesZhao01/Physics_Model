package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import components.Planet;

public class AngleProcessor {
	ArrayList<BigDecimal> nums = new ArrayList<BigDecimal>();
	public AngleProcessor() {
		try(BufferedReader br = new BufferedReader(new FileReader("dataA345600.dat"))) {
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
		System.out.println(mean);
		mean = mean.divide(nums.get(0), Planet.PREC).multiply(new BigDecimal(100));
		System.out.println(mean);
		
	}
	public static void main(String[] args) {
		new AngleProcessor();
	}

}
