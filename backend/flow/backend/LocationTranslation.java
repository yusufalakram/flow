package flow.backend;

import java.math.BigDecimal;

public class LocationTranslation {
	private static final BigDecimal LAT_TO_MILES = new BigDecimal(69.172);
	
	public static BigDecimal[] radiusToLocation(BigDecimal lat, int radius){
		BigDecimal[] retVal = new BigDecimal[2];
		BigDecimal bdRad = new BigDecimal(radius);
		BigDecimal cosLat = new BigDecimal(Math.cos(lat.doubleValue()));
		retVal[0] = bdRad.divide(cosLat.multiply(LAT_TO_MILES));
		retVal[1] = bdRad.divide(LAT_TO_MILES);
		return retVal;
	}
}
