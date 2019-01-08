package org.nightra1n.ndbcbuoyhelper.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Station extends Buoy implements Parcelable {

	String type;
	boolean waves;
	boolean wind;
	boolean temp;

	public Station() {

	}

	public Station(String ndbc_id, String ndbc_desc, String rssTitle, double georss_lat, double georss_lon, long timestamp) {
		super(ndbc_id, ndbc_desc, rssTitle, georss_lat, georss_lon, timestamp);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isWaves() {
		return waves;
	}

	public void setWaves(boolean waves) {
		this.waves = waves;
	}

	public boolean isWind() {
		return wind;
	}

	public void setWind(boolean wind) {
		this.wind = wind;
	}

	public boolean isTemp() {
		return temp;
	}

	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	@Override
	public String toString() {
		String payload = "";
		if (this.waves) payload = "Waves";
		if (this.wind) payload = payload + ", Wind";
		if (this.temp) payload = payload + ", Temp";

		return "Station [NDBC_ID: " + this.NDBC_id
				+ ", NDBC_description: " + this.NDBC_description
				+ ", Lat: " + Double.toString(this.georss_lat) + "\n"
				+ ", Lon: " + Double.toString(this.georss_lon) + "\n"
				+ ", Type: " + this.type + "\n"
				+ ", Data: " + payload
				+ "";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Station)) {
			return false;
		}

		Station s = (Station) o;
		return this.NDBC_id.equals(s.NDBC_id);
	}

	// Parcelable Stuff
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
//        Buoy buoy = null;
//        try {
//            buoy = (Buoy) this.clone();
//            buoy.writeToParcel(parcel, flags);
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
        super.writeToParcel(parcel, flags);
//		parcel.writeParcelable(buoy, flags);
		parcel.writeString(this.type);
		parcel.writeValue(this.waves);
		parcel.writeValue(this.wind);
		parcel.writeValue(this.temp);

	}

	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
	public static final Creator<Station> CREATOR = new Creator<Station>() {
		public Station createFromParcel(Parcel in) {
			return new Station(in);
		}

		public Station[] newArray(int size) {
			return new Station[size];
		}
	};

	private Station(Parcel in) {
		// Buoy part
		Buoy buoy = in.readParcelable(Buoy.class.getClassLoader());
		NDBC_id = buoy.NDBC_id;
		NDBC_description = buoy.NDBC_description;
		rssTitle = buoy.rssTitle;
		georss_lat = buoy.georss_lat;
		georss_lon = buoy.georss_lon;
		timestamp = buoy.timestamp;
//		in.readTypedList(observations, BuoyObservation.CREATOR);
		observations = buoy.getObservations();

		// Station stuff
		this.type = in.readString();
		this.waves = (Boolean) in.readValue(null);
		this.wind = (Boolean) in.readValue(null);
		this.temp = (Boolean) in.readValue(null);
	}
}
