package com.example.dragger_demo;

import android.util.Log;
import dagger.Lazy;

import javax.inject.Inject;

/**
 * Created by simsun on 3/28/14.
 */
public class CoffeeMaker {
  private static final String TAG = "Coffeemaker";
  @Inject Lazy<Heater> heaterLazy;
  @Inject Pump pump;
  @Inject Drink drink;

  public void brew() {
    heaterLazy.get().on();
    pump.pump();
    Log.d(TAG, "Pumped");
    //heaterLazy.get().off();
    drink.drink();
  }
}
