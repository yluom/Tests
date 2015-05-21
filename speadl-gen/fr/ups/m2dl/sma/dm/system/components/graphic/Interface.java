package fr.ups.m2dl.sma.dm.system.components.graphic;

import fr.ups.m2dl.sma.dm.system.configuration.IConfig;
import fr.ups.m2dl.sma.dm.system.environment.IGlobalEnvironmentGet;
import fr.ups.m2dl.sma.dm.system.process.IAction;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

@SuppressWarnings("all")
public abstract class Interface {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public IAction actions();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public IGlobalEnvironmentGet envGet();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public IConfig config();
  }
  
  public interface Component extends Interface.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public ICycle cycle();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements Interface.Component, Interface.Parts {
    private final Interface.Requires bridge;
    
    private final Interface implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_cycle() {
      assert this.cycle == null: "This is a bug.";
      this.cycle = this.implementation.make_cycle();
      if (this.cycle == null) {
      	throw new RuntimeException("make_cycle() in fr.ups.m2dl.sma.dm.system.components.graphic.Interface should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_cycle();
    }
    
    public ComponentImpl(final Interface implem, final Interface.Requires b, final boolean doInits) {
      this.bridge = b;
      this.implementation = implem;
      
      assert implem.selfComponent == null: "This is a bug.";
      implem.selfComponent = this;
      
      // prevent them to be called twice if we are in
      // a specialized component: only the last of the
      // hierarchy will call them after everything is initialised
      if (doInits) {
      	initParts();
      	initProvidedPorts();
      }
    }
    
    private ICycle cycle;
    
    public ICycle cycle() {
      return this.cycle;
    }
  }
  
  /**
   * Used to check that two components are not created from the same implementation,
   * that the component has been started to call requires(), provides() and parts()
   * and that the component is not started by hand.
   * 
   */
  private boolean init = false;;
  
  /**
   * Used to check that the component is not started by hand.
   * 
   */
  private boolean started = false;;
  
  private Interface.ComponentImpl selfComponent;
  
  /**
   * Can be overridden by the implementation.
   * It will be called automatically after the component has been instantiated.
   * 
   */
  protected void start() {
    if (!this.init || this.started) {
    	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
    }
  }
  
  /**
   * This can be called by the implementation to access the provided ports.
   * 
   */
  protected Interface.Provides provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract ICycle make_cycle();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Interface.Requires requires() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
    }
    return this.selfComponent.bridge;
  }
  
  /**
   * This can be called by the implementation to access the parts and their provided ports.
   * 
   */
  protected Interface.Parts parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized Interface.Component _newComponent(final Interface.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Interface has already been used to create a component, use another one.");
    }
    this.init = true;
    Interface.ComponentImpl  _comp = new Interface.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
