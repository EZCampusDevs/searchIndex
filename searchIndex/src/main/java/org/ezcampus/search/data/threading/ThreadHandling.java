package org.ezcampus.search.data.threading;

import java.util.ArrayList;
import java.util.HashMap;

import org.ezcampus.search.data.TimeHelper;
import org.ezcampus.search.data.exceptions.ThreadShuttingDownException;
import org.tinylog.Logger;

public class ThreadHandling
{
	private static HashMap<Thread, Boolean> _threadInfo = new HashMap<>();
	private static long _lastThreadClearour = System.currentTimeMillis();
	private static final long CLEAROUT_INTERVAL = 10 * 60 * 1000; // 10 minutes
	private static boolean _threadsInit = false;

	private static ThreadCallToThread _longRunningCallToThread = new ThreadCallToThread("LongRunningCallToThread");

	public static void initThreads()
	{
		Logger.info("Initializing threads...");
		_longRunningCallToThread.start();
		_threadsInit = true;
	}

	public static  void shutdownAllThreads()
	{
		Logger.info("Shutting down ALL threads...");
		
		synchronized (_threadInfo)
		{
			_threadInfo.forEach((t, v) -> {

				if (t == null)
					return;

				if (t instanceof Daemon)
				{
					((Daemon) t).shutdown();
				}
			});

		}

		shutdownThreads();
	}

	public static void shutdownThreads()
	{
		_longRunningCallToThread.shutdown();
		clearDeadThreads();
	}

	public static void callToThread(ThreadCallRunnable r)
	{
		if(!_threadsInit)
		{
			Logger.info("Cannot callToThread because threads have not be initialized yet!");
			return;
		}
		
		if (_longRunningCallToThread.isAlive())
		{
			_longRunningCallToThread.put(r);
		}
	}

	public static void dieIfShuttingDown() throws ThreadShuttingDownException
	{
		if (isThreadShuttingDown())
			throw new ThreadShuttingDownException();
	}

	public static void clearDeadThreads()
	{
		synchronized (_threadInfo)
		{
			ArrayList<Thread> toRemove = new ArrayList<>();

			_threadInfo.forEach((t, v) -> {
				if (!t.isAlive())
					toRemove.add(t);
			});

			toRemove.forEach(t -> _threadInfo.remove(t));
		}
	}

	public static boolean isThreadShuttingDown()
	{
		if (TimeHelper.timeHasPassed(_lastThreadClearour + CLEAROUT_INTERVAL))
		{
			Logger.info("Auto clearing dead threads");
			clearDeadThreads();
			_lastThreadClearour = System.currentTimeMillis();
		}

		Thread t = Thread.currentThread();

		if (_threadInfo.containsKey(t))
		{
			return _threadInfo.get(t);
		}

		synchronized (_threadInfo)
		{
			_threadInfo.put(t, false);
		}

		return false;
	}

	public static void shutdownThread(Thread t)
	{
		if (t == null)
		{
			Logger.warn("Cannot shutdown null thread!");
			return;
		}

		synchronized (_threadInfo)
		{
			_threadInfo.put(t, true);
		}
	}

}
