package com.tencent.simsun.controllers;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.tencent.simsun.activity.firstActivity;
import com.tencent.simsun.db.CounterDao;
import com.tencent.simsun.model.CounterVo;
import com.tencent.simsun.model.OnChangeListener;


public class firstController extends Controller implements OnChangeListener<CounterVo> {

    private static final String TAG = firstController.class.getSimpleName();
    //FIXME:enum
    public static final int MESSAGE_SAVE_MODEL = 1;
    public static final int MESSAGE_INCREMENT_COUNT = 2;
    public static final int MESSAGE_DECREMENT_COUNT = 3;
    public static final int MESSAGE_RESET_COUNT = 4;
    public static final int MESSAGE_SAVE_COMPLETE = 5;
    
    protected CounterVo model;
    
    //FIXME: runtime bind, could use reflection
    protected firstActivity view;
    
    //TODO: discuss use UI thread or not
    private Handler workerHandler;
    private HandlerThread workerThread;
    //private Handler workerHandler;
    
    public firstController(Activity view) {
        workerThread = new HandlerThread("first controller work thread");
        workerThread.start();
        workerHandler = new Handler(workerThread.getLooper());
        
        //workerHandler = new Handler();
        model = new CounterVo();
        model.addListener(this);
        this.view = (firstActivity)view;
    }
    
    @Override
    public void dispose() {
//        workerThread.getLooper().quit();
        super.dispose();
    }
    @Override
    public boolean handleMessage(int what, Object data) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean handleMessage(int what) {
        Log.d(TAG, "handleMessage" + what);
        switch(what) {
            case MESSAGE_INCREMENT_COUNT:
                moveCount(1);
                return true;
            case MESSAGE_DECREMENT_COUNT:
                moveCount(-1);
                return true;
            case MESSAGE_RESET_COUNT:
                model.setCount(0);
                return true;
            case MESSAGE_SAVE_MODEL:
                saveModel();
                return true;
            default:
                return super.handleMessage(what);
        }
    }
    
    private void moveCount(int amount) {
        model.setCount(model.getCount()+amount);
    }
    private void saveModel() {
        Log.d(TAG, "saveModel");
        workerHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (model) {
                    CounterDao dao = new CounterDao();

                    long id = dao.insert(model);
                    model.setId((int) id);

                    // notify every register handler
                    notifyOutboxHandlers(MESSAGE_SAVE_COMPLETE, 0, 0, null);
                }
            }
        });
    }

    /**
     * model observer
     */
    @Override
    public void onChange(CounterVo model) {
        Log.d(TAG, "onChange");
        updateView();
    }
    
    private void updateView() {        
        //view.count.setText(Integer.toString(model.getCount()));
    }
}
