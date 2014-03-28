package com.example.dragger_demo;

import android.content.Context;
import android.util.Log;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by simsun on 3/28/14.
 */

@Module(
    injects = {
        MyActivity.class
    },
    complete = false,
    library = true
)
public class TestModule {
  private static final String TAG = "TestModule";
  private final Context context;
  public TestModule(Context context) {
    this.context = context;
  }

  @Provides @Singleton
  Context applicatoinContext() {
    Log.e(TAG, "get Applicaton");
    return context;
  }

  @Provides @Singleton Drink provideDrink(PeopleDrink drink) {
    return drink;
  }

  @Provides @Singleton Pump providePump(Thermosiphon pump) {
    return pump;
  }

  @Provides @Singleton Heater provideHeater() {
    return new ElectricHeater();
  }
}
