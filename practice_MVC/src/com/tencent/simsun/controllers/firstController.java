package com.tencent.simsun.controllers;

import android.content.Context;
import android.util.Log;

import com.tencent.simsun.activity.delegate.firstActivityAction;
import com.tencent.simsun.activity.delegate.firstActivityDelegate;
import com.tencent.simsun.db.CounterDao;
import com.tencent.simsun.model.CounterVo;
import com.tencent.simsun.model.OnChangeListener;


public class firstController implements OnChangeListener<CounterVo> ,firstActivityAction{

    private static final String TAG = firstController.class.getSimpleName();

    protected CounterVo model;
    private Context ct;
    private firstActivityDelegate listener;
    
    public firstController(Context ct, firstActivityDelegate listener) {
        
        //workerHandler = new Handler();
        model = new CounterVo();
        model.addListener(this);
        
        this.ct = ct;
        this.listener = listener;
    }
    

    /**
     * should invoke in onDestory
     */
    public void dispose() {
                
    }

    

    private void moveCount(int amount) {
        model.setCount(model.getCount()+amount);
    }

    
    /**
     * model observer
     */
    @Override
    public void onChange(CounterVo model) {
        Log.d(TAG, "onChange");
        listener.onSaveComplete();        
    }


    @Override
    public void plusCount() {
        moveCount(1);
    }


    @Override
    public void minusCount() {
        moveCount(-1);
    }


    @Override
    public void saveCount() {        
        CounterDao dao = new CounterDao();
        long id = dao.insert(model);
        model.setId((int) id);
    }

}
