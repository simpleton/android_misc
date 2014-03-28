package com.example.dragger_demo;

import android.app.Application;
import dagger.ObjectGraph;

/**
 * Created by simsun on 3/28s/14.
 */
public class TestApplication extends Application{
  private ObjectGraph objectGraph;

  @Override
  public void onCreate() {
    super.onCreate();
    this.objectGraph = ObjectGraph.create(new TestModule(this));
  }

  public ObjectGraph getObjectGraph() {
    return objectGraph;
  }
}
