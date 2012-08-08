package com.silde.demo;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.silde.demo.view.MainSildeView;
import com.silde.demo.view.MainSildeView.OnScreenChangeListener;

/**
 * 主框架界面
 * @author darrenchen
 *
 */
public class MainActivity extends ActivityGroup implements OnScreenChangeListener {
	
	/**
	 * 传递目标intent
	 */
	public static final String TARGET_INTENT = "target-intent";
	
	/**
	 * 传递目标intent的标识
	 */
	public static final String TARGET_ID = "target-key";
	
	private static final String ID_CENTER = "center";
	
	private static final String ID_LEFT = "left";
	
	private static final String ID_RIGHT = "right";
	
	private LocalActivityManager actvityManager;
	
	private Intent centerIntent, leftIntent, rightIntent;
	
	private MainSildeView mainSildeView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actvityManager = getLocalActivityManager();
        
        //初始化intent
        centerIntent = new Intent(this, CenterActivity.class);
        leftIntent = new Intent(this, LeftActivity.class);
        rightIntent = new Intent(this, RightActivity.class);
        
        //创建主框架View
        mainSildeView = new MainSildeView(this, null);
        mainSildeView.setOnScreenChangeListener(this);
        
        //生成各自Activity的View
        Window leftWindow = actvityManager.startActivity(ID_LEFT, leftIntent);
        leftWindow.setBackgroundDrawable(null);
        mainSildeView.setLeftView(leftWindow.getDecorView());
        
        Window rightWindow = actvityManager.startActivity(ID_RIGHT, rightIntent);
        rightWindow.setBackgroundDrawable(null);
        mainSildeView.setRightView(rightWindow.getDecorView());
        
        Window centerWindow = actvityManager.startActivity(ID_CENTER, centerIntent);
        centerWindow.setBackgroundDrawable(null);
        mainSildeView.setCenterView(centerWindow.getDecorView());
        
        //设置到ContentView
        setContentView(mainSildeView);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle == null) {
        	return ;
        }
        Intent target = (Intent) bundle.get(TARGET_INTENT);
        String id = bundle.getString(TARGET_ID);
        if(target != null) {
        	Window window = actvityManager.startActivity(id, target);
        	if(window != null) {
            	mainSildeView.setCenterView(window.getDecorView());
            } 
        }
    }

	@Override
	public void onScreenChangeStart(int whichScreen) {
		System.out.println("---------onScreenChangeStart");
	}

	@Override
	public void onScreenChangeEnd(boolean change, int whichScreen) {
		System.out.println("---------onScreenChangeEnd");
	}
    
    
}