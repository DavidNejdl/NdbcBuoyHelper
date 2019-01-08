package org.nightra1n.ndbcbuoyhelper.NDBC;

public class Units {

    private static final double MPS_TO_KNOTS = 1.9438444924406;
    private static final double METERS_TO_FEET = 3.2808399;

    public static float mpsToKnots(float mps) {
        return (float) (mps * MPS_TO_KNOTS);
    }

    public static float metersToFeet(float meters) {
        return (float) (meters * METERS_TO_FEET);
    }

    public static float celsiusToFahrenheit(float celsius) {
        return (celsius * 9 / 5) + 32;
    }

    public static float fahrenheitToCelsius(float fahrenheit) {
        return ((fahrenheit - 32)*5)/9;
    }

}
