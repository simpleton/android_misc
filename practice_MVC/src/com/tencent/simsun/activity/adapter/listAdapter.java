/**
 * 
 */
package com.tencent.simsun.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author simsun
 *
 */
public class listAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> items = Collections.EMPTY_LIST;  
   
    public listAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int index) {     
        return items.get(index);
    }
    @Override
    public long getItemId(int index) {     
        return items.get(index).hashCode();
    }
   
    public void setItems(List<Map<String, String>> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int index, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        }
       
        TextView text1 = (TextView)view.findViewById(android.R.id.text1);
        TextView text2 = (TextView)view.findViewById(android.R.id.text2);
       
        text1.setText(items.get(index).get("text1"));
        text2.setText(items.get(index).get("text2"));
       
        return view;
    }
}
