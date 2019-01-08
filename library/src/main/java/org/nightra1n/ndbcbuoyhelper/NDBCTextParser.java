package org.nightra1n.ndbcbuoyhelper;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

import org.nightra1n.ndbcbuoyhelper.NDBC.NDBCDirection;
import org.nightra1n.ndbcbuoyhelper.NDBC.Units;
import org.nightra1n.ndbcbuoyhelper.helper.Constants;
import org.nightra1n.ndbcbuoyhelper.helper.DateHelper;
import org.nightra1n.ndbcbuoyhelper.helper.LogUtils;
import org.nightra1n.ndbcbuoyhelper.model.BuoyObservation;
import org.nightra1n.ndbcbuoyhelper.model.FiveDayRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NDBCTextParser {
    private static final String TAG = LogUtils.makeLogTag(NDBCTextParser.class);

    private final String assetPath = "44025_5day.txt";
    final URL rssUrl;

    public NDBCTextParser(String url) {
        try {
            this.rssUrl = new URL(url);
        } catch (MalformedURLException e) {
            LogUtils.LOGV(TAG, "MalformedURLException: " + e.toString());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<BuoyObservation> parse(int maxItems) {

        FixedFormatManager manager = new FixedFormatManagerImpl();
        ArrayList<BuoyObservation> observations = new ArrayList<BuoyObservation>();
        BuoyObservation buoyObservation;
        InputStream inputStream;

        String line;
        int lineNum = 0;

        // Get txt data
        try {
            inputStream = getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            do {
                line = reader.readLine();
                lineNum++;
                if (line != null && lineNum > 2) {				// Ignore first two lines
                    // do something with line
                    FiveDayRecord record = manager.load(FiveDayRecord.class, line);
                    buoyObservation = convert(record);
//                    LOGD(TAG, "NDBCTextParser: parsed observation: " + buoyObservation.toString());
                    observations.add(buoyObservation);
                }
            } while (line != null);
        } catch (IOException e) {
            LogUtils.LOGE(TAG, "IOException: ", e);
        } finally {
//			if (inputStream != null)
//				inputStream.close();
        }

        return observations;
    }

    private BuoyObservation convert(FiveDayRecord fiveDayRecord) {
        BuoyObservation observation = new BuoyObservation();

        // Timestamp
        Date date = fiveDayRecord.getTimestamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long longDate = DateHelper.formatDateAsLong(calendar);
        observation.setDescDate(longDate);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);

        // Wind
        float windSpeedMPS = fiveDayRecord.getWindSpeed();
        if (windSpeedMPS != 0f) {
            float windSpeedKnots = Units.mpsToKnots(windSpeedMPS);
            String windSpeedValue = df.format(windSpeedKnots);
            observation.setWindSpeed(windSpeedValue + " knots");
        }

        float windGustMPS = fiveDayRecord.getWindGust();
        if (windGustMPS != 0f) {
            float windGustKnots = Units.mpsToKnots(windGustMPS);
            String windGustValue = df.format(windGustKnots);
            observation.setWindGust(windGustValue + " knots");
        }

        // TODO: standardize
        int windDir = fiveDayRecord.getWindDirection();
        if (windDir != 0) {
            try {
                NDBCDirection windDirection = new NDBCDirection(windDir);
                observation.setWindDirection(windDirection.parseCombinedString());
            } catch (Exception e) {
                LogUtils.LOGE(TAG, "Exception: ", e);
            }
        }

        // Waves
        float waveHeightMeters = fiveDayRecord.getWaveHeight();
        if (waveHeightMeters != 0f) {
            float waveHeightFeet = Units.metersToFeet(waveHeightMeters);
            float waveHeightFeetFinal = Float.parseFloat(df.format(waveHeightFeet));
            observation.setWaveHeightSignificant(waveHeightFeetFinal);
        }

        int waveDir = fiveDayRecord.getWaveDirection();
        if (waveDir != 0) {
            try {
                NDBCDirection waveDirection = new NDBCDirection(waveDir);
                observation.setWaveDirection(waveDirection.parseCombinedString());
            } catch (Exception e) {
                LogUtils.LOGE(TAG, "Exception: ", e);
            }
        }

        int periodDominant = fiveDayRecord.getPeriodDominant();
        if (periodDominant != 0) {
            observation.setPeriodDominant((float) periodDominant);
        }

        float periodAverage = fiveDayRecord.getPeriodAverage();
        if (periodAverage != 0f) {
            observation.setAveragePeriod(periodAverage);
        }

        // Temperature
        // TODO: clean up F vs C
        float airTempC = fiveDayRecord.getAirTemp();
        if (airTempC != 0f) {
            observation.setAirTempC(airTempC);
            float airTempF = Units.celsiusToFahrenheit(airTempC);
            airTempF = Float.valueOf(df.format(airTempF));
            observation.setAirTempF(airTempF);
        }

        float waterTempC = fiveDayRecord.getWaterTemp();
        if (waterTempC != 0f) {
            observation.setWaterTempC(waterTempC);
            float waterTempF = Units.celsiusToFahrenheit(waterTempC);
            waterTempF = Float.valueOf(df.format(waterTempF));
            observation.setWaterTempF(waterTempF);
        }

        return observation;
    }

    private InputStream getInputStream() {
        try {
            URLConnection urlConnection = rssUrl.openConnection();
            urlConnection.setConnectTimeout(Constants.DEFAULT_TIMEOUT_CONNECT);
            urlConnection.setReadTimeout(Constants.DEFAULT_TIMEOUT_READ);
            return urlConnection.getInputStream();
        } catch (IOException e) {
            LogUtils.LOGE(TAG, "IOException: ", e);
            throw new RuntimeException();
        }
    }

}
