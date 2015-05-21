package fr.ups.m2dl.sma.dm.system.components.network;

import fr.ups.m2dl.sma.dm.system.network.IConflict;

@SuppressWarnings("all")
public abstract class ConflictManager {
  public interface Requires {
  }
  
  public interface Component extends ConflictManager.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public IConflict conflictManagement();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements ConflictManager.Component, ConflictManager.Parts {
    private final ConflictManager.Requires bridge;
    
    private final ConflictManager implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_conflictManagement() {
      assert this.conflictManagement == null: "This is a bug.";
      this.conflictManagement = this.implementation.make_conflictManagement();
      if (this.conflictManagement == null) {
      	throw new RuntimeException("make_conflictManagement() in fr.ups.m2dl.sma.dm.system.components.network.ConflictManager should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_conflictManagement();
    }
    
    public ComponentImpl(final ConflictManager implem, final ConflictManager.Requires b, final boolean doInits) {
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
    
    private IConflict conflictManagement;
    
    public IConflict conflictManagement() {
      return this.conflictManagement;
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
  
  private ConflictManager.ComponentImpl selfComponent;
  
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
  protected ConflictManager.Provides provides() {
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
  protected abstract IConflict make_conflictManagement();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected ConflictManager.Requires requires() {
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
  protected ConflictManager.Parts parts() {
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
  public synchronized ConflictManager.Component _newComponent(final ConflictManager.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of ConflictManager has already been used to create a component, use another one.");
    }
    this.init = true;
    ConflictManager.ComponentImpl  _comp = new ConflictManager.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public ConflictManager.Component newComponent() {
    return this._newComponent(new ConflictManager.Requires() {}, true);
  }
}
