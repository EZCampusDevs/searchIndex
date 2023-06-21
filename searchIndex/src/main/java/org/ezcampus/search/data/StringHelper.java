package org.ezcampus.search.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;

public class StringHelper
{
	public static String getEllapsedTimePretty(long startTimeNano) {

		long elapsedTime = System.nanoTime() - startTimeNano;

		// nanoseconds to milliseconds
        long elapsedTimeMillis = (long) (elapsedTime / 1e6);

        return String.format("Elapsed Time: %s", TimeHelper.GetFormattedInterval(elapsedTimeMillis));

	}
	
    public static String getRomanNumeralMapping(String value)
    {
        switch (value)
        {
            case "I":
                return "1";
            case "II":
                return "2";
            case "III":
                return "3";
            case "IV":
                return "4";
            case "V":
                return "5";
            default:
                return null;
        }
    }

	public static String cleanWord(String word)
	{
		String trimmedWord = word.trim().toLowerCase();

		final String REGEX = "[^a-z0-9']+";
		
		// Replace non-alphanumeric characters except apostrophe with empty string
		String cleanedWord = Pattern.compile(REGEX).matcher(trimmedWord).replaceAll("");

		return cleanedWord;
	}

	public static String urlEncodeUTF8(String s)
	{
		try
		{
			return URLEncoder.encode(s, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new UnsupportedOperationException(e);
		}
	}

	public static String urlEncodeUTF8(Map<?, ?> map)
	{
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> entry : map.entrySet())
		{
			if (sb.length() > 0)
			{
				sb.append("&");
			}
			sb.append(
					String.format(
							"%s=%s", urlEncodeUTF8(entry.getKey().toString()),
							urlEncodeUTF8(entry.getValue().toString())
					)
			);
		}
		return sb.toString();
	}
}
