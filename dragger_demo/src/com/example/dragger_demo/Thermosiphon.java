package com.example.dragger_demo;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by simsun on 3/28/14.
 */
public class Thermosiphon implements Pump {
  private static final String TAG = "Thermosiphon";
  private final Heater heater;
  boolean pumped = false;

  @Inject
  Thermosiphon(Heater heater) {
    this.heater = heater;
  }

  @Override
  public void pump() {
    if (heater.isHot()) {
      Log.e(TAG, "Pumping");
      pumped = true;
    }
  }

  @Override
  public boolean isPumped() {
    return pumped;
  }
}
