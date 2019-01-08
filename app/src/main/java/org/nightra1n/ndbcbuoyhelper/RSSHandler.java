package org.nightra1n.ndbcbuoyhelper;

import org.nightra1n.ndbcbuoyhelper.NDBC.NDBC;
import org.nightra1n.ndbcbuoyhelper.helper.DateHelper;
import org.nightra1n.ndbcbuoyhelper.helper.LogUtils;
import org.nightra1n.ndbcbuoyhelper.model.Buoy;
import org.nightra1n.ndbcbuoyhelper.model.BuoyObservation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RSSHandler extends DefaultHandler {
	private static final String TAG = LogUtils.makeLogTag(RSSHandler.class);

	private ArrayList<Buoy> buoyList;
	private Buoy buoy;
	private StringBuffer chars;

	private final int stateIn = 1;
	private final int stateOut = 0;
	private int inItem = stateOut;
	private int itemCount = 0;
	private int maxItems;
	private int skipCount = 0;

	private static final String WATER_TEMPERATURE = "Water Temperature:";
	private static final String AIR_TEMPERATURE = "Air Temperature:";
	private static final String ATMOSPHERIC_PRESSURE = "Atmospheric Pressure:";
	private static final String MEAN_WAVE_DIRECTION = "Mean Wave Direction:";
	private static final String PERIOD_AVERAGE = "Average Period:";
	private static final String PERIOD_DOMINANT = "Dominant Wave Period:";
	private static final String WAVE_HEIGHT_SIG = "Significant Wave Height:";
	private static final String WIND_GUST = "Wind Gust:";
	private static final String WIND_SPEED = "Wind Speed:";
	private static final String WIND_DIRECTION = "Wind Direction:";
//    private static final String LOCATION = "Location:";

	public RSSHandler(int maxItems) {
		this.maxItems = maxItems;
	}

	public Buoy getBuoy() {
		return this.buoy;
	}

	public ArrayList<Buoy> getBuoyList() {
		return this.buoyList;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		buoyList = new ArrayList<Buoy>();
		chars = new StringBuffer();
	}

	@Override
	public void endDocument() {
		LogUtils.LOGV(TAG, "Fetched " + itemCount + " items. Skipped " + skipCount);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
							 Attributes attributes) {
		chars = new StringBuffer();
		if (localName.equalsIgnoreCase("item")) {
			itemCount++;
			if (itemCount <= maxItems) {
				inItem = stateIn;
				buoy = new Buoy();
//                LOGV(TAG, "Processing Buoy #" + itemCount);
			} else {
//                LOGV(TAG, "Skipping Buoy #" + itemCount);
				skipCount++;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {

		if (localName.equalsIgnoreCase("item") && (inItem == stateIn)) {
			inItem = stateOut;
            // Only add if valid
            if (NDBC.isValidNDBCid(buoy.getNDBC_ID())) {
                buoyList.add(buoy);
                LogUtils.LOGV(TAG, "Added Buoy Item #" + itemCount + ": " + buoy.toString());
            } else {
                LogUtils.LOGE(TAG, "Invalid NDBC ID: " + buoy.getNDBC_ID());
            }
		}

		if (inItem == stateIn) {
			if (localName.equalsIgnoreCase("title")) {
				buoy.setRssTitle(chars.toString().trim());
				// NDBC ID & Description handled with setRssTitle
			} else if (qName.equalsIgnoreCase("georss:point")) {
				// <georss:point>40.969 -71.127</georss:point>
				try {
					Pattern pattern = Pattern.compile("(-?\\d{1,3}\\.\\d{3})\\s(-?\\d{1,3}\\.\\d{3})");
					Matcher matcher = pattern.matcher(chars.toString().trim());
					if (matcher.find()) {
						buoy.setGeoRssLat(Double.parseDouble(matcher.group(1)));
						buoy.setGeoRssLon(Double.parseDouble(matcher.group(2)));
					}
				} catch (PatternSyntaxException e) {
					LogUtils.LOGV(TAG, "PatternSyntaxException: toString()=" + e.toString());
				} catch (Exception e) {
					LogUtils.LOGV(TAG, "Exception: toString()=" + e.toString());
				}
//            } else if (localName.equalsIgnoreCase("pubDate")) {
//                try {
//                    // Tue, 02 Jul 2013 14:08:24 UT
//                    // Replace "UT" with "GMT"? UT won't parse?
//                    String pubDate;
//                    if (chars.indexOf("UT") != -1) {
//                        pubDate = chars.toString().trim().replace("UT", "GMT");
//                    } else {
//                        LOGV(TAG, "Test result: " + chars.toString().trim().replace("UT", "GMT"));
//                        pubDate = chars.toString().trim();
//                    }
//                    Date result = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).parse(pubDate);
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(result);
//                    long dateAsLong = DateHelper.formatDateAsLong(cal);
//
//                } catch (ParseException e) {
//                    LOGV(TAG, "ParserException: " + e.toString());
//                    e.printStackTrace();
//                } catch (IllegalArgumentException e) {
//                    LOGV(TAG, "IllegalArgumentException: " + e.toString());
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    LOGV(TAG, "Exception: " + e.toString());
//                    e.printStackTrace();
//                }
			} else if (localName.equalsIgnoreCase("description")) {
				BuoyObservation buoyObservation = new BuoyObservation();
				String inputClean = chars.toString();
				int x;

				try {
					inputClean = inputClean.replaceAll("<strong>", "");
					inputClean = inputClean.replaceAll("</strong>", "");
					inputClean = inputClean.replaceAll("&#176;", "");
				} catch (Exception e) {
					LogUtils.LOGV("RSSHandler", "Regex Exception in RSSHandler: " + e.getMessage());
				}

				String[] fields = inputClean.split("<br />");

				try {
					// August 26, 2014 9:45 pm EDT
					// August 27, 2014 01:00 UTC
					// hack to replace "UT" with GMT (UT won't parse)
					SimpleDateFormat DATE_FORMAT_A = new SimpleDateFormat("MMMM dd, yyyy h:mm a z", Locale.US);
					SimpleDateFormat DATE_FORMAT_B = new SimpleDateFormat("MMMM dd, yyyy HH:mm z", Locale.US);

					String dateString = fields[0].trim().replace("UTC", "GMT").replace("UT", "GMT");
					Date descDate = null;
					try {
						descDate = DATE_FORMAT_A.parse(dateString);
					} catch (ParseException e) {
						// Swallow
					}
					if (descDate == null) {
						try {
							descDate = DATE_FORMAT_B.parse(dateString);
						} catch (ParseException e) {
							LogUtils.LOGV(TAG, "ParserException: " + e.toString());
						}
					}

					Calendar cal = Calendar.getInstance();
					cal.setTime(descDate);
					long dateAsLong = DateHelper.formatDateAsLong(cal);
					buoyObservation.setDescDate(dateAsLong);
//                    LOGV(TAG, "Formatted Desc Date " + fields[0] + " to " + dateAsLong);
				} catch (IllegalArgumentException e) {
					LogUtils.LOGV(TAG, "IllegalArgumentException: " + e.toString());
				} catch (Exception e) {
					LogUtils.LOGV(TAG, "Exception: " + e.toString());
				}

				for (String field : fields) {
					try {
						x = field.indexOf(WIND_DIRECTION);
						if (x != -1) {
							buoyObservation.setWindDirection(field.substring(x + WIND_DIRECTION.length(), field.length()).trim());
							continue;
						}
						x = field.indexOf(WIND_SPEED);
						if (x != -1) {
							buoyObservation.setWindSpeed(field.substring(x + WIND_SPEED.length(), field.length()).trim());
							continue;
						}
						x = field.indexOf(WIND_GUST);
						if (x != -1) {
							buoyObservation.setWindGust(field.substring(x + WIND_GUST.length(), field.length()).trim());
							continue;
						}
						x = field.indexOf(WAVE_HEIGHT_SIG);
						if (x != -1) {
							// Convert to float
							String tempString = field.substring(x + WAVE_HEIGHT_SIG.length(), field.indexOf(" ft"));
							float y = Float.parseFloat(tempString);
							buoyObservation.setWaveHeightSignificant(y);
							continue;
						}
						x = field.indexOf(PERIOD_DOMINANT);
						if (x != -1) {
							// Convert to float
							String tempString = field.substring(x + PERIOD_DOMINANT.length(), field.indexOf(" sec"));
							float y = Float.parseFloat(tempString);
							buoyObservation.setPeriodDominant(y);
							continue;
						}
						x = field.indexOf(PERIOD_AVERAGE);
						if (x != -1) {
							// Convert to float
							String tempString = field.substring(x + PERIOD_AVERAGE.length(), field.indexOf(" sec"));
							float y = Float.parseFloat(tempString);
							buoyObservation.setAveragePeriod(y);
							continue;
						}
						x = field.indexOf(MEAN_WAVE_DIRECTION);
						if (x != -1) {
							buoyObservation.setWaveDirection(field.substring(x + MEAN_WAVE_DIRECTION.length(), field.length()).trim());
							continue;
						}
						x = field.indexOf(ATMOSPHERIC_PRESSURE);
						if (x != -1) {
							//
							continue;
						}
						x = field.indexOf(AIR_TEMPERATURE);
						if (x != -1) {
							String tempString = field.substring(x + AIR_TEMPERATURE.length(), field.length());
                            float tempF = parseF(tempString);
                            float tempC = parseC(tempString);
                            LogUtils.LOGV(TAG, "Setting Air Temp F = " + Float.toString(tempF) + ", C = " + Float.toString(tempC));
                            buoyObservation.setAirTempF(tempF);
							buoyObservation.setAirTempC(tempC);
							continue;
						}
						x = field.indexOf(WATER_TEMPERATURE);
						if (x != -1) {
							String tempString = field.substring(x + WATER_TEMPERATURE.length(), field.length());
                            float tempF = parseF(tempString);
                            float tempC = parseC(tempString);
                            LogUtils.LOGV(TAG, "Setting Water Temp F = " + Float.toString(tempF) + ", C = " + Float.toString(tempC));
                            buoyObservation.setWaterTempF(tempF);
							buoyObservation.setWaterTempC(tempC);
						}
					} catch (NumberFormatException e) {
						LogUtils.LOGV(TAG, "NumberFormatException: " + e.toString());
						e.printStackTrace();
					}
				}
				// Add observation to Buoy
				buoy.addObservation(buoyObservation);
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		if (inItem == stateIn) {
			chars.append(new String(ch, start, length));
		}
	}

	private float parseF(String input) {
		return Float.parseFloat(input.substring(0, input.indexOf("F")).trim());
	}

	private float parseC(String input) {
		return Float.parseFloat(input.substring(input.indexOf("(") + 1, input.indexOf("C")));
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		LogUtils.LOGV("RSSHandler", e.getMessage());
		super.warning(e);
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		LogUtils.LOGV("RSSHandler", e.getMessage());
		super.error(e);
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		LogUtils.LOGV("RSSHandler", e.getMessage());
		super.fatalError(e);
	}

}
