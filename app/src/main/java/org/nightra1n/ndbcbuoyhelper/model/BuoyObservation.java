package org.nightra1n.ndbcbuoyhelper.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.nightra1n.ndbcbuoyhelper.helper.Constants;
import org.nightra1n.ndbcbuoyhelper.helper.DateHelper;
import org.nightra1n.ndbcbuoyhelper.helper.LogUtils;


public class BuoyObservation implements Parcelable {
	private static final String TAG = LogUtils.makeLogTag(BuoyObservation.class);

	protected int id;
	protected String ndbcId;
	protected long pubDate;
	protected long descDate;
	protected long fetchDate;

	protected String windSpeed;		// knots
	protected String windGust;		// knots
	protected String windDirection;	// ESE (110*)

	protected float waveHeightSignificant;
	protected String waveDirection;
	protected float periodDominant;
	protected float periodAverage;

	protected float airTempF;
	protected float waterTempF;
	protected float airTempC;
	protected float waterTempC;

	public BuoyObservation() { }

	private String extractCData(String data) {
		data = data.replaceAll("<!\\[CDATA\\]", "");
		data = data.replaceAll("\\]\\]", "");
		return data;
	}

	@Override
	public String toString() {
		String str = "";
//		if (!TextUtils.isEmpty(ndbcId)) str = str.concat("NDBC Id: " + ndbcId + "\n");
		if (descDate != 0)
			str = str.concat(DateHelper.getStringReadableFromFormattedLong(descDate) + "\n");
		if (waveHeightSignificant != 0)
			str = str.concat("Wave Height: " + waveHeightSignificant + " ft" + "\n");
		if (waveDirection != null) str = str.concat("Wave Direction: " + waveDirection + "\n");
		if (periodAverage != 0)
			str = str.concat("Average Period: " + periodAverage + " sec" + "\n");
		if (periodDominant != 0)
			str = str.concat("Dominant Period: " + periodDominant + " sec" + "\n");
		if (windSpeed != null) str = str.concat("Wind Speed: " + windSpeed + " kts" + "\n");
		if (windGust != null) str = str.concat("Wind Gust: " + windGust + " kts" + "\n");
		if (windDirection != null) str = str.concat("Wind Direction: " + windDirection + "\n");
		if (airTempF != 0)
			str = str.concat("Air Temp: " + airTempF + Constants.UNICODE_DEGREE_FAHRENHEIT + "\n");
		if (waterTempF != 0)
			str = str.concat("Water Temp: " + waterTempF + Constants.UNICODE_DEGREE_FAHRENHEIT + "\n");
		if (pubDate != 0)
			str = str.concat("Pub Date: " + DateHelper.getStringReadableFromFormattedLong(pubDate) + "\n");
		if (fetchDate != 0)
			str = str.concat("Fetch Date: " + DateHelper.getStringReadableFromFormattedLong(fetchDate) + "\n");
		return str;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getNdbcId() {
		return ndbcId;
	}
	public void setNdbcId(String ndbcId) {
		this.ndbcId = ndbcId;
	}

	public long getPubDate() {
		return pubDate;
	}
	public void setPubDate(long pubDate) {
		this.pubDate = pubDate;
	}

	public long getDescDate() {
		return descDate;
	}
	public void setDescDate(long descDate) {
		this.descDate = descDate;
	}

	public long getFetchDate() {
		return fetchDate;
	}
	public void setFetchDate(long fetchDate) {
		this.fetchDate = fetchDate;
	}

	public String getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWindGust() {
		return windGust;
	}
	public void setWindGust(String windGust) {
		this.windGust = windGust;
	}

	public String getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public float getWaveHeightSignificant() {
		return waveHeightSignificant;
	}
	public void setWaveHeightSignificant(float waveHeightSignificant) {
		this.waveHeightSignificant = waveHeightSignificant;
	}

	public float getPeriodDominant() {
		return periodDominant;
	}
	public void setPeriodDominant(float periodDominant) {
		this.periodDominant = periodDominant;
	}

	public float getAveragePeriod() {
		return periodAverage;
	}
	public void setAveragePeriod(float averagePeriod) {
		this.periodAverage = averagePeriod;
	}

	public String getWaveDirection() {
		return waveDirection;
	}
	public void setWaveDirection(String waveDirection) {
		this.waveDirection = waveDirection;
	}

	public float getAirTempF() {
		return airTempF;
	}
	public void setAirTempF(float airTempF) {
		this.airTempF = airTempF;
	}

	public float getWaterTempF() {
		return waterTempF;
	}
	public void setWaterTempF(float waterTempF) {
		this.waterTempF = waterTempF;
	}

	public float getAirTempC() {
		return airTempC;
	}
	public void setAirTempC(float airTempC) {
		this.airTempC = airTempC;
	}

	public float getWaterTempC() {
		return waterTempC;
	}
	public void setWaterTempC(float waterTempC) {
		this.waterTempC = waterTempC;
	}


	// 99.9% of the time you can just ignore this
	@Override
	public int describeContents() {
		return 0;
	}

	// write your object's data to the passed-in Parcel
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(id);
		// Observe that in the case you have more than one field to retrieve from a given Parcel,
		// you must do this in the same order you put them in (that is, in a FIFO approach).
		parcel.writeString(ndbcId);
		parcel.writeLong(pubDate);
		parcel.writeLong(descDate);
		parcel.writeLong(fetchDate);
		parcel.writeString(windSpeed);
		parcel.writeString(windGust);
		parcel.writeString(windDirection);
		parcel.writeFloat(waveHeightSignificant);
		parcel.writeFloat(periodDominant);
		parcel.writeFloat(periodAverage);
		parcel.writeString(waveDirection);
		parcel.writeFloat(airTempF);
		parcel.writeFloat(waterTempF);
		parcel.writeFloat(airTempC);
		parcel.writeFloat(waterTempC);
	}

	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
	public static final Creator<BuoyObservation> CREATOR = new Creator<BuoyObservation>() {
		public BuoyObservation createFromParcel(Parcel in) {
			return new BuoyObservation(in);
		}

		public BuoyObservation[] newArray(int size) {
			return new BuoyObservation[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated with it's values
	private BuoyObservation(Parcel in) {
		id = in.readInt();
		ndbcId = in.readString();
		pubDate = in.readLong();
		descDate = in.readLong();
		fetchDate = in.readLong();
		windSpeed = in.readString();
		windGust = in.readString();
		windDirection = in.readString();
		waveHeightSignificant = in.readFloat();
		periodDominant = in.readFloat();
		periodAverage = in.readFloat();
		waveDirection = in.readString();
		airTempF = in.readFloat();
		waterTempF = in.readFloat();
		airTempC = in.readFloat();
		waterTempC = in.readFloat();
	}
}
