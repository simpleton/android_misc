package com.tencent.simsun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.tencent.simsun.R;
import com.tencent.simsun.activity.adapter.listAdapter;
import com.tencent.simsun.activity.delegate.listActivityDelegate;
import com.tencent.simsun.controllers.listController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class listActivity extends Activity implements listActivityDelegate{

    private ListView listView;
    private listAdapter adapter;
   
    private listController controller;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
       
        listView = (ListView)findViewById(R.id.list);
       
        adapter = new listAdapter(this);
        listView.setAdapter(adapter);
       
        List<Map<String, String>> stubItems = new ArrayList<Map<String,String>>();
        Map<String, String> stubItem = new HashMap<String, String>();
        stubItem.put("text1", "Loading...");
        stubItem.put("text2", "Loading...");
        stubItems.add(stubItem);
       
        adapter.setItems(stubItems);
        
        controller = new listController(this, this);
        controller.queryData();
       
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDateReceived(List<Map<String, String>> items) {
        adapter.setItems(items);        
    }

   

}
