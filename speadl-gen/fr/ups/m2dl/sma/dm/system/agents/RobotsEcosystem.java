package fr.ups.m2dl.sma.dm.system.agents;

import fr.ups.m2dl.sma.dm.system.agents.AgentBehaviourPDA;
import fr.ups.m2dl.sma.dm.system.agents.IConfigEcosystem;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentGet;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.process.ICycle;
import fr.ups.m2dl.sma.dm.utils.Joining;

@SuppressWarnings("all")
public abstract class RobotsEcosystem {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ILocalEnvironmentGet envLocalGet();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ILocalEnvironmentSet envLocalSet();
  }
  
  public interface Component extends RobotsEcosystem.Provides {
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
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IConfigEcosystem config();
  }
  
  public interface Parts {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Joining.Component<ILocalEnvironmentGet> joiningGet();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Joining.Component<ILocalEnvironmentSet> joiningSet();
  }
  
  public static class ComponentImpl implements RobotsEcosystem.Component, RobotsEcosystem.Parts {
    private final RobotsEcosystem.Requires bridge;
    
    private final RobotsEcosystem implementation;
    
    public void start() {
      assert this.joiningGet != null: "This is a bug.";
      ((Joining.ComponentImpl<ILocalEnvironmentGet>) this.joiningGet).start();
      assert this.joiningSet != null: "This is a bug.";
      ((Joining.ComponentImpl<ILocalEnvironmentSet>) this.joiningSet).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_joiningGet() {
      assert this.joiningGet == null: "This is a bug.";
      assert this.implem_joiningGet == null: "This is a bug.";
      this.implem_joiningGet = this.implementation.make_joiningGet();
      if (this.implem_joiningGet == null) {
      	throw new RuntimeException("make_joiningGet() in fr.ups.m2dl.sma.dm.system.agents.RobotsEcosystem should not return null.");
      }
      this.joiningGet = this.implem_joiningGet._newComponent(new BridgeImpl_joiningGet(), false);
      
    }
    
    private void init_joiningSet() {
      assert this.joiningSet == null: "This is a bug.";
      assert this.implem_joiningSet == null: "This is a bug.";
      this.implem_joiningSet = this.implementation.make_joiningSet();
      if (this.implem_joiningSet == null) {
      	throw new RuntimeException("make_joiningSet() in fr.ups.m2dl.sma.dm.system.agents.RobotsEcosystem should not return null.");
      }
      this.joiningSet = this.implem_joiningSet._newComponent(new BridgeImpl_joiningSet(), false);
      
    }
    
    protected void initParts() {
      init_joiningGet();
      init_joiningSet();
    }
    
    private void init_cycle() {
      assert this.cycle == null: "This is a bug.";
      this.cycle = this.implementation.make_cycle();
      if (this.cycle == null) {
      	throw new RuntimeException("make_cycle() in fr.ups.m2dl.sma.dm.system.agents.RobotsEcosystem should not return null.");
      }
    }
    
    private void init_log() {
      assert this.log == null: "This is a bug.";
      this.log = this.implementation.make_log();
      if (this.log == null) {
      	throw new RuntimeException("make_log() in fr.ups.m2dl.sma.dm.system.agents.RobotsEcosystem should not return null.");
      }
    }
    
    private void init_config() {
      assert this.config == null: "This is a bug.";
      this.config = this.implementation.make_config();
      if (this.config == null) {
      	throw new RuntimeException("make_config() in fr.ups.m2dl.sma.dm.system.agents.RobotsEcosystem should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_cycle();
      init_log();
      init_config();
    }
    
    public ComponentImpl(final RobotsEcosystem implem, final RobotsEcosystem.Requires b, final boolean doInits) {
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
    
    private IConfigEcosystem config;
    
    public IConfigEcosystem config() {
      return this.config;
    }
    
    private Joining.Component<ILocalEnvironmentGet> joiningGet;
    
    private Joining<ILocalEnvironmentGet> implem_joiningGet;
    
    private final class BridgeImpl_joiningGet implements Joining.Requires<ILocalEnvironmentGet> {
      public final ILocalEnvironmentGet req() {
        return RobotsEcosystem.ComponentImpl.this.bridge.envLocalGet();
      }
    }
    
    public final Joining.Component<ILocalEnvironmentGet> joiningGet() {
      return this.joiningGet;
    }
    
    private Joining.Component<ILocalEnvironmentSet> joiningSet;
    
    private Joining<ILocalEnvironmentSet> implem_joiningSet;
    
    private final class BridgeImpl_joiningSet implements Joining.Requires<ILocalEnvironmentSet> {
      public final ILocalEnvironmentSet req() {
        return RobotsEcosystem.ComponentImpl.this.bridge.envLocalSet();
      }
    }
    
    public final Joining.Component<ILocalEnvironmentSet> joiningSet() {
      return this.joiningSet;
    }
  }
  
  public static abstract class Robot {
    public interface Requires {
    }
    
    public interface Component extends RobotsEcosystem.Robot.Provides {
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
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Joining.JoiningEntity.Component<ILocalEnvironmentGet> joinGet();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Joining.JoiningEntity.Component<ILocalEnvironmentSet> joinSet();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public AgentBehaviourPDA.Component behaviour();
    }
    
    public static class ComponentImpl implements RobotsEcosystem.Robot.Component, RobotsEcosystem.Robot.Parts {
      private final RobotsEcosystem.Robot.Requires bridge;
      
      private final RobotsEcosystem.Robot implementation;
      
      public void start() {
        assert this.joinGet != null: "This is a bug.";
        ((Joining.JoiningEntity.ComponentImpl<ILocalEnvironmentGet>) this.joinGet).start();
        assert this.joinSet != null: "This is a bug.";
        ((Joining.JoiningEntity.ComponentImpl<ILocalEnvironmentSet>) this.joinSet).start();
        assert this.behaviour != null: "This is a bug.";
        ((AgentBehaviourPDA.ComponentImpl) this.behaviour).start();
        this.implementation.start();
        this.implementation.started = true;
      }
      
      private void init_joinGet() {
        assert this.joinGet == null: "This is a bug.";
        assert this.implementation.use_joinGet != null: "This is a bug.";
        this.joinGet = this.implementation.use_joinGet._newComponent(new BridgeImpl_joiningGet_joinGet(), false);
        
      }
      
      private void init_joinSet() {
        assert this.joinSet == null: "This is a bug.";
        assert this.implementation.use_joinSet != null: "This is a bug.";
        this.joinSet = this.implementation.use_joinSet._newComponent(new BridgeImpl_joiningSet_joinSet(), false);
        
      }
      
      private void init_behaviour() {
        assert this.behaviour == null: "This is a bug.";
        assert this.implem_behaviour == null: "This is a bug.";
        this.implem_behaviour = this.implementation.make_behaviour();
        if (this.implem_behaviour == null) {
        	throw new RuntimeException("make_behaviour() in fr.ups.m2dl.sma.dm.system.agents.RobotsEcosystem$Robot should not return null.");
        }
        this.behaviour = this.implem_behaviour._newComponent(new BridgeImpl_behaviour(), false);
        
      }
      
      protected void initParts() {
        init_joinGet();
        init_joinSet();
        init_behaviour();
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final RobotsEcosystem.Robot implem, final RobotsEcosystem.Robot.Requires b, final boolean doInits) {
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
      
      public ICycle cycle() {
        return this.behaviour().cycle();
      }
      
      public ILog log() {
        return this.behaviour().log();
      }
      
      private Joining.JoiningEntity.Component<ILocalEnvironmentGet> joinGet;
      
      private final class BridgeImpl_joiningGet_joinGet implements Joining.JoiningEntity.Requires<ILocalEnvironmentGet> {
      }
      
      public final Joining.JoiningEntity.Component<ILocalEnvironmentGet> joinGet() {
        return this.joinGet;
      }
      
      private Joining.JoiningEntity.Component<ILocalEnvironmentSet> joinSet;
      
      private final class BridgeImpl_joiningSet_joinSet implements Joining.JoiningEntity.Requires<ILocalEnvironmentSet> {
      }
      
      public final Joining.JoiningEntity.Component<ILocalEnvironmentSet> joinSet() {
        return this.joinSet;
      }
      
      private AgentBehaviourPDA.Component behaviour;
      
      private AgentBehaviourPDA implem_behaviour;
      
      private final class BridgeImpl_behaviour implements AgentBehaviourPDA.Requires {
        public final ILocalEnvironmentGet localGet() {
          return RobotsEcosystem.Robot.ComponentImpl.this.joinGet().prov();
        }
        
        public final ILocalEnvironmentSet localSet() {
          return RobotsEcosystem.Robot.ComponentImpl.this.joinSet().prov();
        }
      }
      
      public final AgentBehaviourPDA.Component behaviour() {
        return this.behaviour;
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
    
    private RobotsEcosystem.Robot.ComponentImpl selfComponent;
    
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
    protected RobotsEcosystem.Robot.Provides provides() {
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
    protected RobotsEcosystem.Robot.Requires requires() {
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
    protected RobotsEcosystem.Robot.Parts parts() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    private Joining.JoiningEntity<ILocalEnvironmentGet> use_joinGet;
    
    private Joining.JoiningEntity<ILocalEnvironmentSet> use_joinSet;
    
    /**
     * This should be overridden by the implementation to define how to create this sub-component.
     * This will be called once during the construction of the component to initialize this sub-component.
     * 
     */
    protected abstract AgentBehaviourPDA make_behaviour();
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized RobotsEcosystem.Robot.Component _newComponent(final RobotsEcosystem.Robot.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of Robot has already been used to create a component, use another one.");
      }
      this.init = true;
      RobotsEcosystem.Robot.ComponentImpl  _comp = new RobotsEcosystem.Robot.ComponentImpl(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private RobotsEcosystem.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected RobotsEcosystem.Provides eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected RobotsEcosystem.Requires eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected RobotsEcosystem.Parts eco_parts() {
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
  
  private RobotsEcosystem.ComponentImpl selfComponent;
  
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
  protected RobotsEcosystem.Provides provides() {
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
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract IConfigEcosystem make_config();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected RobotsEcosystem.Requires requires() {
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
  protected RobotsEcosystem.Parts parts() {
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
  protected abstract Joining<ILocalEnvironmentGet> make_joiningGet();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Joining<ILocalEnvironmentSet> make_joiningSet();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized RobotsEcosystem.Component _newComponent(final RobotsEcosystem.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of RobotsEcosystem has already been used to create a component, use another one.");
    }
    this.init = true;
    RobotsEcosystem.ComponentImpl  _comp = new RobotsEcosystem.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract RobotsEcosystem.Robot make_Robot(final String identifier);
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public RobotsEcosystem.Robot _createImplementationOfRobot(final String identifier) {
    RobotsEcosystem.Robot implem = make_Robot(identifier);
    if (implem == null) {
    	throw new RuntimeException("make_Robot() in fr.ups.m2dl.sma.dm.system.agents.RobotsEcosystem should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    assert this.selfComponent.implem_joiningGet != null: "This is a bug.";
    assert implem.use_joinGet == null: "This is a bug.";
    implem.use_joinGet = this.selfComponent.implem_joiningGet._createImplementationOfJoiningEntity();
    assert this.selfComponent.implem_joiningSet != null: "This is a bug.";
    assert implem.use_joinSet == null: "This is a bug.";
    implem.use_joinSet = this.selfComponent.implem_joiningSet._createImplementationOfJoiningEntity();
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected RobotsEcosystem.Robot.Component newRobot(final String identifier) {
    RobotsEcosystem.Robot _implem = _createImplementationOfRobot(identifier);
    return _implem._newComponent(new RobotsEcosystem.Robot.Requires() {},true);
  }
}
