package com.example.dblocktest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	public static AtomicInteger allCount = new AtomicInteger();
	private Handler mUiHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.btn_MultiThread).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 allCount.set(0);
                 final List<DbInsertThread> allThreads = new ArrayList<DbInsertThread>();

                 DatabaseHelper helper = new DatabaseHelper(MainActivity.this);

                 for(int i=0; i<4; i++)
                 {
                     allThreads.add(new DbInsertThread(helper, 100, allCount));
                 }

                 runAllThreads(allThreads);
			}
		});
		
		
		findViewById(R.id.btn_MultiProcess).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				allCount.set(0);
				
				Intent service = new Intent(MainActivity.this, DBServiceOne.class);
				startService(service);
				
				Intent service1 = new Intent(MainActivity.this, DBServiceTwo.class);
				startService(service1);
				
				Intent service2 = new Intent(MainActivity.this, DBServiceThree.class);
				startService(service2);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

                mUiHandler.post(new Runnable()
                {
                    public void run()
                    {
                        ((TextView)findViewById(R.id.results)).setText("Inserted "+ allCount.get());
                    }
                });
            }
        }).start();
    }
}
