package com.example.mergecursordemo;

import android.app.Activity;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {
	private static final String[] MemberCols = {
		"_id",
		"number"
	};
	private static final String[] items=
	      { "aaa", "aa", "a", "bbb", "bb", "bbbb", "ccc", "cc" };
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView) findViewById(R.id.listView1);
		ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		//mListView.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Cursor[] cursors = new Cursor[2];
		MatrixCursor cursor1 = new MatrixCursor(MemberCols);
		cursor1.addRow(new Object[] {0,"1"});
		cursor1.addRow(new Object[] {1, "3"});
		cursor1.addRow(new Object[] {2, "5"});
		cursor1.addRow(new Object[] {3, "7"});
		
		MatrixCursor cursor2 = new MatrixCursor(MemberCols);
		cursor2.addRow(new Object[] {0,"2"});
		cursor2.addRow(new Object[] {1, "4"});
		cursor2.addRow(new Object[] {2, "6"});
		cursor2.addRow(new Object[] {3, "8"});
		cursors[0] = cursor1;
		cursors[1] = cursor2;
		Cursor mergeCursor = new MergeCursor(cursors);
		SortCursor sortCursor = new SortCursor(cursors, "number");
		ListAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, sortCursor, 
				new String[] {"number"}, new int[]{android.R.id.text1});
		mListView.setAdapter(adapter);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
