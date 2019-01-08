package org.nightra1n.ndbcbuoyhelper.NDBC;

import org.nightra1n.ndbcbuoyhelper.helper.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class NDBC {
	private static final String TAG = LogUtils.makeLogTag(NDBC.class);

	// Block Island = 44097
	private static final String URL_STATION_PAGE = "http://www.ndbc.noaa.gov/station_page.php?station=NDBCID";
	private static final String URL_LATEST_OBS = "http://www.ndbc.noaa.gov/data/latest_obs/NDBCID.rss";
	private static final String URL_5DAY = "http://www.ndbc.noaa.gov/data/5day2/NDBCID_5day.txt";
	private static final String URL_PLOT_WVHT =
			"http://www.ndbc.noaa.gov/plot_wave.php?station=NDBCID&meas=sght&uom=E&time_diff=-4&time_label=EDT";
	private static final String URL_PLOT_DPD =
			"http://www.ndbc.noaa.gov/plot_wave.php?station=NDBCID&meas=dmpd&uom=E&time_diff=-4&time_label=EDT";
	private static final String URL_PLOT_MWD =
			"http://www.ndbc.noaa.gov/plot_wave.php?station=NDBCID&meas=wvdr&uom=E&time_diff=-4&time_label=EDT";
	private static final String URL_PLOT_WTMP =
			"http://www.ndbc.noaa.gov/plot_wave.php?station=NDBCID&meas=wtmp&uom=E&time_diff=-4&time_label=EDT";
	// Wave energy versus frequency (and period)
    // Spectral Density
	private static final String URL_PLOT_FREQ =
			"http://www.ndbc.noaa.gov/spec_plot.php?station=NDBCID";


	private static String replaceNDBCid(String urlString, String NDBC_id) {
		String url;

		// verify we get a valid NDBC ID
		if (!isValidNDBCid(NDBC_id)) {
			LogUtils.LOGE(TAG, "Invalid NDBC Id: " + (NDBC_id == null ? "null" : NDBC_id));
			return null;
		}

		try {
			String encodedNdbcId = URLEncoder.encode(NDBC_id.toUpperCase(Locale.US), "UTF-8");
			url = urlString.replace("NDBCID", encodedNdbcId);
		} catch (NullPointerException e) {
			LogUtils.LOGE(TAG, "NullPointerException: ", e);
			url = null;
		} catch (UnsupportedEncodingException e) {
			LogUtils.LOGE(TAG, "UnsupportedEncodingException: ", e);
			url = null;
		}

		return url;
	}

	public static String getUrl5day(String NDBC_id) {
		return replaceNDBCid(URL_5DAY, NDBC_id);
	}

	public static String getUrlStationPage(String NDBC_id) {
		return replaceNDBCid(URL_STATION_PAGE, NDBC_id);
	}

	public static String getUrlPlotWaveHeight(String NDBC_id) {
		return replaceNDBCid(URL_PLOT_WVHT, NDBC_id);
	}

	public static String getUrlPlotDominantPeriod(String NDBC_id) {
		return replaceNDBCid(URL_PLOT_DPD, NDBC_id);
	}

	public static String getUrlPlotMeanWaveDirection(String NDBC_id) {
		return replaceNDBCid(URL_PLOT_MWD, NDBC_id);
	}

	public static String getUrlPlotWaterTemp(String NDBC_id) {
		return replaceNDBCid(URL_PLOT_WTMP, NDBC_id);
	}

	public static String getUrlPlotFreq(String NDBC_id) {
		return replaceNDBCid(URL_PLOT_FREQ, NDBC_id);
	}

	public static String getUrlLatestObs(String NDBC_id) {
		return replaceNDBCid(URL_LATEST_OBS, NDBC_id);
	}

	public static boolean isValidNDBCid(String NDBC_id) {
		boolean isValid = false;
		if (NDBC_id == null)
			return false;
		if (NDBC_id.length() == 5 && !NDBC_id.matches("^.*[^a-zA-Z0-9 ].*$"))
			isValid = true;
		return isValid;
	}

}
