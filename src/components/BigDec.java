package components;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDec extends BigDecimal{

	private static final BigDecimal SQRT_DIG = new BigDecimal(15);
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());
	
	public BigDec(BigInteger c) {
		super(c);
	}
	public BigDec(String s) {
		super(s);
	}

	private static BigDecimal newtonRoot(BigDecimal num, BigDecimal xn, BigDecimal precision) {
		//f(x) = x^2 - c
		BigDecimal fx = xn.pow(2).add(num.negate());
		BigDecimal fpn = xn.multiply(new BigDecimal(2));
		BigDecimal quo = fx.negate().divide(fpn, 2 * SQRT_DIG.intValue(), BigDecimal.ROUND_HALF_EVEN);
		BigDecimal xn2 = quo.add(xn);
		BigDecimal numsquare = xn2.pow(2);
		BigDecimal squareoff = num.subtract(numsquare).abs();
		if(squareoff.compareTo(precision) <= -1) {
			return xn2;
		}
		return newtonRoot(num, xn2, precision);
		
	}

}
