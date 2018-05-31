package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import components.Planet;

public class SD {
	ArrayList<BigDecimal> nums = new ArrayList<BigDecimal>();
	public SD() {
		String s = "";
		s = JOptionPane.showInputDialog("This program will compute the average difference from the mean, as a percent. \nFile Name:");
		try(BufferedReader br = new BufferedReader(new FileReader(s))) {
			String str;
			while(((str = br.readLine()) != null)) {
				nums.add(new BigDecimal(str));
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(s == null) {
			System.out.println("Exited Dialog!");
			System.exit(0);
		}
		
		
		BigDecimal mean = BigDecimal.ZERO.setScale(Planet.PREC.getPrecision(), Planet.PREC.getRoundingMode());
		for(int i = 0; i < nums.size(); i++) {
			mean = mean.add(nums.get(i));
		}
		mean = mean.divide(new BigDecimal(nums.size()), Planet.PREC);
		
		BigDecimal sum = BigDecimal.ZERO.setScale(Planet.PREC.getPrecision(), Planet.PREC.getRoundingMode());
		
		for(int i = 0; i < nums.size(); i++) {
			sum = sum.add(nums.get(i).subtract(mean).abs());
		}
		
		sum = sum.divide(new BigDecimal(nums.size() - 1), Planet.PREC);
		sum = sum.divide(mean, Planet.PREC).multiply(new BigDecimal(100));
		
		System.out.println(s);
		System.out.println("% diff: " + sum);
		
		
	}
	
	public static void main(String[] args) {
		new SD();
	}

}
