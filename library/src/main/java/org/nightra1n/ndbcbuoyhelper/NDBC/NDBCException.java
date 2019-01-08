package org.nightra1n.ndbcbuoyhelper.NDBC;

public class NDBCException extends Exception {

	public NDBCException(String detailMessage) {
		super(detailMessage);
	}

	public NDBCException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
