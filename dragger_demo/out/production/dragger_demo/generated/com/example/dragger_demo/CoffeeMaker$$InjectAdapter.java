// Code generated by dagger-compiler.  Do not edit.
package com.example.dragger_demo;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binding<CoffeeMaker>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code CoffeeMaker} and its
 * dependencies.
 *
 * Being a {@code Provider<CoffeeMaker>} and handling creation and
 * preparation of object instances.
 *
 * Being a {@code MembersInjector<CoffeeMaker>} and handling injection
 * of annotated fields.
 */
public final class CoffeeMaker$$InjectAdapter extends Binding<CoffeeMaker>
    implements Provider<CoffeeMaker>, MembersInjector<CoffeeMaker> {
  private Binding<dagger.Lazy<Heater>> heaterLazy;
  private Binding<Pump> pump;
  private Binding<Drink> drink;

  public CoffeeMaker$$InjectAdapter() {
    super("com.example.dragger_demo.CoffeeMaker", "members/com.example.dragger_demo.CoffeeMaker", NOT_SINGLETON, CoffeeMaker.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    heaterLazy = (Binding<dagger.Lazy<Heater>>) linker.requestBinding("dagger.Lazy<com.example.dragger_demo.Heater>", CoffeeMaker.class, getClass().getClassLoader());
    pump = (Binding<Pump>) linker.requestBinding("com.example.dragger_demo.Pump", CoffeeMaker.class, getClass().getClassLoader());
    drink = (Binding<Drink>) linker.requestBinding("com.example.dragger_demo.Drink", CoffeeMaker.class, getClass().getClassLoader());
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(heaterLazy);
    injectMembersBindings.add(pump);
    injectMembersBindings.add(drink);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<CoffeeMaker>}.
   */
  @Override
  public CoffeeMaker get() {
    CoffeeMaker result = new CoffeeMaker();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<CoffeeMaker>}.
   */
  @Override
  public void injectMembers(CoffeeMaker object) {
    object.heaterLazy = heaterLazy.get();
    object.pump = pump.get();
    object.drink = drink.get();
  }

}
