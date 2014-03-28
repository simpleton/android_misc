package com.example.dragger_demo;

import android.app.Activity;
import android.os.Bundle;
import dagger.ObjectGraph;

import javax.inject.Inject;

public class MyActivity extends Activity {
  /**
   * Called when the activity is first created.
   */
  @Inject
  CoffeeMaker coffeeMaker;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    //ObjectGraph objectGraph = ((TestApplication) getApplication()).getObjectGraph();
    //objectGraph.inject(this);

    coffeeMaker.brew();
  }
}
