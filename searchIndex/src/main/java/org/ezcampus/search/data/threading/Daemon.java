package org.ezcampus.search.data.threading;

import org.tinylog.Logger;

public abstract class Daemon extends Thread
{
	protected ThreadEvent _event = new ThreadEvent();
	
	public void doPrecall()
	{
		Logger.debug("Thread {} is doing a job", this.getName());
	}

	public String getCurrentJobSummary()
	{
		return "Unknown job";
	}

	public void shutdown()
	{
		ThreadHandling.shutdownThread(this);
		
		wake();
	}

	public void wake()
	{
		this._event.set();
	}
}
