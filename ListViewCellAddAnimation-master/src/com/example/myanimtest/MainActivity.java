package com.example.myanimtest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	static final int ANIMATION_DURATION = 200;
	private static List<MyCell> mAnimList = new ArrayList<MyCell>();
	private MyAnimListAdapter mMyAnimListAdapter;
	private ListView mListView;

	private static int index = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		for (int i=0;i<50;i++) {
			MyCell cell = new MyCell();
			cell.name = "Cell No."+Integer.toString(i);
			mAnimList.add(cell);
		}

		mMyAnimListAdapter = new MyAnimListAdapter(this, R.layout.chain_cell, mAnimList);
		mListView = (ListView) findViewById(R.id.chainListView);
		mListView.setAdapter(mMyAnimListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = false;

		switch (item.getItemId()) {
		case R.id.menu_add:
			MyCell myCell = new MyCell();
			myCell.name = "New Cell!!!";
			addNewCell(myCell);
			result = true;
			break;
		}

		return result;
	}

	private void addNewCell(MyCell cell) {
		Bitmap bitmap = getListViewBitmap();
		final ImageView imageView = (ImageView) findViewById(R.id.bitmapImageView);
		imageView.setImageBitmap(bitmap);
//		imageView.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
		imageView.setVisibility(View.VISIBLE);

		View rowView = mListView.getChildAt(0);
		TranslateAnimation transanim = new TranslateAnimation(0, 0, 0, rowView.getHeight());
		transanim.setDuration(ANIMATION_DURATION);
		transanim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				imageView.setVisibility(View.GONE);
			}
			@Override public void onAnimationRepeat(Animation animation) {}
			@Override public void onAnimationStart(Animation animation) {}
		});

		mMyAnimListAdapter.insert(cell, 0);
		imageView.startAnimation(transanim);
	}

	private Bitmap getListViewBitmap() {
		mListView.setDrawingCacheEnabled(true);
		Bitmap bitmap = mListView.getDrawingCache();
		Bitmap bitmap1 = Bitmap.createBitmap(bitmap);
		mListView.setDrawingCacheEnabled(false);
		return bitmap1;
	}

	private void deleteCell(final View v, final int index) {
		AnimationListener al = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				mAnimList.remove(index);

				ViewHolder vh = (ViewHolder)v.getTag();
				vh.needInflate = true;

				mMyAnimListAdapter.notifyDataSetChanged();
			}
			@Override public void onAnimationRepeat(Animation animation) {}
			@Override public void onAnimationStart(Animation animation) {}
		};

		collapse(v, al);
	}

	private void collapse(final View v, AnimationListener al) {
		final int initialHeight = v.getMeasuredHeight();

		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				}
				else {
					v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		if (al!=null) {
			anim.setAnimationListener(al);
		}
		anim.setDuration(ANIMATION_DURATION);
		v.startAnimation(anim);
	}


	private class MyCell {
		public String name;
	}

	private class ViewHolder {
		public boolean needInflate;
		public TextView text;
		public ViewGroup vg;
		ImageButton imageButton;
	}

	public class MyAnimListAdapter extends ArrayAdapter<MyCell> {
		private LayoutInflater mInflater;
		private int resId;

		public MyAnimListAdapter(Context context, int textViewResourceId, List<MyCell> objects) {
			super(context, textViewResourceId, objects);
			this.resId = textViewResourceId;
			this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			final View view;
			ViewHolder vh;
			MyCell cell = (MyCell)getItem(position);

			if (convertView==null) {
				view = mInflater.inflate(R.layout.chain_cell, parent, false);
				setViewHolder(view);
			}
			else if (((ViewHolder)convertView.getTag()).needInflate) {
				view = mInflater.inflate(R.layout.chain_cell, parent, false);
				setViewHolder(view);
			}
			else {
				view = convertView;
			}

			vh = (ViewHolder)view.getTag();
			
			vh.text.setText(cell.name);
			
			//vh.text.addView(new TextView(getContext()).setText(cell.name));
			vh.imageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//deleteCell(view, position);
					index = position;
					mMyAnimListAdapter.notifyDataSetChanged();
				}
			});
			final FlipView fv;
			//View testview = mInflater.inflate(R.layout.chain_cell, null);
			View testview = View.inflate(getContext(), R.layout.chain_cell, null);
			View testview1 = View.inflate(getContext(), R.layout.chain_cell, null);
			if (position == index) {
			    fv = new FlipView(getContext(), testview, testview1);
			    fv.setAnimationDuration(800);
			    fv.setDirection(FlipView.DIRECTION_VERTICAL);
			    fv.setPivot(FlipView.PIVOT_BOTTOM);
			    testview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						fv.flip();
					}
				});
			    vh.needInflate = true;
			    fv.setTag(vh);
			    vh.vg.removeAllViews();
			    vh.vg.addView(fv);
			}
			return view;
		}

		private void setViewHolder(View view) {
			ViewHolder vh = new ViewHolder();
			vh.vg = (ViewGroup) view.findViewById(R.id.container);
			vh.text = (TextView)view.findViewById(R.id.cell_name_textview);
			vh.imageButton = (ImageButton) view.findViewById(R.id.cell_trash_button);
			vh.needInflate = false;
			view.setTag(vh);
		}
	}
}
