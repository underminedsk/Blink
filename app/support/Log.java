package support;

import play.Logger;

public class Log {
	
	public static void debug(String TAG, String m) {
		Logger.debug(decorate(TAG) + m);
	}
	
	public static void error(String TAG, String m) {
		Logger.error(decorate(TAG) + m);
	}
	
	public static void error(String TAG, Throwable e) {
		Logger.error(decorate(TAG) + e.getMessage());
	}
	
	public static void warn(String TAG, String m) {
		Logger.warn(decorate(TAG) + m);
	}
	
	public static void info(String TAG, String m) {
		Logger.info(decorate(TAG) + m);
	}
	
	public static void trace(String TAG, String m) {
		Logger.trace(decorate(TAG) + m);
	}
	
	private static String decorate(String TAG) {
		return "[-" + TAG + "-]: ";
	}
}
