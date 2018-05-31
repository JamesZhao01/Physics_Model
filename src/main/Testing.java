package main;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.swing.*;

import ch.obermuhlner.math.big.BigDecimalMath;
import components.Vector;

public class Testing {

	public static MathContext CON = new MathContext(50, RoundingMode.HALF_EVEN);
	public Testing() {
//		BigDecimal b = new BigDecimal("12394238");
//		System.out.println(b.intValueExact());
//		BigDecimal xn = new BigDecimal(1).negate().setScale(CON.getPrecision());
//		for(int i = 1; i < 15; i++) {
//			BigDecimal y = BigDecimalMath.pow(xn, 3, CON).subtract(xn).add(new BigDecimal(3));
//			BigDecimal fy = BigDecimalMath.pow(xn, 2, CON).multiply(new BigDecimal(3)).subtract(BigDecimal.ONE);
//			BigDecimal xnext = BigDecimal.ONE.negate().multiply(y).divide(fy, CON.getRoundingMode()).add(xn);
//			System.out.println("x" + ((i < 10)? "0" : "") + i + " | " + xn);
//			xn = xnext;
//		}
//		JFrame frame = new JFrame();
//		JPanel panel = new JPanel() {
//			@Override
//			public void paintComponent(Graphics g) {
//				
//				Graphics2D g2d = (Graphics2D)g;
//				
//				g2d.scale(2, 2);
//				g.drawLine(50, 50, 100, 51);
//				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//				Line2D.Float line = new Line2D.Float(150f, 150f, 200f, 151f);
//				g2d.draw(line);
//			}
//		};
//		panel.setPreferredSize(new Dimension(300, 300));
//		frame.setContentPane(panel);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
//		frame.setVisible(true);
//		for(int i = 0; i < 100; i++)
//		try(BufferedWriter bw = new BufferedWriter(new FileWriter("meme.dat", true));) {
//			bw.write(i + "\n");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Vector v = new Vector(BigDecimal.ONE, BigDecimal.ONE, false);
		Vector v2 = new Vector(BigDecimal.ONE.negate(), BigDecimal.ONE, false);
		System.out.println(v.angle(v2));
		System.out.println(v2.angle(v));
		System.out.println(v.area(v2));
		
	}

	public static void main(String[] args) {
		new Testing();
		
	}
}
