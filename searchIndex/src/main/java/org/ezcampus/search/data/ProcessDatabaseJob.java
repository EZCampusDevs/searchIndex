package org.ezcampus.search.data;

import org.ezcampus.search.data.exceptions.ThreadShuttingDownException;
import org.ezcampus.search.data.threading.ThreadCallCallback;

public class ProcessDatabaseJob extends ThreadCallCallback
{
	@Override
	public String getJobSummary()
	{
		return "Processes the latest scraped data in the database.";
	}

	@Override
	protected void runWithParams(Object... args) throws ThreadShuttingDownException
	{
		DatabaseProcessing.processDatabase();
	}

}
