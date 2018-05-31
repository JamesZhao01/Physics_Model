package components;

import java.math.BigDecimal;

import ch.obermuhlner.math.big.BigDecimalMath;

public class Vector implements Cloneable{

	private BigDecimal x;
	private BigDecimal y;
	private BigDecimal theta;
	private BigDecimal mag;
	/**
	 * 
	 * @param mag = x if isPolar = false
	 * @param theta = y if isPolar = false
	 * @param isPolar
	 */
	public Vector(BigDecimal mag, BigDecimal theta, boolean isPolar) {
		if(isPolar) {
			this.mag = mag;
			this.theta = theta;
			this.x = BigDecimalMath.cos(theta, Planet.PREC).multiply(mag);
			this.y = BigDecimalMath.sin(theta, Planet.PREC).multiply(mag);
		} else {
			this.x = mag;
			this.y = theta;
			this.mag = BigDecimalMath.sqrt(x.pow(2).add(y.pow(2)), Planet.PREC);
			this.theta = BigDecimalMath.atan2(y, x, Planet.PREC);
		}
	}
	public BigDecimal getX() {
		return x;
	}
	public void setX(BigDecimal x) {
		this.x = x;
		updatePolar();
	}
	
	public BigDecimal getY() {
		return y;
	}
	public void setY(BigDecimal y) {
		this.y = y;
		updatePolar();
	}
	public BigDecimal getTheta() {
		return theta;
	}
	public void setTheta(BigDecimal theta) {
		this.theta = theta;
		updateCoord();
	}
	public BigDecimal getMag() {
		return mag;
	}
	public void setMag(BigDecimal mag) {
		this.mag = mag;
		updateCoord();
	}
	private void updatePolar() {
		this.mag = BigDecimalMath.sqrt(x.pow(2).add(y.pow(2)), Planet.PREC);
		this.theta = BigDecimalMath.atan2(y, x, Planet.PREC);
	}
	
	private void updateCoord() {
		this.x = BigDecimalMath.cos(theta, Planet.PREC).multiply(mag);
		this.y = BigDecimalMath.sin(theta, Planet.PREC).multiply(mag);
	}
	
	public void scale(BigDecimal sf) {
		this.mag = this.mag.multiply(sf);
		updateCoord();
	}
	
	@Override
	public Vector clone() {
		return new Vector(x, y, false);
	}
	
	public BigDecimal dot(Vector v) {
		return x.multiply(v.x).add(y.multiply(v.y));
	}
	
	public BigDecimal angle(Vector v) {
		return BigDecimalMath.acos(this.dot(v).divide(this.mag.multiply(v.mag), Planet.PREC), Planet.PREC);
	}
	
	public BigDecimal area(Vector v) {
		return this.mag.multiply(v.mag).multiply(BigDecimalMath.sin(this.angle(v), Planet.PREC)).divide(new BigDecimal(2), Planet.PREC);
	}
	
}
