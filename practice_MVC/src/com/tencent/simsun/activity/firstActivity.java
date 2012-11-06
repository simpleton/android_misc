package com.tencent.simsun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.simsun.R;
import com.tencent.simsun.activity.delegate.firstActivityDelegate;
import com.tencent.simsun.app.mvcApp;
import com.tencent.simsun.controllers.firstController;

public class firstActivity extends Activity implements firstActivityDelegate{
    private static final String TAG = mvcApp.class.getSimpleName();
    
    firstController controller;
   
    private Button minusBtn;
    private Button plusBtn;
    private Button saveBtn;
    
    //FIXME: could use getter and setter, just use public for being lazy...
    public TextView count;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstlayout);
        
        //FIXME:may be strange, by passing two this
        controller = new firstController(this, this);
        
        minusBtn = (Button)findViewById(R.id.btn_dec);
        plusBtn = (Button) findViewById(R.id.btn_inc);
        saveBtn = (Button) findViewById(R.id.btn_save);
        
        count = (TextView) findViewById(R.id.textView1);
        
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "plusBtn click");
                controller.plusCount();
            }
        });
        
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "minusBtn click");
                controller.minusCount();
            }
        });
        
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "saveBtn click");
                controller.saveCount();
            }
        });
        
    }
    
    @Override
    protected void onDestroy() {
        controller.dispose();
        super.onDestroy();
    }

    @Override
    public void onSaveComplete() {
        Log.d(TAG, "onSaveComplete");
    }


}
