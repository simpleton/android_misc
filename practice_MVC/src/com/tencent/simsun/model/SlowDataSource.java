package com.tencent.simsun.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlowDataSource {
    public static List<Map<String, String>> getData() {
        
        List<Map<String, String>> items = new ArrayList<Map<String,String>>();
       
        for (int i = 0; i<10; i++) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("text1", "Item "+i);
            item.put("text2", "Details "+i);
            items.add(item);
        }
               
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Log.e("SlowDataSource", "interrupted", e);
        }
       
        return items;
    }   
}
