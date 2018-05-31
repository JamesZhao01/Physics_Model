package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JPanel;

import ch.obermuhlner.math.big.BigDecimalMath;
import components.Entity;
import components.Planet;
import components.Vector;
import main.Main;

public class Screen extends JPanel{

	/*
	 * problems: force calculationaa
	 */
	Planet sun;
	private static double scale = 2;
	private double hShift = 0;
	private double vShift = 0;
	private Main ref;
	public boolean run = true;
	ArrayList<Planet> comps = new ArrayList<Planet>();
	public Screen(Main m) {
		ref = m;
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_A) {
					if(m.isPaused) {
						synchronized(Main.class) {
							Main.class.notifyAll();
							ref.isPaused = false;
						}
					} else {
						ref.pause();
					}

				} else {
					switch(e.getKeyCode()) {
					case KeyEvent.VK_W:
						scale *= 1.25;
						break;
					case KeyEvent.VK_S:
						if(scale > 0)
							scale /= 1.25;
						break;
					case KeyEvent.VK_UP:
						vShift -= 50;
						break;
					case KeyEvent.VK_DOWN:
						vShift += 50;
						break;
					case KeyEvent.VK_LEFT:
						hShift -= 50;
						break;
					case KeyEvent.VK_RIGHT:
						hShift += 50;
						break;
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_R) {
					repaint();
				}

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

		});
		this.setPreferredSize(new Dimension(1200, 600));
		// 1.496e+11 m in one au
		sun = new Planet("Sun", new BigDecimal("5E11"), new BigDecimal("4E11"), new BigDecimal("1.989E30"), new Vector(BigDecimal.ZERO, BigDecimal.ZERO, true), new Vector(BigDecimal.ZERO, BigDecimal.ZERO, true), false);
		comps.add(sun);
		comps.add(new Planet("Earth", new BigDecimal("5E11").add(new BigDecimal("147.09E9")), new BigDecimal("4E11"), new BigDecimal("5.9723E24"), new Vector(BigDecimal.ZERO, new BigDecimal("30290"), false), sun, true));		
//		, new BigDecimal("1.4960E11"), new BigDecimal("1.49579E11")
//		To calculate b(b/c I couldn't find a value), b^2 = a^2 - c^2, c = a - perihelion
//		BigDecimal auToM = new BigDecimal("1.496E11");
//		BigDecimal a = new BigDecimal("39.6");
//		BigDecimal b = BigDecimalMath.sqrt(a.pow(2).subtract(a.subtract(new BigDecimal("29.7")).pow(2)), Planet.PREC);
//		System.out.println(a + ", " + b);
//		comps.add(new Planet("Pluto", new BigDecimal(5E11).add(new BigDecimal("4.4431E12")), new BigDecimal("4E11"), new BigDecimal("1.30900E22"), new Vector(BigDecimal.ZERO, new BigDecimal("6100"), false), sun, true));

	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		at.translate(-334 * ((scale - 1)/scale) + hShift, -267 * ((scale - 1)/scale) + vShift);
		g2.setTransform(at);
		
		for(Planet i : comps) {
			i.draw(g2);
			i.update();
			if(i.isMovable) {
//				BigDecimal a = i.a;
//				BigDecimal b = i.b;
//				BigDecimal c = BigDecimalMath.sqrt(a.pow(2).subtract(b.pow(2)), Planet.PREC);
//				try {
//					g2.drawOval(Planet.convert(sun.x.subtract(c).subtract(a)), Planet.convert(sun.y.subtract(b)), Planet.convert(a.multiply(new BigDecimal(2))), Planet.convert(b.multiply(new BigDecimal(2))));
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}
		try {
			int centerx = Planet.convert(new BigDecimal("5E11"));
			int centery = Planet.convert(new BigDecimal("4E11"));
			for(int i = 0; i <= 3; i++) {
				g.setColor(Color.BLUE);
				g.drawLine(centerx, centery, centerx + 100, centery);
				g.drawLine(centerx, centery, centerx - 100, centery);
				g.drawLine(centerx, centery, centerx, centery + 100);
				g.drawLine(centerx, centery, centerx, centery - 100);
				
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

}
