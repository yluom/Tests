package fr.ups.m2dl.sma.dm.system.components.network;

import fr.ups.m2dl.sma.dm.system.network.ICom;
import fr.ups.m2dl.sma.dm.system.network.IConfigNetwork;
import fr.ups.m2dl.sma.dm.system.network.IConflict;

@SuppressWarnings("all")
public abstract class Com {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public IConflict conflictManager();
  }
  
  public interface Component extends Com.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public ICom com();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IConfigNetwork config();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements Com.Component, Com.Parts {
    private final Com.Requires bridge;
    
    private final Com implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_com() {
      assert this.com == null: "This is a bug.";
      this.com = this.implementation.make_com();
      if (this.com == null) {
      	throw new RuntimeException("make_com() in fr.ups.m2dl.sma.dm.system.components.network.Com should not return null.");
      }
    }
    
    private void init_config() {
      assert this.config == null: "This is a bug.";
      this.config = this.implementation.make_config();
      if (this.config == null) {
      	throw new RuntimeException("make_config() in fr.ups.m2dl.sma.dm.system.components.network.Com should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_com();
      init_config();
    }
    
    public ComponentImpl(final Com implem, final Com.Requires b, final boolean doInits) {
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
    
    private ICom com;
    
    public ICom com() {
      return this.com;
    }
    
    private IConfigNetwork config;
    
    public IConfigNetwork config() {
      return this.config;
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
  
  private Com.ComponentImpl selfComponent;
  
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
  protected Com.Provides provides() {
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
  protected abstract ICom make_com();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract IConfigNetwork make_config();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Com.Requires requires() {
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
  protected Com.Parts parts() {
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
  public synchronized Com.Component _newComponent(final Com.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Com has already been used to create a component, use another one.");
    }
    this.init = true;
    Com.ComponentImpl  _comp = new Com.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
