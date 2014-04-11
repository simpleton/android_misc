// Code generated by dagger-compiler.  Do not edit.
package com.example.dragger_demo;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binding<PeopleDrink>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code PeopleDrink} and its
 * dependencies.
 *
 * Being a {@code Provider<PeopleDrink>} and handling creation and
 * preparation of object instances.
 */
public final class PeopleDrink$$InjectAdapter extends Binding<PeopleDrink>
    implements Provider<PeopleDrink> {
  private Binding<Pump> pump;

  public PeopleDrink$$InjectAdapter() {
    super("com.example.dragger_demo.PeopleDrink", "members/com.example.dragger_demo.PeopleDrink", NOT_SINGLETON, PeopleDrink.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    pump = (Binding<Pump>) linker.requestBinding("com.example.dragger_demo.Pump", PeopleDrink.class, getClass().getClassLoader());
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    getBindings.add(pump);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<PeopleDrink>}.
   */
  @Override
  public PeopleDrink get() {
    PeopleDrink result = new PeopleDrink(pump.get());
    return result;
  }

}