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

	// Preference Keys
	public static final String KEY_PREF_GPS_RADIUS = "settings_gps_radius";
	public static final String KEY_PREF_PERFORM_UPDATES = "settings_perform_updates";
	public static final String KEY_PREF_UPDATE_FREQUENCY = "settings_update_frequency";

	// Time stuff
	public final static long HOUR_IN_MILLIS = 3600000L;
	public final static long DAY_IN_MILLIS = 86400000L;
	public final static long WEEK_IN_MILLIS = 7 * DateUtils.DAY_IN_MILLIS;
	public final static long YEAR_IN_MILLIS = (long) (52.1775 * DateUtils.WEEK_IN_MILLIS);

	public final static String PARCEL_BUOY = "BuoyParcel";
	public final static String PARCEL_OBSERVATION = "BuoyObservation";
	public final static String PARCEL_WIDGET = "WidgetParcel";
	public final static String ARG_TIMESTAMP = "Timestamp";

	// Request Codes
	public final static String ARG_REQUEST_TYPE = "RequestType";
	public final static String ARG_BUOY_NDBC_ID = "BuoyNDBCId";
	public final static String ARG_BUOY_WIDGET_ID = "WidgetId";
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_IS_WIDGET_SETUP = "IsWidgetSetup";

	public final static int REQUEST_WIDGET_DETAIL = 1;
	public final static int REQUEST_WIDGET_SETUP = 2;
	public static final String ACTION_WIDGET_REFRESH = "org.nightra1n.buoywidget.APPWIDGET_REFRESH";
	public static final String ACTION_WIDGET_RELOAD = "org.nightra1n.buoywidget.APPWIDGET_RELOAD";
	public static final String ACTION_WIDGET_RESIZE = "org.nightra1n.buoywidget.APPWIDGET_RESIZE";

	// Request code to use when launching the resolution activity
	public static final int REQUEST_RESOLVE_PLAY_SERVICES_ERROR = 1001;
	public static final int REQUEST_PLAY_SERVICES_ERROR_DIALOG = 1002;



}
