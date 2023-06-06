package org.ezcampus.search.data.threading;

import java.util.LinkedList;
import java.util.Queue;

import org.ezcampus.search.data.exceptions.ThreadShuttingDownException;
import org.tinylog.Logger;

public class ThreadCallToThread extends Daemon
{
	private ThreadCallRunnable _currentJob = null;

	private Queue<ThreadCallRunnable> _jobQueue = new LinkedList<>();

	private boolean _isWorking = true;

	public ThreadCallToThread() {
		
	}
	
	public ThreadCallToThread(String name) {
		super.setName(name);
	}
	
	public boolean isWorking()
	{
		return _isWorking;
	}

	public void put(ThreadCallRunnable job)
	{

		_isWorking = true;

		_jobQueue.offer(job);

		super._event.set();
	}

	@Override
	public String getCurrentJobSummary()
	{
		return this._currentJob.getJobSummary();
	}

	@Override
	public void doPrecall() {
		Logger.info("CallToThread [{}] is doing a job. Description: {}", this.getName(), this.getCurrentJobSummary());
	}
	
	@Override
	public final void run()
	{
		try
		{
			while (true)
			{
				while (this._jobQueue.isEmpty())
				{
					ThreadHandling.dieIfShuttingDown();

					super._event.doWait(10f);

					super._event.clear();
				}

				ThreadHandling.dieIfShuttingDown();

				try
				{
					this._currentJob = this._jobQueue.poll();

					if (this._currentJob == null)
						continue;

					this.doPrecall();

					this._currentJob.run();

					this._currentJob = null;
				}
				catch (ThreadShuttingDownException e)
				{
					return;
				}
				finally
				{
					this._isWorking = false;
				}
			}
		}
		catch (ThreadShuttingDownException | InterruptedException e)
		{
			// TODO: handle exception
		}
	}
}
