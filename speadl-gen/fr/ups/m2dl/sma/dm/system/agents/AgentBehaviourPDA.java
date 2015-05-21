package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.agents.AgentAct;
import fr.ups.m2dl.sma.dm.system.agents.AgentDecision;
import fr.ups.m2dl.sma.dm.system.agents.AgentPerception;
import fr.ups.m2dl.sma.dm.system.agents.IAgentAction;
import fr.ups.m2dl.sma.dm.system.agents.IAgentPerception;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentGet;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.process.ICycle;

@SuppressWarnings("all")
public abstract class AgentBehaviourPDA {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ILocalEnvironmentGet localGet();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ILocalEnvironmentSet localSet();
  }
  
  public interface Component extends AgentBehaviourPDA.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public ILog log();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public ICycle cycle();
  }
  
  public interface Parts {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public AgentPerception.Component perception();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public AgentAct.Component actions();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public AgentDecision.Component decision();
  }
  
  public static class ComponentImpl implements AgentBehaviourPDA.Component, AgentBehaviourPDA.Parts {
    private final AgentBehaviourPDA.Requires bridge;
    
    private final AgentBehaviourPDA implementation;
    
    public void start() {
      assert this.perception != null: "This is a bug.";
      ((AgentPerception.ComponentImpl) this.perception).start();
      assert this.actions != null: "This is a bug.";
      ((AgentAct.ComponentImpl) this.actions).start();
      assert this.decision != null: "This is a bug.";
      ((AgentDecision.ComponentImpl) this.decision).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_perception() {
      assert this.perception == null: "This is a bug.";
      assert this.implem_perception == null: "This is a bug.";
      this.implem_perception = this.implementation.make_perception();
      if (this.implem_perception == null) {
      	throw new RuntimeException("make_perception() in fr.ups.m2dl.sma.dm.system.agents.AgentBehaviourPDA should not return null.");
      }
      this.perception = this.implem_perception._newComponent(new BridgeImpl_perception(), false);
      
    }
    
    private void init_actions() {
      assert this.actions == null: "This is a bug.";
      assert this.implem_actions == null: "This is a bug.";
      this.implem_actions = this.implementation.make_actions();
      if (this.implem_actions == null) {
      	throw new RuntimeException("make_actions() in fr.ups.m2dl.sma.dm.system.agents.AgentBehaviourPDA should not return null.");
      }
      this.actions = this.implem_actions._newComponent(new BridgeImpl_actions(), false);
      
    }
    
    private void init_decision() {
      assert this.decision == null: "This is a bug.";
      assert this.implem_decision == null: "This is a bug.";
      this.implem_decision = this.implementation.make_decision();
      if (this.implem_decision == null) {
      	throw new RuntimeException("make_decision() in fr.ups.m2dl.sma.dm.system.agents.AgentBehaviourPDA should not return null.");
      }
      this.decision = this.implem_decision._newComponent(new BridgeImpl_decision(), false);
      
    }
    
    protected void initParts() {
      init_perception();
      init_actions();
      init_decision();
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final AgentBehaviourPDA implem, final AgentBehaviourPDA.Requires b, final boolean doInits) {
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
    
    public ILog log() {
      return this.decision().log();
    }
    
    public ICycle cycle() {
      return this.decision().cycle();
    }
    
    private AgentPerception.Component perception;
    
    private AgentPerception implem_perception;
    
    private final class BridgeImpl_perception implements AgentPerception.Requires {
      public final ILocalEnvironmentGet localGet() {
        return AgentBehaviourPDA.ComponentImpl.this.bridge.localGet();
      }
    }
    
    public final AgentPerception.Component perception() {
      return this.perception;
    }
    
    private AgentAct.Component actions;
    
    private AgentAct implem_actions;
    
    private final class BridgeImpl_actions implements AgentAct.Requires {
      public final ILocalEnvironmentSet localSet() {
        return AgentBehaviourPDA.ComponentImpl.this.bridge.localSet();
      }
    }
    
    public final AgentAct.Component actions() {
      return this.actions;
    }
    
    private AgentDecision.Component decision;
    
    private AgentDecision implem_decision;
    
    private final class BridgeImpl_decision implements AgentDecision.Requires {
      public final IAgentAction actions() {
        return AgentBehaviourPDA.ComponentImpl.this.actions().actions();
      }
      
      public final IAgentPerception perception() {
        return AgentBehaviourPDA.ComponentImpl.this.perception().perception();
      }
    }
    
    public final AgentDecision.Component decision() {
      return this.decision;
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
  
  private AgentBehaviourPDA.ComponentImpl selfComponent;
  
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
  protected AgentBehaviourPDA.Provides provides() {
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
  protected AgentBehaviourPDA.Requires requires() {
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
  protected AgentBehaviourPDA.Parts parts() {
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
  protected abstract AgentPerception make_perception();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract AgentAct make_actions();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract AgentDecision make_decision();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized AgentBehaviourPDA.Component _newComponent(final AgentBehaviourPDA.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of AgentBehaviourPDA has already been used to create a component, use another one.");
    }
    this.init = true;
    AgentBehaviourPDA.ComponentImpl  _comp = new AgentBehaviourPDA.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
