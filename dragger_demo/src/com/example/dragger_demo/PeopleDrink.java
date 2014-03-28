package com.example.dragger_demo;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by simsun on 3/28/14.
 */
public class PeopleDrink implements Drink{
  private static final String TAG = "PeopleDrink";
  private Pump pump;
  @Inject
  PeopleDrink(Pump pump) {
    this.pump = pump;
  }

  @Override
  public void drink() {
    if (pump.isPumped()) {
      Log.e(TAG, "Drink");
    }
  }
}
