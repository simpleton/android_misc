package com.silde.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CenterActivity extends Activity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center);
        
        findViewById(R.id.btn1).setOnClickListener(clickListener);
        findViewById(R.id.btn2).setOnClickListener(clickListener);
    }
    
    private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent target = new Intent(CenterActivity.this, ExampleActivity.class);
			switch(v.getId()) {
			case R.id.btn1:
				Intent intent = new Intent(CenterActivity.this, MainActivity.class);
	            intent.putExtra(MainActivity.TARGET_INTENT, target);
	            intent.putExtra(MainActivity.TARGET_ID, "example");
	            startActivity(intent);
				break;
			case R.id.btn2:
				startActivity(target);
				break;
			}
		}
	};
}