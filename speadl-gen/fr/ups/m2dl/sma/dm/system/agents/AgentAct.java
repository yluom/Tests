package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.agents.IAgentAction;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet;

@SuppressWarnings("all")
public abstract class AgentAct {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ILocalEnvironmentSet localSet();
  }
  
  public interface Component extends AgentAct.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public IAgentAction actions();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements AgentAct.Component, AgentAct.Parts {
    private final AgentAct.Requires bridge;
    
    private final AgentAct implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_actions() {
      assert this.actions == null: "This is a bug.";
      this.actions = this.implementation.make_actions();
      if (this.actions == null) {
      	throw new RuntimeException("make_actions() in fr.ups.m2dl.sma.dm.system.agents.AgentAct should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_actions();
    }
    
    public ComponentImpl(final AgentAct implem, final AgentAct.Requires b, final boolean doInits) {
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
    
    private IAgentAction actions;
    
    public IAgentAction actions() {
      return this.actions;
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
  
  private AgentAct.ComponentImpl selfComponent;
  
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
  protected AgentAct.Provides provides() {
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
  protected abstract IAgentAction make_actions();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected AgentAct.Requires requires() {
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
  protected AgentAct.Parts parts() {
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
  public synchronized AgentAct.Component _newComponent(final AgentAct.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of AgentAct has already been used to create a component, use another one.");
    }
    this.init = true;
    AgentAct.ComponentImpl  _comp = new AgentAct.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
