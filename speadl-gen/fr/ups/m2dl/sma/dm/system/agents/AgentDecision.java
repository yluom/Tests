package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.agents.IAgentAction;
import fr.ups.m2dl.sma.dm.system.agents.IAgentPerception;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

@SuppressWarnings("all")
public abstract class AgentDecision {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public IAgentPerception perception();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public IAgentAction actions();
  }
  
  public interface Component extends AgentDecision.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public ICycle cycle();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public ILog log();
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements AgentDecision.Component, AgentDecision.Parts {
    private final AgentDecision.Requires bridge;
    
    private final AgentDecision implementation;
    
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
      	throw new RuntimeException("make_cycle() in fr.ups.m2dl.sma.dm.system.agents.AgentDecision should not return null.");
      }
    }
    
    private void init_log() {
      assert this.log == null: "This is a bug.";
      this.log = this.implementation.make_log();
      if (this.log == null) {
      	throw new RuntimeException("make_log() in fr.ups.m2dl.sma.dm.system.agents.AgentDecision should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_cycle();
      init_log();
    }
    
    public ComponentImpl(final AgentDecision implem, final AgentDecision.Requires b, final boolean doInits) {
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
    
    private ILog log;
    
    public ILog log() {
      return this.log;
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
  
  private AgentDecision.ComponentImpl selfComponent;
  
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
  protected AgentDecision.Provides provides() {
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
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract ILog make_log();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected AgentDecision.Requires requires() {
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
  protected AgentDecision.Parts parts() {
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
  public synchronized AgentDecision.Component _newComponent(final AgentDecision.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of AgentDecision has already been used to create a component, use another one.");
    }
    this.init = true;
    AgentDecision.ComponentImpl  _comp = new AgentDecision.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
