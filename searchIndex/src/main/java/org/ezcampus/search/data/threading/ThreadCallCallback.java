package org.ezcampus.search.data.threading;

import org.ezcampus.search.data.exceptions.ThreadShuttingDownException;

public abstract class ThreadCallCallback implements ThreadCallRunnable
{

	protected Object[] params;

	public ThreadCallCallback(Object... args)
	{
		this.params = args;
	}

	public void setParams(Object... args)
	{
		this.params = args;
	}

	public Object[] getParams()
	{
		return this.params;
	}

	@Override
	public final void run() throws ThreadShuttingDownException
	{
		runWithParams(params);
	}

	protected abstract void runWithParams(Object... args) throws ThreadShuttingDownException;
}
