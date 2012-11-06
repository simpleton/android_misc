
package com.tencent.simsun;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tencent.simsun.activity.firstActivity;
import com.tencent.simsun.activity.listActivity;

public class MainActivity extends ListActivity {

    public static final String[] options = {
            "firstView", "ListView"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent;

        switch (position) {
            default:
            case 0:
                intent = new Intent(this, firstActivity.class);
                break;

            case 1:
                intent = new Intent(this, listActivity.class);
                break;

        }

        startActivity(intent);
    }
}
