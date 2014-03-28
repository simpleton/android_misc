package com.example.dragger_demo;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by simsun on 3/28/14.
 */
public class ElectricHeater implements Heater {
  private static final String TAG = "ElectricHeater";
  private boolean heating = false;

  @Override
  public void on() {
    heating = true;
    Log.e(TAG, "Heating");
  }

  @Override
  public void off() {
    heating = false;
    Log.e(TAG, "Heating Off");
  }

  @Override
  public boolean isHot() {
    return heating;
  }
}
