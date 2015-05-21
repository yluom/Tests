package fr.ups.m2dl.sma.dm.system.components.graphic;

import fr.ups.m2dl.sma.dm.system.components.graphic.Interface;
import fr.ups.m2dl.sma.dm.system.components.system.System;
import fr.ups.m2dl.sma.dm.system.configuration.IConfig;
import fr.ups.m2dl.sma.dm.system.environment.IGlobalEnvironmentGet;
import fr.ups.m2dl.sma.dm.system.process.IAction;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

@SuppressWarnings("all")
public abstract class RunnableComponent {
  public interface Requires {
  }
  
  public interface Component extends RunnableComponent.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public Runnable run();
  }
  
  public interface Parts {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Interface.Component gui();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public System.Component system();
  }
  
  public static class ComponentImpl implements RunnableComponent.Component, RunnableComponent.Parts {
    private final RunnableComponent.Requires bridge;
    
    private final RunnableComponent implementation;
    
    public void start() {
      assert this.gui != null: "This is a bug.";
      ((Interface.ComponentImpl) this.gui).start();
      assert this.system != null: "This is a bug.";
      ((System.ComponentImpl) this.system).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_gui() {
      assert this.gui == null: "This is a bug.";
      assert this.implem_gui == null: "This is a bug.";
      this.implem_gui = this.implementation.make_gui();
      if (this.implem_gui == null) {
      	throw new RuntimeException("make_gui() in fr.ups.m2dl.sma.dm.system.components.graphic.RunnableComponent should not return null.");
      }
      this.gui = this.implem_gui._newComponent(new BridgeImpl_gui(), false);
      
    }
    
    private void init_system() {
      assert this.system == null: "This is a bug.";
      assert this.implem_system == null: "This is a bug.";
      this.implem_system = this.implementation.make_system();
      if (this.implem_system == null) {
      	throw new RuntimeException("make_system() in fr.ups.m2dl.sma.dm.system.components.graphic.RunnableComponent should not return null.");
      }
      this.system = this.implem_system._newComponent(new BridgeImpl_system(), false);
      
    }
    
    protected void initParts() {
      init_gui();
      init_system();
    }
    
    private void init_run() {
      assert this.run == null: "This is a bug.";
      this.run = this.implementation.make_run();
      if (this.run == null) {
      	throw new RuntimeException("make_run() in fr.ups.m2dl.sma.dm.system.components.graphic.RunnableComponent should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_run();
    }
    
    public ComponentImpl(final RunnableComponent implem, final RunnableComponent.Requires b, final boolean doInits) {
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
    
    private Runnable run;
    
    public Runnable run() {
      return this.run;
    }
    
    private Interface.Component gui;
    
    private Interface implem_gui;
    
    private final class BridgeImpl_gui implements Interface.Requires {
      public final IAction actions() {
        return RunnableComponent.ComponentImpl.this.system().actions();
      }
      
      public final IGlobalEnvironmentGet envGet() {
        return RunnableComponent.ComponentImpl.this.system().environmentGet();
      }
      
      public final IConfig config() {
        return RunnableComponent.ComponentImpl.this.system().config();
      }
    }
    
    public final Interface.Component gui() {
      return this.gui;
    }
    
    private System.Component system;
    
    private fr.ups.m2dl.sma.dm.system.components.system.System implem_system;
    
    private final class BridgeImpl_system implements System.Requires {
      public final ICycle externCycle() {
        return RunnableComponent.ComponentImpl.this.gui().cycle();
      }
    }
    
    public final System.Component system() {
      return this.system;
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
  
  private RunnableComponent.ComponentImpl selfComponent;
  
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
  protected RunnableComponent.Provides provides() {
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
  protected abstract Runnable make_run();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected RunnableComponent.Requires requires() {
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
  protected RunnableComponent.Parts parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Interface make_gui();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract fr.ups.m2dl.sma.dm.system.components.system.System make_system();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized RunnableComponent.Component _newComponent(final RunnableComponent.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of RunnableComponent has already been used to create a component, use another one.");
    }
    this.init = true;
    RunnableComponent.ComponentImpl  _comp = new RunnableComponent.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public RunnableComponent.Component newComponent() {
    return this._newComponent(new RunnableComponent.Requires() {}, true);
  }
}
