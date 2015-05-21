package fr.ups.m2dl.sma.dm.utils;

@SuppressWarnings("all")
public abstract class Dispatcher3<T> {
  public interface Requires<T> {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public T to1();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public T to2();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public T to3();
  }
  
  public interface Component<T> extends Dispatcher3.Provides<T> {
  }
  
  public interface Provides<T> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public T from();
  }
  
  public interface Parts<T> {
  }
  
  public static class ComponentImpl<T> implements Dispatcher3.Component<T>, Dispatcher3.Parts<T> {
    private final Dispatcher3.Requires<T> bridge;
    
    private final Dispatcher3<T> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_from() {
      assert this.from == null: "This is a bug.";
      this.from = this.implementation.make_from();
      if (this.from == null) {
      	throw new RuntimeException("make_from() in fr.ups.m2dl.sma.dm.utils.Dispatcher3<T> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_from();
    }
    
    public ComponentImpl(final Dispatcher3<T> implem, final Dispatcher3.Requires<T> b, final boolean doInits) {
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
    
    private T from;
    
    public T from() {
      return this.from;
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
  
  private Dispatcher3.ComponentImpl<T> selfComponent;
  
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
  protected Dispatcher3.Provides<T> provides() {
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
  protected abstract T make_from();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Dispatcher3.Requires<T> requires() {
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
  protected Dispatcher3.Parts<T> parts() {
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
  public synchronized Dispatcher3.Component<T> _newComponent(final Dispatcher3.Requires<T> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Dispatcher3 has already been used to create a component, use another one.");
    }
    this.init = true;
    Dispatcher3.ComponentImpl<T>  _comp = new Dispatcher3.ComponentImpl<T>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
