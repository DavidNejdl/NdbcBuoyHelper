package org.nightra1n.ndbcbuoyhelper.model;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

import org.nightra1n.ndbcbuoyhelper.NDBC.NDBCFloatFormatter;
import org.nightra1n.ndbcbuoyhelper.NDBC.NDBCIntFormatter;

import java.util.Date;

@Record
public class FiveDayRecord {

	Date timestamp;

	float windSpeed;		// 0 to 62 m/s
	float windGust;			// 0 to 82 m/s
	int windDirection;		// 0 to 360 degrees

	float waveHeight;
	int waveDirection;		// 0 to 360 degrees
	int periodDominant;
	float periodAverage;

	float pressure;
	float airTemp;
	float waterTemp;
	float dewPoint;

//	float visibility;
//	int ptdy;
//	float tide;

	@Field(offset = 1, length = 16)
	@FixedFormatPattern("yyyy MM dd hh mm")
	public Date getTimestamp() { return timestamp; }
	public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }


	@Field(offset = 18, length = 3, formatter = NDBCIntFormatter.class)
	public int getWindDirection() { return windDirection; }
	public void setWindDirection(int windDirection) { this.windDirection = windDirection; }

	@Field(offset = 22, length = 4, formatter = NDBCFloatFormatter.class)
	public float getWindSpeed() { return windSpeed; }
	public void setWindSpeed(float windSpeed) { this.windSpeed = windSpeed; }

	@Field(offset = 27, length = 4, formatter = NDBCFloatFormatter.class)
	public float getWindGust() { return windGust; }
	public void setWindGust(float windGust) { this.windGust = windGust; }

	@Field(offset = 33, length = 4, formatter = NDBCFloatFormatter.class)
	public float getWaveHeight() { return waveHeight; }
	public void setWaveHeight(float waveHeight) { this.waveHeight = waveHeight; }

	@Field(offset = 41, length = 2, formatter = NDBCIntFormatter.class)
	public int getPeriodDominant() { return periodDominant; }
	public void setPeriodDominant(int periodDominant) { this.periodDominant = periodDominant; }

	@Field(offset = 45, length = 4, formatter = NDBCFloatFormatter.class)
	public float getPeriodAverage() { return periodAverage; }
	public void setPeriodAverage(float periodAverage) { this.periodAverage = periodAverage; }

	@Field(offset = 50, length = 3, formatter = NDBCIntFormatter.class)
	public int getWaveDirection() { return waveDirection; }
	public void setWaveDirection(int waveDirection) { this.waveDirection = waveDirection; }

	@Field(offset = 54, length = 6, formatter = NDBCFloatFormatter.class)
	public float getPressure() { return pressure; }
	public void setPressure(float pressure) { this.pressure = pressure; }

	@Field(offset = 61, length = 5, formatter = NDBCFloatFormatter.class)
	public float getAirTemp() { return airTemp; }
	public void setAirTemp(float airTemp) { this.airTemp = airTemp; }

	@Field(offset = 67, length = 5, formatter = NDBCFloatFormatter.class)
	public float getWaterTemp() { return waterTemp; }
	public void setWaterTemp(float waterTemp) { this.waterTemp = waterTemp; }

	@Field(offset = 73, length = 5, formatter = NDBCFloatFormatter.class)
	public float getDewPoint() { return dewPoint; }
	public void setDewPoint(float dewPoint) { this.dewPoint = dewPoint; }


}
