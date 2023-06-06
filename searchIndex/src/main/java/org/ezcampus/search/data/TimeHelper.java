package org.ezcampus.search.data;

public class TimeHelper
{

	public static boolean timeHasPassed(long time) {
		return System.currentTimeMillis() > time;
	}
	
	public static String GetFormattedInterval(final long ms) {
	    long millis = ms % 1000;
	    long x = ms / 1000;
	    long seconds = x % 60;
	    x /= 60;
	    long minutes = x % 60;
	    x /= 60;
	    long hours = x % 24;

	    return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
	}
}
