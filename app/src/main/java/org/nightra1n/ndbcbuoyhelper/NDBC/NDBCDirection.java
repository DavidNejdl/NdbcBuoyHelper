package org.nightra1n.ndbcbuoyhelper.NDBC;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.nightra1n.ndbcbuoyhelper.R;
import org.nightra1n.ndbcbuoyhelper.helper.BitmapUtils;
import org.nightra1n.ndbcbuoyhelper.helper.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class NDBCDirection {
	private static final String TAG = "NDBCDirection";

	int MAX_N = 11;
	int MIN_N = 349;

	int MIN_NNE = 12;
	int MAX_NNE = 33;
	int MIN_NE = 34;
	int MAX_NE = 56;
	int MIN_ENE = 57;
	int MAX_ENE = 78;
	int MIN_E = 79;
	int MAX_E = 101;
	int MIN_ESE = 102;
	int MAX_ESE = 123;
	int MIN_SE = 124;
	int MAX_SE = 146;
	int MIN_SSE = 147;
	int MAX_SSE = 168;
	int MIN_S = 169;
	int MAX_S = 191;
	int MIN_SSW = 192;
	int MAX_SSW = 213;
	int MIN_SW = 214;
	int MAX_SW = 236;
	int MIN_WSW = 237;
	int MAX_WSW = 258;
	int MIN_W = 259;
	int MAX_W = 281;
	int MIN_WNW = 282;
	int MAX_WNW = 303;
	int MIN_NW = 304;
	int MAX_NW = 326;
	int MIN_NNW = 327;
	int MAX_NNW = 348;

	// ESE (102°)
	// N (350°)
//	String direction;
	String regex = "([NESW]{1,3})\\s\\((\\d{1,3})" + Constants.UNICODE_DEGREE_SYMBOL + "?\\)";
	Pattern pattern;
	Matcher matcher;

	int degrees;
	String compass;

	public NDBCDirection(String combinedDirString) throws NDBCException {
//		this.direction = combinedDirString;
		try {
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(combinedDirString);
			if (!matcher.find())
				throw new NDBCException("Invalid Direction String: " + combinedDirString);
			compass = matcher.group(1);
			degrees = Integer.parseInt(matcher.group(2));
		} catch (PatternSyntaxException e) {
			throw new NDBCException("Invalid Direction String: " + combinedDirString);
		} catch (NullPointerException e) {
			throw new NDBCException("NullPointerException: Invalid direction: " + combinedDirString);
		} catch (IllegalStateException e) {
			throw new NDBCException("IllegalStateException: " + combinedDirString);
		}
	}

	public NDBCDirection(int degree) {
		this.degrees = degree;

		if (degree <= MAX_N || degree >= MIN_N) {
			this.compass = "N";
		} else if (degree >= MIN_NNE && degree <= MAX_NNE) {
			this.compass = "NNE";
		} else if (degree >= MIN_NE && degree <= MAX_NE) {
			this.compass = "NE";
		} else if (degree >= MIN_ENE && degree <= MAX_ENE) {
			this.compass = "ENE";
		} else if (degree >= MIN_E && degree <= MAX_E) {
			this.compass = "E";
		} else if (degree >= MIN_ESE && degree <= MAX_ESE) {
			this.compass = "ESE";
		} else if (degree >= MIN_SE && degree <= MAX_SE) {
			this.compass = "SE";
		} else if (degree >= MIN_SSE && degree <= MAX_SSE) {
			this.compass = "SSE";
		} else if (degree >= MIN_S && degree <= MAX_S) {
			this.compass = "S";
		} else if (degree >= MIN_SSW && degree <= MAX_SSW) {
			this.compass = "SSW";
		} else if (degree >= MIN_SW && degree <= MAX_SW) {
			this.compass = "SW";
		} else if (degree >= MIN_WSW && degree <= MAX_WSW) {
			this.compass = "WSW";
		} else if (degree >= MIN_W && degree <= MAX_W) {
			this.compass = "W";
		} else if (degree >= MIN_WNW && degree <= MAX_WNW) {
			this.compass = "WNW";
		} else if (degree >= MIN_NW && degree <= MAX_NW) {
			this.compass = "NW";
		} else if (degree >= MIN_NNW && degree <= MAX_NNW) {
			this.compass = "NNW";
		}


	}

	public String parseCompass() {
		return this.compass;
	}

	public int parseDegree() {
		return this.degrees;
	}

	public String parseCombinedString() {
		return this.compass + " (" + this.degrees + Constants.UNICODE_DEGREE_SYMBOL + ")";
	}

    public Bitmap exportBitmap(Context context) {
        // direction image
        Bitmap source = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.glyphicons_233_direction_white);
        source = BitmapUtils.RotateBitmap(source, 135);

        source = BitmapUtils.RotateBitmap(source, parseDegree());
        return source;
    }
}
