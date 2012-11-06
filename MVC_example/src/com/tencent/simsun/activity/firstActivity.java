package com.tencent.simsun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.simsun.R;
import com.tencent.simsun.app.mvcApp;
import com.tencent.simsun.controllers.Controller;
import com.tencent.simsun.controllers.firstController;

public class firstActivity extends Activity implements Callback {
    private static final String TAG = mvcApp.class.getSimpleName();
    
    Controller controller;
   
    private Button minusBtn;
    private Button plusBtn;
    private Button saveBtn;
    
    //FIXME: could use getter and setter, just use public for being lazy...
    public TextView count;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstlayout);
        controller = new firstController(this);
        
        minusBtn = (Button)findViewById(R.id.btn_dec);
        plusBtn = (Button) findViewById(R.id.btn_inc);
        saveBtn = (Button) findViewById(R.id.btn_save);
        
        count = (TextView) findViewById(R.id.textView1);
        
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "plusBtn click");
                controller.handleMessage(firstController.MESSAGE_INCREMENT_COUNT);
            }
        });
        
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "minusBtn");
                controller.handleMessage(firstController.MESSAGE_DECREMENT_COUNT);
            }
        });
        
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "saveBtn");
                controller.handleMessage(firstController.MESSAGE_SAVE_MODEL);                
            }
        });
        
        controller.addOutboxHandler(new Handler(this));
    }
    
    @Override
    protected void onDestroy() {
        controller.dispose();
        super.onDestroy();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case firstController.MESSAGE_SAVE_COMPLETE:
                //do asynchronously
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "work work");
                    }
                });
                return true;
        }
        return false;
    }
}
