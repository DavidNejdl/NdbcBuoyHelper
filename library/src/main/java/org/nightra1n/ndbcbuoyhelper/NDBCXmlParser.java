package org.nightra1n.ndbcbuoyhelper;

import org.nightra1n.ndbcbuoyhelper.helper.Constants;
import org.nightra1n.ndbcbuoyhelper.helper.LogUtils;
import org.nightra1n.ndbcbuoyhelper.model.Buoy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class NDBCXmlParser {
	private static final String TAG = LogUtils.makeLogTag(NDBCXmlParser.class);

	final URL rssUrl;

	public NDBCXmlParser(String url) {
        LogUtils.LOGV(TAG, "NDBCXmlParser: url = " + url);
        try {
            this.rssUrl = new URL(url);
        } catch (MalformedURLException e) {
            LogUtils.LOGV(TAG, "MalformedURLException: " + e.toString());
            throw new RuntimeException(e);
        }
    }

	public ArrayList<Buoy> parse(int maxItems) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			saxParserFactory.setNamespaceAware(true);
			SAXParser saxParser = saxParserFactory.newSAXParser();
			RSSHandler rssHandler = new RSSHandler(maxItems);

			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(rssHandler);
			xmlReader.setErrorHandler(rssHandler);
			xmlReader.parse(new InputSource(getInputStream()));

            ArrayList<Buoy> results = rssHandler.getBuoyList();
            LogUtils.LOGV(TAG, "parse(" + maxItems + "): returning " + results.size() + " buoys");

			return results;
		} catch (MalformedURLException e) {
			LogUtils.LOGV(TAG, "MalformedURLException: " + e.toString());
		} catch (ParserConfigurationException e) {
			LogUtils.LOGV("RSS Handler Parser Config", e.toString());
		} catch (SAXException e) {
			LogUtils.LOGV("RSS Handler SAX", e.toString());
		} catch (IOException e) {
			LogUtils.LOGV("RSS Handler IO", e.getMessage() + " >> " + e.toString());
		} catch (Exception e) {
			LogUtils.LOGV(TAG, e.toString());
		}
		return null;
	}

	private InputStream getInputStream() {
		try {
			HttpsURLConnection urlConnection = (HttpsURLConnection) rssUrl.openConnection();
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			((HttpsURLConnection) urlConnection).setSSLSocketFactory(socketFactory);
			urlConnection.setConnectTimeout(Constants.DEFAULT_TIMEOUT_CONNECT);
			urlConnection.setReadTimeout(Constants.DEFAULT_TIMEOUT_READ);
			urlConnection.setInstanceFollowRedirects(true);

			return urlConnection.getInputStream();
		} catch (IOException e) {
			LogUtils.LOGV(TAG, "IOException: " + e.toString());
			throw new RuntimeException();
		}
	}
}
