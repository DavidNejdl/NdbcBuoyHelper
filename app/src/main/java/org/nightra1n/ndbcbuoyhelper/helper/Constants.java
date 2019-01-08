package org.nightra1n.ndbcbuoyhelper.helper;

import android.text.format.DateUtils;

public class Constants {
	public static final boolean DEBUG = true;

    public static final boolean DEFAULT_AUTO_REFRESH = true;
	public static final int DEFAULT_UPDATE_INTERVAL_MILLIS = 1800000;
	public static final int DEFAULT_UPDATE_INTERVAL_MINUTES = DEFAULT_UPDATE_INTERVAL_MILLIS / 60000;
	public static final int DEFAULT_TIMEOUT_CONNECT = 10000;   // timeout after 10 seconds
	public static final int DEFAULT_TIMEOUT_READ = 10000;
	public static final int DEFAULT_MAX_FETCH_BUOYS = 80;

	// Symbols
	public static final String UNICODE_DEGREE_SYMBOL = "\u00b0";
	public static final String UNICODE_DEGREE_CELCIUS = "\u2103";
	public static final String UNICODE_DEGREE_FAHRENHEIT = "\u2109";

	// Time stuff
	public final static long HOUR_IN_MILLIS = 3600000L;
	public final static long DAY_IN_MILLIS = 86400000L;
	public final static long WEEK_IN_MILLIS = 7 * DateUtils.DAY_IN_MILLIS;
	public final static long YEAR_IN_MILLIS = (long) (52.1775 * DateUtils.WEEK_IN_MILLIS);

}
