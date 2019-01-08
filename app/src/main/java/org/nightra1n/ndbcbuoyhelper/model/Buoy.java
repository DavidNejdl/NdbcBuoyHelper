package org.nightra1n.ndbcbuoyhelper.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.nightra1n.ndbcbuoyhelper.helper.DateHelper;
import org.nightra1n.ndbcbuoyhelper.helper.LogUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Buoy implements Parcelable {
	private static final String TAG = LogUtils.makeLogTag(Buoy.class);

	public static final int PAYLOAD_WAVES = 1;
	public static final int PAYLOAD_WIND = 2;
	public static final int PAYLOAD_TEMP = 4;

	String NDBC_id;
	String NDBC_description;
	String rssTitle;
	double georss_lat;
	double georss_lon;
	int payload;
	long timestamp;
	ArrayList<BuoyObservation> observations = new ArrayList<BuoyObservation>();

	public Buoy() {
		this.timestamp = DateHelper.getNowFormattedAsLong();
	}

	/**
	 * Parse rssTitle constructor
	 */
	public Buoy(String rssTitle, double georss_lat, double georss_lon) {
		this();     // sets timestamp
		this.rssTitle = rssTitle;
		parseNDBC_Id();             // parse ndbc id, description & rssUrl
		this.georss_lat = georss_lat;
		this.georss_lon = georss_lon;
	}

	/**
	 * Full Constructor
	 */
	public Buoy(String ndbc_id, String ndbc_desc, String rssTitle,
				double georss_lat, double georss_lon, long timestamp) {
		this.NDBC_id = ndbc_id;
		this.NDBC_description = ndbc_desc;
		this.rssTitle = rssTitle;
		this.georss_lat = georss_lat;
		this.georss_lon = georss_lon;
		this.timestamp = timestamp;
	}

	/**
	 * Observations Functions
	 */
	public void addObservation(BuoyObservation observation) {
		observations.add(observation);
	}
	public void addObservations(ArrayList<BuoyObservation> observations) { observations.addAll(observations); }
	public ArrayList<BuoyObservation> getObservations() {
		return this.observations;
	}
	public BuoyObservation getObservationLatest() {
		if (this.observations.size() > 0) {
			return this.observations.get(this.observations.size() - 1);
		} else {
			return null;
		}
	}
	public int getObservationCount() { return this.observations.size(); }

	public String getNDBC_ID() {
		return this.NDBC_id;
	}
	public void setNDBC_ID(String ndbc_id) {
		this.NDBC_id = ndbc_id;
	}

	public String getNDBC_Description() {
		return this.NDBC_description;
	}
	public void setNDBC_description(String ndbc_description) { this.NDBC_description = ndbc_description; }

	public String getRssTitle() {
		return rssTitle;
	}
	public void setRssTitle(String rssTitle) {
		// TODO: remove (159) part
		this.rssTitle = rssTitle;

		// if NDBC id & description are empty, parse them from the title
		if (this.NDBC_id == null && this.NDBC_description == null) {
			parseNDBC_Id();
		}
	}

	public double getGeoRssLat() {
		return this.georss_lat;
	}
	public void setGeoRssLat(double lat) {
		this.georss_lat = lat;
	}

	public double getGeoRssLon() {
		return this.georss_lon;
	}
	public void setGeoRssLon(double lon) {
		this.georss_lon = lon;
	}

	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public void updateTimestamp() {
		this.timestamp = DateHelper.getNowFormattedAsLong();
	}

	private void parseNDBC_Id() {
		try {
			Pattern titlePattern = Pattern.compile("Station\\s([A-Z0-9]{4,6})\\s-\\s(.*)");
			Matcher matcher = titlePattern.matcher(this.rssTitle);
			if (matcher.find()) {
				this.NDBC_id = matcher.group(1);
				this.NDBC_description = matcher.group(2);
			}

		} catch (PatternSyntaxException e) {
			LogUtils.LOGV(TAG, "PatternSyntaxException: " + e.toString());
		} catch (Exception e) {
			LogUtils.LOGV(TAG, "Exception: " + e.toString());
		}
	}

	public int getPayload() { return payload; }
	public void setPayload(int payload) { this.payload = payload; }

	@Override
	public String toString() {
		return "Buoy [NDBC_ID: " + this.NDBC_id
				+ ", NDBC_description: " + this.NDBC_description
				+ ", RSS_title: " + this.rssTitle
				+ ", Lat: " + Double.toString(this.georss_lat)
				+ ", Lon: " + Double.toString(this.georss_lon)
				+ ", timestamp: " + ((this.timestamp == (long) 0) ? "null" : DateHelper.getStringFromFormattedLong(this.timestamp))
				+ ", observation count: " + this.observations.size()
				+ "]";
	}

	// 99.9% of the time you can just ignore this
	@Override
	public int describeContents() {
		return 0;
	}

	// write your object's data to the passed-in Parcel
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(NDBC_id);
		// Observe that in the case you have more than one field to retrieve from a given Parcel,
		// you must do this in the same order you put them in (that is, in a FIFO approach).
		parcel.writeString(NDBC_description);
		parcel.writeString(rssTitle);
		parcel.writeDouble(georss_lat);
		parcel.writeDouble(georss_lon);
		parcel.writeLong(timestamp);
//		parcel.writeTypedList(observations);
//		parcel.writeParcelableArray((Parcelable[]) observations.toArray(), 0);
//		parcel.createTypedArrayList(BuoyObservation.CREATOR);
		parcel.writeTypedList(observations);
//		Bundle bundle = new Bundle();
//		bundle.putParcelableArrayList("observations", observations);
//		parcel.writeBundle(bundle);
	}

	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
	public static final Creator<Buoy> CREATOR = new Creator<Buoy>() {
		public Buoy createFromParcel(Parcel in) {
			return new Buoy(in);
		}

		public Buoy[] newArray(int size) {
			return new Buoy[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated with it's values
	private Buoy(Parcel in) {
		NDBC_id = in.readString();
		NDBC_description = in.readString();
		rssTitle = in.readString();
		georss_lat = in.readDouble();
		georss_lon = in.readDouble();
		timestamp = in.readLong();
//		in.readTypedList(observations, BuoyObservation.CREATOR);
		observations = in.createTypedArrayList(BuoyObservation.CREATOR);
//		BuoyObservation[] obArray = (BuoyObservation[]) in.readParcelableArray(BuoyObservation.class.getClassLoader());
//		observations = in.createTypedArrayList(BuoyObservation.CREATOR);

//		Bundle bundle = in.readBundle(ClassLoader.getSystemClassLoader());
//		observations = bundle.getParcelableArrayList("observations");
	}

}
