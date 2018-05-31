package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;

public class Planet{

	private static BigDecimal G = new BigDecimal("6.67408E-11");
	private static int TRACE_SIZE = 100000;
	public static MathContext PREC = new MathContext(20, RoundingMode.HALF_EVEN);
	public static BigDecimal AU_CONV = new BigDecimal("1.496E11");
	public static BigDecimal STEP = new BigDecimal("8640").multiply(new BigDecimal("40"));
	public Planet lock;
	public static int nextID = 0;
	public int id;
	private int iteration;
	private BigDecimal prevDist;
	private BigDecimal prevVMag;
	private BigDecimal prevDist2;
	private BigDecimal prevVMag2;
	private BigDecimal prevX;
	private BigDecimal prevY;

	private BigDecimal a;
	private BigDecimal b;
	private boolean drawEllipse;

	public String name;
	public BigDecimal x;
	public BigDecimal y;
	public BigDecimal mass;
	public Vector v;
	public Vector f;
	public boolean isMovable;
	public ArrayList<Point> points = new ArrayList<Point>();
	public ArrayList<Point> posPer = new ArrayList<Point>();
	public ArrayList<BigDecimal> en = new ArrayList<BigDecimal>();
	public ArrayList<BigDecimal> perix = new ArrayList<BigDecimal>();
	public ArrayList<BigDecimal> periy = new ArrayList<BigDecimal>();
	public ArrayList<BigDecimal> lx = new ArrayList<BigDecimal>();
	public ArrayList<BigDecimal> ly = new ArrayList<BigDecimal>();

	private BigDecimal originalAngle;
	int count = 0;
	//120 pluto
	final int pointsPerCount = 40;
	int dataPointCount = 0;
	
	int energyPointCount = 0;
	int maxEnergyPoints = 100;

	public Planet(String name, BigDecimal x, BigDecimal y, BigDecimal mass, Vector v, Vector f, boolean isMovable, BigDecimal a, BigDecimal b) {
		this.name = name;
		id = nextID;
		nextID++;
		this.x = x;
		this.y = y;
		this.mass = mass;
		this.v = v;
		this.f = f;
		this.isMovable = isMovable;
		this.a = a;
		this.b = b;
		drawEllipse = true;
	}
	public Planet(String name, BigDecimal x, BigDecimal y, BigDecimal mass, Vector v, Vector f, boolean isMovable) {
		this(name, x, y, mass, v, f, isMovable, BigDecimal.ZERO, BigDecimal.ZERO);
		drawEllipse = false;
	}

	public Planet(String name, BigDecimal x, BigDecimal y, BigDecimal mass, Vector v, Planet p, boolean isMovable, BigDecimal a, BigDecimal b) {
		this(name, x, y, mass, v, new Vector(calcForce(p.mass, mass, dist(x, y, p)), BigDecimalMath.atan2(p.y.subtract(y), p.x.subtract(x), PREC), true), isMovable, a, b);
		lock = p;
	}

	public Planet(String name, BigDecimal x, BigDecimal y, BigDecimal mass, Vector v, Planet p, boolean isMovable) {
		this(name, x, y, mass, v, new Vector(calcForce(p.mass, mass, dist(x, y, p)), BigDecimalMath.atan2(p.y.subtract(y), p.x.subtract(x), PREC), true), isMovable);
		lock = p;
	}

	public void draw(Graphics2D g) {

		try {
			g.setColor(Color.BLACK);
			g.drawRect(convert(x) - 10, convert(y) - 10, 20, 20);
			g.setColor(Color.BLUE);
			g.drawLine(convert(x), convert(y), convert(x.add(v.getX())), convert(y.add(v.getY())));

			g.setColor(Color.GREEN);
			g.setStroke(new BasicStroke(1f));
			//Trace
			//			for(int i = 0; i < points.size() - 1; i+=10) {
			//				Line2D.Double l = new Line2D.Double(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
			//				g.draw(l);
			//			}
			g.setColor(Color.CYAN);
			for(int i = 0; i < perix.size(); i++){
				g.drawRect(convert(perix.get(i)), convert(periy.get(i)), 1, 1);
			}
			//			boolean isGray = true;
			//			g.setColor(new Color(171, 255, 0));
			//
			//			for(int i = 0; i < lx.size() - 1; i+=1) {
			//				if(i  % pointsPerCount == 0) {
			//					if(isGray) 
			//						g.setColor(new Color(0, 255, 242));
			//					else
			//						g.setColor(new Color(171, 255, 0));
			//					isGray = !isGray;
			//				}
			//				Polygon p = new Polygon();
			//				p.addPoint(convert(lock.x), convert(lock.y));
			//				p.addPoint((int)points.get(i).x, (int)points.get(i).y);
			//				p.addPoint((int)points.get(i + 1).x, (int)points.get(i + 1).y);
			//
			//				g.fill(p);
			//			}

			g.setColor(Color.RED);
			if(drawEllipse && lock != null) {
				BigDecimal c = BigDecimalMath.sqrt(a.pow(2).subtract(b.pow(2)), PREC);
				Ellipse2D.Double ellipse = new Ellipse2D.Double(convertD(lock.x.subtract(c).subtract(a)), convertD(lock.y.subtract(b)), convertD(a.multiply(new BigDecimal("2"))), convertD(b.multiply(new BigDecimal("2"))));
				g.draw(ellipse);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public void update() {
		iteration++;
		if(isMovable) {
			f = new Vector(calcForce(lock.mass, mass, dist(x, lock.x, y, lock.y)), BigDecimalMath.atan2(lock.y.subtract(y), lock.x.subtract(x), PREC), true);
			v.setX(v.getX().add(f.getX().multiply(STEP).divide(mass, PREC.getPrecision(), PREC.getRoundingMode())));
			v.setY(v.getY().add(f.getY().multiply(STEP).divide(mass, PREC.getPrecision(), PREC.getRoundingMode())));
			x = x.add(v.getX().multiply(STEP));
			y = y.add(v.getY().multiply(STEP));
			add(new Point(convertD(x), convertD(y)));
			/* Second Law Calculations */
			if(iteration > 2) {
				count++;
				lx.add(x);
				ly.add(y);
				if(count == pointsPerCount) {
					count = 0;
					BigDecimal netArea = BigDecimal.ZERO;
					for(int i = 0; i < pointsPerCount - 1; i++) {
						int i2 = lx.size() - 1 - i;
						int i1 = lx.size() - 2 - i;
						BigDecimal areaComp = distTwoComps(lx.get(i2), ly.get(i2), lx.get(i1), ly.get(i1)).multiply(dist(lx.get(i1), ly.get(i1), lock));
						netArea = netArea.add(areaComp);
					}
					//write data
					//					try(BufferedWriter bw = new BufferedWriter(new FileWriter("data_secondLaw_" + name + ".dat", true));) {
					//						bw.write(netArea + "\n");
					//					} catch (IOException exc) {
					//						exc.printStackTrace();
					//					}
					//					dataPointCount++;
					//					if(dataPointCount == 300)
					//						System.exit(0);
				}
			}
			// Perihelion detection / energy calculations
			if(iteration > 2 && prevDist2.compareTo(prevDist) == 1 && dist(x, y, lock).compareTo(prevDist) == 1) {
				System.out.println("peri" + iteration);
				perix.add(prevX);
				periy.add(prevY);
				BigDecimal angle = calcAngle(x, y);
				if(originalAngle != null) {
					originalAngle = angle;
				}
//				BigDecimal kE = (new BigDecimal(0.5).multiply(mass).multiply(prevVMag.pow(2)));
//				BigDecimal gE = (mass.multiply(lock.mass).multiply(G).negate().setScale(PREC.getPrecision()).divide(prevDist, PREC.getRoundingMode()));
//				BigDecimal e = kE.add(gE);
				try(BufferedWriter bw = new BufferedWriter(new FileWriter("dataA" + STEP + ".dat", true));) {
					bw.write(angle + "\n");
					energyPointCount++;
				} catch (IOException exc) {
					exc.printStackTrace();
				}
				if(energyPointCount == maxEnergyPoints) 
					System.exit(0);
			}

			// updating prevVars
			prevDist2 = prevDist;
			prevVMag2 = prevVMag;
			prevVMag = v.getMag();
			prevDist = dist(x, y, lock);
			prevX = x;
			prevY = y;
		}


	}

	private static BigDecimal calcForce(BigDecimal mass1, BigDecimal mass2, BigDecimal distance) {
		return G.multiply(mass1).multiply(mass2).divide(distance.multiply(distance), PREC.getPrecision(), BigDecimal.ROUND_HALF_EVEN);
	}

	private BigDecimal dist(Vector v1, Vector v2) {
		BigDecimal c1 = v1.getX().subtract(x).pow(2).abs();
		BigDecimal c2 = v2.getY().subtract(y).pow(2).abs();
		return BigDecimalMath.sqrt(c1.add(c2), PREC);

	}

	private BigDecimal dist(BigDecimal x1, BigDecimal x2, BigDecimal y1, BigDecimal y2) {
		BigDecimal c1 = x1.subtract(x2).pow(2).abs();
		BigDecimal c2 = y1.subtract(y2).pow(2).abs();
		return BigDecimalMath.sqrt(c1.add(c2), PREC);	
	}

	private static BigDecimal dist(BigDecimal x1, BigDecimal y1, Planet p) {
		BigDecimal c1 = p.x.subtract(x1).pow(2).abs();
		BigDecimal c2 = p.y.subtract(y1).pow(2).abs();
		return BigDecimalMath.sqrt(c1.add(c2), PREC);	
	}

	public static int convert(BigDecimal b) throws Exception {
		BigDecimal res = b.divide(AU_CONV, 20, PREC.getRoundingMode()).multiply(new BigDecimal(100)).setScale(0, PREC.getRoundingMode());
		return res.intValueExact();
	}

	public static double convertD(BigDecimal b) {
		BigDecimal res = b.divide(AU_CONV, 20, PREC.getRoundingMode()).multiply(new BigDecimal(100)).setScale(PREC.getPrecision(), PREC.getRoundingMode());
		return res.doubleValue();
	}

	@Override
	public String toString() {
		try {
			return convert(x) + ", " + convert(y);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	public void add(Point p) {
		if(points.size() == TRACE_SIZE) {
			points.remove(0);
		}
		points.add(p);
	}

	private BigDecimal calcEnergy() {
		return new BigDecimal(0.5).multiply(mass).multiply(v.getMag().pow(2)).add(G.multiply(mass).multiply(lock.mass).divide(dist(x, y, lock)));
	}

	private BigDecimal calcAngle(BigDecimal xi, BigDecimal yi) {
		BigDecimal y2 = yi.subtract(lock.y);
		BigDecimal x2 = xi.subtract(lock.x);
		return BigDecimalMath.atan2(y2, x2, Planet.PREC);
	}

	public static BigDecimal distTwoComps(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
		return BigDecimalMath.sqrt(x1.subtract(x2).abs().pow(2).add(y1.subtract(y2).abs().pow(2)), PREC);
	}

}
