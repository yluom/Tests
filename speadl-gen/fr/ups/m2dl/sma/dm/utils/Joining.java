package fr.ups.m2dl.sma.dm.utils;

@SuppressWarnings("all")
public abstract class Joining<T> {
  public interface Requires<T> {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public T req();
  }
  
  public interface Component<T> extends Joining.Provides<T> {
  }
  
  public interface Provides<T> {
  }
  
  public interface Parts<T> {
  }
  
  public static class ComponentImpl<T> implements Joining.Component<T>, Joining.Parts<T> {
    private final Joining.Requires<T> bridge;
    
    private final Joining<T> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final Joining<T> implem, final Joining.Requires<T> b, final boolean doInits) {
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
  }
  
  public static abstract class JoiningEntity<T> {
    public interface Requires<T> {
    }
    
    public interface Component<T> extends Joining.JoiningEntity.Provides<T> {
    }
    
    public interface Provides<T> {
      /**
       * This can be called to access the provided port.
       * 
       */
      public T prov();
    }
    
    public interface Parts<T> {
    }
    
    public static class ComponentImpl<T> implements Joining.JoiningEntity.Component<T>, Joining.JoiningEntity.Parts<T> {
      private final Joining.JoiningEntity.Requires<T> bridge;
      
      private final Joining.JoiningEntity<T> implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      private void init_prov() {
        assert this.prov == null: "This is a bug.";
        this.prov = this.implementation.make_prov();
        if (this.prov == null) {
        	throw new RuntimeException("make_prov() in fr.ups.m2dl.sma.dm.utils.Joining$JoiningEntity<T> should not return null.");
        }
      }
      
      protected void initProvidedPorts() {
        init_prov();
      }
      
      public ComponentImpl(final Joining.JoiningEntity<T> implem, final Joining.JoiningEntity.Requires<T> b, final boolean doInits) {
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
      
      private T prov;
      
      public T prov() {
        return this.prov;
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
    
    private Joining.JoiningEntity.ComponentImpl<T> selfComponent;
    
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
    protected Joining.JoiningEntity.Provides<T> provides() {
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
    protected abstract T make_prov();
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected Joining.JoiningEntity.Requires<T> requires() {
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
    protected Joining.JoiningEntity.Parts<T> parts() {
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
    public synchronized Joining.JoiningEntity.Component<T> _newComponent(final Joining.JoiningEntity.Requires<T> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of JoiningEntity has already been used to create a component, use another one.");
      }
      this.init = true;
      Joining.JoiningEntity.ComponentImpl<T>  _comp = new Joining.JoiningEntity.ComponentImpl<T>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private Joining.ComponentImpl<T> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected Joining.Provides<T> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected Joining.Requires<T> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected Joining.Parts<T> eco_parts() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
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
  
  private Joining.ComponentImpl<T> selfComponent;
  
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
  protected Joining.Provides<T> provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Joining.Requires<T> requires() {
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
  protected Joining.Parts<T> parts() {
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
  public synchronized Joining.Component<T> _newComponent(final Joining.Requires<T> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Joining has already been used to create a component, use another one.");
    }
    this.init = true;
    Joining.ComponentImpl<T>  _comp = new Joining.ComponentImpl<T>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract Joining.JoiningEntity<T> make_JoiningEntity();
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public Joining.JoiningEntity<T> _createImplementationOfJoiningEntity() {
    Joining.JoiningEntity<T> implem = make_JoiningEntity();
    if (implem == null) {
    	throw new RuntimeException("make_JoiningEntity() in fr.ups.m2dl.sma.dm.utils.Joining should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected Joining.JoiningEntity.Component<T> newJoiningEntity() {
    Joining.JoiningEntity<T> _implem = _createImplementationOfJoiningEntity();
    return _implem._newComponent(new Joining.JoiningEntity.Requires<T>() {},true);
  }
}
