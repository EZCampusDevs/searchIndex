package org.ezcampus.search.data.threading;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread event similar to python's threading.event
 * 
 * @author https://stackoverflow.com/a/1040821
 *
 */
class ThreadEvent
{
	private Lock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();
	private boolean flag;


	public void doWait() throws InterruptedException
	{
		lock.lock();
		try
		{
			while (!flag)
			{
				cond.await();
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public boolean doWait(float seconds) throws InterruptedException
	{
		lock.lock();
		try
		{
			cond.await((int) (seconds * 1000), TimeUnit.MILLISECONDS);

			return this.flag;
		}
		finally
		{
			lock.unlock();
		}
	}

	public boolean isSet()
	{
		lock.lock();
		try
		{
			return flag;
		}
		finally
		{
			lock.unlock();
		}
	}

	public void set()
	{
		lock.lock();
		try
		{
			flag = true;
			cond.signalAll();
		}
		finally
		{
			lock.unlock();
		}
	}

	public void clear()
	{
		lock.lock();
		try
		{
			flag = false;
			cond.signalAll();
		}
		finally
		{
			lock.unlock();
		}
	}
}
