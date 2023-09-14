package org.ezcampus.search.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;

public class StringHelper
{
	public static String getEllapsedTimePretty(long elapsedTimeMS) 
	{
        return String.format("Elapsed Time: %s", TimeHelper.GetFormattedInterval(elapsedTimeMS));
	}
	
    public static String getSpecialCharacterMapping(String value)
    {
        switch (value)
        {
	        case "&&":
	        case "&":
	        	return "and";
	        case "||":
	        	return "or";
        }
        return null;
    }
	
    public static String getNumberMapping(String value)
    {
        switch (value)
        {
        case "0": return "zero";
        case "1": return "one";
        case "2": return "two";
        case "3": return "three";
        case "4": return "four";
        case "5": return "five";
        case "6": return "six";
        case "7": return "seven";
        case "8": return "eight";
        case "9": return "nine";
        }
        return null;
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
        }
        return null;
    }

	private static final Pattern CLEAN_REGEX = Pattern.compile("[^a-z0-9'\"-]+");

	public static String cleanWord(String word)
	{
		String trimmedWord = word.trim().toLowerCase();

		// Replace non-alphanumeric characters except apostrophe with empty string
		String cleanedWord = CLEAN_REGEX.matcher(trimmedWord).replaceAll("");

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
