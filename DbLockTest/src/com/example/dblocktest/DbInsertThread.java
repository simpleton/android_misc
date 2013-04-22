/**
 * 
 */
package com.example.dblocktest;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import android.util.Log;

/**
 * @author simsun
 *
 */
public class DbInsertThread extends Thread {
	private DatabaseHelper helper;
    private int runCount;
    private AtomicInteger allCount;
    private static final String TAG = "DbInsertThread";
	DbInsertThread(DatabaseHelper helper, int runCount, AtomicInteger count) {
		this.helper = helper;
		this.runCount = runCount;
		allCount = count;
	}

	@Override
	public void run() {
		Random random = new Random();
		for (int i = 0; i < runCount; i++) {
			if (i % 10 == 0)
				Log.i(TAG, "Writing" + i);

			try {
				helper.createSession("heyo - " + random.nextInt(12345678));
				allCount.incrementAndGet();
			} catch (Exception e) {
				// Insert failed!!!
				Log.e(TAG, "Insert failed!!!");
			}
		}
	}

}
