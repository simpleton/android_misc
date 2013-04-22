/**
 * 
 */
package com.example.dblocktest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author simsun
 *
 */
public class DBServiceThree extends IntentService {

	private static final String TAG = "DBServiceThree";
	private AtomicInteger allCount = new AtomicInteger();
	
	public DBServiceThree() {
		super("DBServiceThree");
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(DBServiceThree.class.getName(), "onHandlerIntent");
		allCount.set(0);
		final List<DbInsertThread> allThreads = new ArrayList<DbInsertThread>();

        DatabaseHelper helper = new DatabaseHelper(DBServiceThree.this);

        for(int i=0; i<config.THREAD_THRESHOLD; i++)
        {
            allThreads.add(new DbInsertThread(helper, 100, allCount));
        }

        runAllThreads(allThreads);
	}

	private void runAllThreads(final List<DbInsertThread> allThreads)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                for (DbInsertThread allThread : allThreads)
                {
                    allThread.start();
                }
                for (DbInsertThread thread : allThreads)
                {
                    try
                    {
                        thread.join();
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
                Log.e(TAG , "Inserted" + allCount.get());
            }
        }).start();
    }
}
