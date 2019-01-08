package org.nightra1n.ndbcbuoyhelper.helper;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.nightra1n.ndbcbuoyhelper.helper.LogUtils.LOGE;
import static org.nightra1n.ndbcbuoyhelper.helper.LogUtils.makeLogTag;

public class DateHelper extends DateUtils {
	private static final String TAG = makeLogTag(DateHelper.class);

	public static final String DATE_FORMAT = "yyyyMMddHHmmss";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
	public static final String DATE_FORMAT_READABLE = "MMMM d, yyyy h:mm a z";
	private static final SimpleDateFormat dateFormatReadable =
			new SimpleDateFormat(DATE_FORMAT_READABLE, Locale.US);

	public static final String DATE_FORMAT_DAY_TIME = "EEE" + "\n" + "h:mm a";
	private static final SimpleDateFormat dateFormatDayTime =
			new SimpleDateFormat(DATE_FORMAT_DAY_TIME, Locale.US);

    public static final String DATE_FORMAT_DAY_TIME_SINGLE_LINE = "EEEE h:mm a";
    private static final SimpleDateFormat dateFormatDayTimeSingleLine =
            new SimpleDateFormat(DATE_FORMAT_DAY_TIME_SINGLE_LINE, Locale.US);

    public static final String DATE_FORMAT_TIME = "h:mm a";
    private static final SimpleDateFormat dateFormatTime =
            new SimpleDateFormat(DATE_FORMAT_TIME, Locale.US);

	// Cannot be instantiated
	private DateHelper() {
	}

	private static boolean isWithinWeek(final long millis) {
		return System.currentTimeMillis() - millis <= (Constants.WEEK_IN_MILLIS - Constants.DAY_IN_MILLIS);
	}

	private static boolean isWithinYear(final long millis) {
		return System.currentTimeMillis() - millis <= Constants.YEAR_IN_MILLIS;
	}

	public static String getBetterRelativeTimeSpanString(final Context c, final long millis) {
		int formatFlags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_TIME;
		if (!isToday(millis)) {
			if (isWithinWeek(millis)) {
				formatFlags |= DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY;
			} else if (isWithinYear(millis)) {
				formatFlags |= DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_ALL;
			} else {
				formatFlags |= DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL;
			}
		}
		return DateUtils.formatDateTime(c, millis, formatFlags);
	}

	public static String getDurationBreakdown(long millis) {
		if (millis < 0)
			throw new IllegalArgumentException("Invalid millisecond argument: " + millis);

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return (String.valueOf(days) + " Days " + hours + " Hours " + minutes + " Minutes " + seconds + " Seconds");
	}

	// Calendar used for primary Java data type because the implementation
	// of Androids DatePickerDialog is more closely aligned with the Calendar object.
	public static long formatDateAsLong(Calendar cal) {
		return Long.parseLong(dateFormat.format(cal.getTime()));
	}

	public static Calendar getCalendarFromFormattedLong(long l) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateFormat.parse(String.valueOf(l)));
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static long getNowFormattedAsLong() {
		return DateHelper.formatDateAsLong(Calendar.getInstance());
	}

	public static String getNowFormattedAsString() {
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	public static String getStringFromFormattedLong(long l) {
		String dateString;
		try {
			dateString = dateFormat.format(dateFormat.parse(String.valueOf(l)));
			return dateString;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getStringReadableFromFormattedLong(long l) {
		String dateString;
		try {
			dateString = dateFormatReadable.format(dateFormat.parse(String.valueOf(l)));
			return dateString;
		} catch (ParseException e) {
			LOGE(TAG, "ParseException: ", e);
			return null;
		}
	}

	public static String getDayTimeFromFormattedLong(long l) {
		String dateString;
		try {
			dateString = dateFormatDayTime.format(dateFormat.parse(String.valueOf(l)));
			// make AM PM lower case
			dateString = dateString.replace("AM", "am").replace("PM", "pm");
			return dateString;
		} catch (ParseException e) {
			LOGE(TAG, "ParseException: ", e);
			return null;
		}
	}

    public static String getDayTimeSingleLineFromFormattedLong(long l) {
        String dateString;
        try {
            dateString = dateFormatDayTimeSingleLine.format(dateFormat.parse(String.valueOf(l)));
            // make AM PM lower case
            dateString = dateString.replace("AM", "am").replace("PM", "pm");
            return dateString;
        } catch (ParseException e) {
            LOGE(TAG, "ParseException: ", e);
            return null;
        }
    }

    public static String getTimeFromFormattedLong(long l) {
        String dateString;
        try {
            dateString = dateFormatTime.format(dateFormat.parse(String.valueOf(l)));
            dateString = dateString.replace("AM", "am").replace("PM", "pm");
            return dateString;
        } catch (ParseException e) {
            LOGE(TAG, "ParseException: ", e);
            return null;
        }
    }

	public static String getTimeAgo(long time, Context ctx) {
		final int SECOND = 1000;
		final int MINUTE = 60 * SECOND;
		final int HOUR = 60 * MINUTE;
		final int DAY = 24 * HOUR;
		// TODO: use DateUtils methods instead
		if (time < 1000000000000L) {
			// if timestamp given in seconds, convert to millis
			time *= 1000;
		}

//		long now = UIUtils.getCurrentTime(ctx);
		long now = System.currentTimeMillis();
		if (time > now || time <= 0) {
			return null;
		}

		final long diff = now - time;
		if (diff < MINUTE) {
			return "just now";
		} else if (diff < 2 * MINUTE) {
			return "a minute ago";
		} else if (diff < 50 * MINUTE) {
			return diff / MINUTE + " minutes ago";
		} else if (diff < 90 * MINUTE) {
			return "an hour ago";
		} else if (diff < 24 * HOUR) {
			return diff / HOUR + " hours ago";
		} else if (diff < 48 * HOUR) {
			return "yesterday";
		} else {
			return diff / DAY + " days ago";
		}
	}

}
