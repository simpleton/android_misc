package com.tencent.simsun.controllers;

import android.content.Context;
import android.os.AsyncTask;

import com.tencent.simsun.activity.delegate.listActivityAction;
import com.tencent.simsun.activity.delegate.listActivityDelegate;
import com.tencent.simsun.model.SlowDataSource;

import java.util.List;
import java.util.Map;

public class listController implements listActivityAction{
    private LoadItemsTask mTaks = new LoadItemsTask();
    
    private Context ct;
    private listActivityDelegate listener;
    
    public listController(Context ct, listActivityDelegate listener) {
        this.ct = ct;
        this.listener = listener;        
    }
    
    
    private class LoadItemsTask extends AsyncTask<Void, Void, Void> {
        private List<Map<String, String>> items;
       
        @Override
        protected Void doInBackground(Void... params) {
            items = SlowDataSource.getData();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            //adapter.setItems(items);
            listener.onDateReceived(items);
        }
       
    }


    public void queryData() {
        mTaks.execute();
    }
}
