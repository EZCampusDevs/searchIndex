package org.ezcampus.search.data;

public class TimeHelper
{

	public static boolean timeHasPassed(long time) {
		return System.currentTimeMillis() > time;
	}
	
}
