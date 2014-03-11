package com.example.ThreadBenchmark;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;


public class ThreadTestActivity extends Activity {
    private static final String TAG = "TreadTestActivity";
    private GraphViewSeries graphViewSeries;

    private static final int UPDATE = 1;
    private static final int PROCESS_MAX_PRIORITY = -20;
    private static final int PROCESS_MIN_PRIORITY = Process.THREAD_PRIORITY_LOWEST;
    private int []runningProcess = new int[PROCESS_MIN_PRIORITY - PROCESS_MAX_PRIORITY + 1];
    private GraphView.GraphViewData []viewData = new GraphView.GraphViewData[PROCESS_MIN_PRIORITY - PROCESS_MAX_PRIORITY + 1];
    private static final int PRIORITY_TO_INDEX = 20;
    private static final int valueMax = 100;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button startBtn = (Button) findViewById(R.id.button);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startThread();
            }
        });

        // init example series data
        updateData();

        graphViewSeries = new GraphViewSeries(viewData);
        GraphView graphView = new BarGraphView(this, "Thread Profile");
        graphView.getGraphViewStyle().setNumHorizontalLabels(10);
        graphView.setManualYAxisBounds(valueMax, 0);
        graphView.addSeries(graphViewSeries);
        LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
        layout.addView(graphView);
    }

    private void updateData() {
        for (int i = 0; i < viewData.length; ++i) {
            viewData[i] = new GraphView.GraphViewData(i+1, runningProcess[i]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(UPDATE);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE) {
                updateData();
                graphViewSeries.resetData(viewData);
            }
            super.handleMessage(msg);
        }
    };

    private int wasteCpuTime() {
        int value = 0;
        for (int i = 0; i < (1<<20); ++i) {
            for (int j = 0; j < 10; ++j) {
                value = i*j;
            }
        }
        return value;
    }

    private Runnable buildRunable(final String name, final int priority, final int max) {
        return new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(priority);
                final int index = priority + PRIORITY_TO_INDEX;
                for (int i = 0; i < max; ++i) {
                    wasteCpuTime();
                    runningProcess[index]++;
                    if (!handler.hasMessages(UPDATE)) {
                        handler.sendEmptyMessageDelayed(UPDATE, 20L);
                    }
                }
            }
        };
    }
    /**
     */
    private void startThread() {
        for (int i = 0; i < runningProcess.length; ++i) {
            runningProcess[i] = 0;
        }
        for (int i = PROCESS_MIN_PRIORITY; i > PROCESS_MAX_PRIORITY; --i) {
            String threadName = "Thread_" + i;
            Thread t = new Thread(buildRunable(threadName, i, valueMax));
            t.setName(threadName);
            t.start();
        }
    }
}
