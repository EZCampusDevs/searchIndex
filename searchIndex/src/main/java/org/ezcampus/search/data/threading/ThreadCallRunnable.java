package org.ezcampus.search.data.threading;

import org.ezcampus.search.data.exceptions.ThreadShuttingDownException;

public interface ThreadCallRunnable
{
	public abstract String getJobSummary();
	
	public abstract void run() throws ThreadShuttingDownException;
}
