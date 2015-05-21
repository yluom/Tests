package fr.ups.m2dl.sma.dm.system.components.environment;

import fr.ups.m2dl.sma.dm.system.environment.IConfigEnvironment;
import fr.ups.m2dl.sma.dm.system.environment.IGlobalEnvironmentGet;
import fr.ups.m2dl.sma.dm.system.environment.IGlobalEnvironmentSet;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentGet;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet;
import fr.ups.m2dl.sma.dm.system.environment.IPersist;
import fr.ups.m2dl.sma.dm.system.log.ILog;

@SuppressWarnings("all")
public abstract class EnvironmentRepresentation {
  public interface Requires {
  }
  
  public interface Component extends EnvironmentRepresentation.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public IGlobalEnvironmentGet globalGet();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IGlobalEnvironmentSet globalSet();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public ILocalEnvironmentGet localGet();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public ILocalEnvironmentSet localSet();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IConfigEnvironment config();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public ILog log();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IPersist persist();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements EnvironmentRepresentation.Component, EnvironmentRepresentation.Parts {
    private final EnvironmentRepresentation.Requires bridge;
    
    private final EnvironmentRepresentation implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_globalGet() {
      assert this.globalGet == null: "This is a bug.";
      this.globalGet = this.implementation.make_globalGet();
      if (this.globalGet == null) {
      	throw new RuntimeException("make_globalGet() in fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation should not return null.");
      }
    }
    
    private void init_globalSet() {
      assert this.globalSet == null: "This is a bug.";
      this.globalSet = this.implementation.make_globalSet();
      if (this.globalSet == null) {
      	throw new RuntimeException("make_globalSet() in fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation should not return null.");
      }
    }
    
    private void init_localGet() {
      assert this.localGet == null: "This is a bug.";
      this.localGet = this.implementation.make_localGet();
      if (this.localGet == null) {
      	throw new RuntimeException("make_localGet() in fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation should not return null.");
      }
    }
    
    private void init_localSet() {
      assert this.localSet == null: "This is a bug.";
      this.localSet = this.implementation.make_localSet();
      if (this.localSet == null) {
      	throw new RuntimeException("make_localSet() in fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation should not return null.");
      }
    }
    
    private void init_config() {
      assert this.config == null: "This is a bug.";
      this.config = this.implementation.make_config();
      if (this.config == null) {
      	throw new RuntimeException("make_config() in fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation should not return null.");
      }
    }
    
    private void init_log() {
      assert this.log == null: "This is a bug.";
      this.log = this.implementation.make_log();
      if (this.log == null) {
      	throw new RuntimeException("make_log() in fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation should not return null.");
      }
    }
    
    private void init_persist() {
      assert this.persist == null: "This is a bug.";
      this.persist = this.implementation.make_persist();
      if (this.persist == null) {
      	throw new RuntimeException("make_persist() in fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_globalGet();
      init_globalSet();
      init_localGet();
      init_localSet();
      init_config();
      init_log();
      init_persist();
    }
    
    public ComponentImpl(final EnvironmentRepresentation implem, final EnvironmentRepresentation.Requires b, final boolean doInits) {
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
    
    private IGlobalEnvironmentGet globalGet;
    
    public IGlobalEnvironmentGet globalGet() {
      return this.globalGet;
    }
    
    private IGlobalEnvironmentSet globalSet;
    
    public IGlobalEnvironmentSet globalSet() {
      return this.globalSet;
    }
    
    private ILocalEnvironmentGet localGet;
    
    public ILocalEnvironmentGet localGet() {
      return this.localGet;
    }
    
    private ILocalEnvironmentSet localSet;
    
    public ILocalEnvironmentSet localSet() {
      return this.localSet;
    }
    
    private IConfigEnvironment config;
    
    public IConfigEnvironment config() {
      return this.config;
    }
    
    private ILog log;
    
    public ILog log() {
      return this.log;
    }
    
    private IPersist persist;
    
    public IPersist persist() {
      return this.persist;
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
  
  private EnvironmentRepresentation.ComponentImpl selfComponent;
  
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
  protected EnvironmentRepresentation.Provides provides() {
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
  protected abstract IGlobalEnvironmentGet make_globalGet();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract IGlobalEnvironmentSet make_globalSet();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract ILocalEnvironmentGet make_localGet();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract ILocalEnvironmentSet make_localSet();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract IConfigEnvironment make_config();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract ILog make_log();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract IPersist make_persist();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected EnvironmentRepresentation.Requires requires() {
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
  protected EnvironmentRepresentation.Parts parts() {
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
  public synchronized EnvironmentRepresentation.Component _newComponent(final EnvironmentRepresentation.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of EnvironmentRepresentation has already been used to create a component, use another one.");
    }
    this.init = true;
    EnvironmentRepresentation.ComponentImpl  _comp = new EnvironmentRepresentation.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public EnvironmentRepresentation.Component newComponent() {
    return this._newComponent(new EnvironmentRepresentation.Requires() {}, true);
  }
}
