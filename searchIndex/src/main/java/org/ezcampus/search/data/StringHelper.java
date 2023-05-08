package org.ezcampus.search.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;

public class StringHelper
{

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
