package fr.ups.m2dl.sma.dm.system.components.system;

import fr.ups.m2dl.sma.dm.system.agents.IConfigEcosystem;
import fr.ups.m2dl.sma.dm.system.agents.RobotsEcosystem;
import fr.ups.m2dl.sma.dm.system.components.configuration.Configurator;
import fr.ups.m2dl.sma.dm.system.components.environment.EnvironmentRepresentation;
import fr.ups.m2dl.sma.dm.system.components.log.Printer;
import fr.ups.m2dl.sma.dm.system.components.network.NetworkCycleManager;
import fr.ups.m2dl.sma.dm.system.components.process.Controller;
import fr.ups.m2dl.sma.dm.system.components.process.CycleManager;
import fr.ups.m2dl.sma.dm.system.configuration.IConfig;
import fr.ups.m2dl.sma.dm.system.environment.IConfigEnvironment;
import fr.ups.m2dl.sma.dm.system.environment.IGlobalEnvironmentGet;
import fr.ups.m2dl.sma.dm.system.environment.IGlobalEnvironmentSet;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentGet;
import fr.ups.m2dl.sma.dm.system.environment.ILocalEnvironmentSet;
import fr.ups.m2dl.sma.dm.system.log.ILog;
import fr.ups.m2dl.sma.dm.system.network.ICom;
import fr.ups.m2dl.sma.dm.system.network.IConfigNetwork;
import fr.ups.m2dl.sma.dm.system.process.IAction;
import fr.ups.m2dl.sma.dm.system.process.ICycle;
import fr.ups.m2dl.sma.dm.utils.Dispatcher2;
import fr.ups.m2dl.sma.dm.utils.Dispatcher3;

@SuppressWarnings("all")
public abstract class System {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ICycle externCycle();
  }
  
  public interface Component extends System.Provides {
  }
  
  public interface Provides {
    /**
     * This can be called to access the provided port.
     * 
     */
    public IAction actions();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IGlobalEnvironmentGet environmentGet();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public IConfig config();
  }
  
  public interface Parts {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public NetworkCycleManager.Component networkManager();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public EnvironmentRepresentation.Component environment();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public RobotsEcosystem.Component ecox();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Dispatcher2.Component<ILog> logDispatcher();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Printer.Component printer();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Dispatcher3.Component<ICycle> cycleDispatcher();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public CycleManager.Component cycleManager();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Controller.Component controller();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Configurator.Component configurator();
  }
  
  public static class ComponentImpl implements System.Component, System.Parts {
    private final System.Requires bridge;
    
    private final System implementation;
    
    public void start() {
      assert this.networkManager != null: "This is a bug.";
      ((NetworkCycleManager.ComponentImpl) this.networkManager).start();
      assert this.environment != null: "This is a bug.";
      ((EnvironmentRepresentation.ComponentImpl) this.environment).start();
      assert this.ecox != null: "This is a bug.";
      ((RobotsEcosystem.ComponentImpl) this.ecox).start();
      assert this.logDispatcher != null: "This is a bug.";
      ((Dispatcher2.ComponentImpl<ILog>) this.logDispatcher).start();
      assert this.printer != null: "This is a bug.";
      ((Printer.ComponentImpl) this.printer).start();
      assert this.cycleDispatcher != null: "This is a bug.";
      ((Dispatcher3.ComponentImpl<ICycle>) this.cycleDispatcher).start();
      assert this.cycleManager != null: "This is a bug.";
      ((CycleManager.ComponentImpl) this.cycleManager).start();
      assert this.controller != null: "This is a bug.";
      ((Controller.ComponentImpl) this.controller).start();
      assert this.configurator != null: "This is a bug.";
      ((Configurator.ComponentImpl) this.configurator).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_networkManager() {
      assert this.networkManager == null: "This is a bug.";
      assert this.implem_networkManager == null: "This is a bug.";
      this.implem_networkManager = this.implementation.make_networkManager();
      if (this.implem_networkManager == null) {
      	throw new RuntimeException("make_networkManager() in fr.ups.m2dl.sma.dm.system.components.system.System should not return null.");
      }
      this.networkManager = this.implem_networkManager._newComponent(new BridgeImpl_networkManager(), false);
      
    }
    
    private void init_environment() {
      assert this.environment == null: "This is a bug.";
      assert this.implem_environment == null: "This is a bug.";
      this.implem_environment = this.implementation.make_environment();
      if (this.implem_environment == null) {
      	throw new RuntimeException("make_environment() in fr.ups.m2dl.sma.dm.system.components.system.System should not return null.");
      }
      this.environment = this.implem_environment._newComponent(new BridgeImpl_environment(), false);
      
    }
    
    private void init_ecox() {
      assert this.ecox == null: "This is a bug.";
      assert this.implem_ecox == null: "This is a bug.";
      this.implem_ecox = this.implementation.make_ecox();
      if (this.implem_ecox == null) {
      	throw new RuntimeException("make_ecox() in fr.ups.m2dl.sma.dm.system.components.system.System should not return null.");
      }
      this.ecox = this.implem_ecox._newComponent(new BridgeImpl_ecox(), false);
      
    }
    
    private void init_logDispatcher() {
      assert this.logDispatcher == null: "This is a bug.";
      assert this.implem_logDispatcher == null: "This is a bug.";
      this.implem_logDispatcher = this.implementation.make_logDispatcher();
      if (this.implem_logDispatcher == null) {
      	throw new RuntimeException("make_logDispatcher() in fr.ups.m2dl.sma.dm.system.components.system.System should not return null.");
      }
      this.logDispatcher = this.implem_logDispatcher._newComponent(new BridgeImpl_logDispatcher(), false);
      
    }
    
    private void init_printer() {
      assert this.printer == null: "This is a bug.";
      assert this.implem_printer == null: "This is a bug.";
      this.implem_printer = this.implementation.make_printer();
      if (this.implem_printer == null) {
      	throw new RuntimeException("make_printer() in fr.ups.m2dl.sma.dm.system.components.system.System should not return null.");
      }
      this.printer = this.implem_printer._newComponent(new BridgeImpl_printer(), false);
      
    }
    
    private void init_cycleDispatcher() {
      assert this.cycleDispatcher == null: "This is a bug.";
      assert this.implem_cycleDispatcher == null: "This is a bug.";
      this.implem_cycleDispatcher = this.implementation.make_cycleDispatcher();
      if (this.implem_cycleDispatcher == null) {
      	throw new RuntimeException("make_cycleDispatcher() in fr.ups.m2dl.sma.dm.system.components.system.System should not return null.");
      }
      this.cycleDispatcher = this.implem_cycleDispatcher._newComponent(new BridgeImpl_cycleDispatcher(), false);
      
    }
    
    private void init_cycleManager() {
      assert this.cycleManager == null: "This is a bug.";
      assert this.implem_cycleManager == null: "This is a bug.";
      this.implem_cycleManager = this.implementation.make_cycleManager();
      if (this.implem_cycleManager == null) {
      	throw new RuntimeException("make_cycleManager() in fr.ups.m2dl.sma.dm.system.components.system.System should not return null.");
      }
      this.cycleManager = this.implem_cycleManager._newComponent(new BridgeImpl_cycleManager(), false);
      
    }
    
    private void init_controller() {
      assert this.controller == null: "This is a bug.";
      assert this.implem_controller == null: "This is a bug.";
      this.implem_controller = this.implementation.make_controller();
      if (this.implem_controller == null) {
      	throw new RuntimeException("make_controller() in fr.ups.m2dl.sma.dm.system.components.system.System should not return null.");
      }
      this.controller = this.implem_controller._newComponent(new BridgeImpl_controller(), false);
      
    }
    
    private void init_configurator() {
      assert this.configurator == null: "This is a bug.";
      assert this.implem_configurator == null: "This is a bug.";
      this.implem_configurator = this.implementation.make_configurator();
      if (this.implem_configurator == null) {
      	throw new RuntimeException("make_configurator() in fr.ups.m2dl.sma.dm.system.components.system.System should not return null.");
      }
      this.configurator = this.implem_configurator._newComponent(new BridgeImpl_configurator(), false);
      
    }
    
    protected void initParts() {
      init_networkManager();
      init_environment();
      init_ecox();
      init_logDispatcher();
      init_printer();
      init_cycleDispatcher();
      init_cycleManager();
      init_controller();
      init_configurator();
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final System implem, final System.Requires b, final boolean doInits) {
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
    
    public IAction actions() {
      return this.controller().actions();
    }
    
    public IGlobalEnvironmentGet environmentGet() {
      return this.environment().globalGet();
    }
    
    public IConfig config() {
      return this.configurator().config();
    }
    
    private NetworkCycleManager.Component networkManager;
    
    private NetworkCycleManager implem_networkManager;
    
    private final class BridgeImpl_networkManager implements NetworkCycleManager.Requires {
    }
    
    public final NetworkCycleManager.Component networkManager() {
      return this.networkManager;
    }
    
    private EnvironmentRepresentation.Component environment;
    
    private EnvironmentRepresentation implem_environment;
    
    private final class BridgeImpl_environment implements EnvironmentRepresentation.Requires {
    }
    
    public final EnvironmentRepresentation.Component environment() {
      return this.environment;
    }
    
    private RobotsEcosystem.Component ecox;
    
    private RobotsEcosystem implem_ecox;
    
    private final class BridgeImpl_ecox implements RobotsEcosystem.Requires {
      public final ILocalEnvironmentGet envLocalGet() {
        return System.ComponentImpl.this.environment().localGet();
      }
      
      public final ILocalEnvironmentSet envLocalSet() {
        return System.ComponentImpl.this.environment().localSet();
      }
    }
    
    public final RobotsEcosystem.Component ecox() {
      return this.ecox;
    }
    
    private Dispatcher2.Component<ILog> logDispatcher;
    
    private Dispatcher2<ILog> implem_logDispatcher;
    
    private final class BridgeImpl_logDispatcher implements Dispatcher2.Requires<ILog> {
      public final ILog to1() {
        return System.ComponentImpl.this.environment().log();
      }
      
      public final ILog to2() {
        return System.ComponentImpl.this.ecox().log();
      }
    }
    
    public final Dispatcher2.Component<ILog> logDispatcher() {
      return this.logDispatcher;
    }
    
    private Printer.Component printer;
    
    private Printer implem_printer;
    
    private final class BridgeImpl_printer implements Printer.Requires {
      public final ILog logs() {
        return System.ComponentImpl.this.logDispatcher().from();
      }
    }
    
    public final Printer.Component printer() {
      return this.printer;
    }
    
    private Dispatcher3.Component<ICycle> cycleDispatcher;
    
    private Dispatcher3<ICycle> implem_cycleDispatcher;
    
    private final class BridgeImpl_cycleDispatcher implements Dispatcher3.Requires<ICycle> {
      public final ICycle to1() {
        return System.ComponentImpl.this.ecox().cycle();
      }
      
      public final ICycle to2() {
        return System.ComponentImpl.this.printer().cycle();
      }
      
      public final ICycle to3() {
        return System.ComponentImpl.this.bridge.externCycle();
      }
    }
    
    public final Dispatcher3.Component<ICycle> cycleDispatcher() {
      return this.cycleDispatcher;
    }
    
    private CycleManager.Component cycleManager;
    
    private CycleManager implem_cycleManager;
    
    private final class BridgeImpl_cycleManager implements CycleManager.Requires {
      public final ICom com() {
        return System.ComponentImpl.this.networkManager().com();
      }
      
      public final ICycle doCyle() {
        return System.ComponentImpl.this.cycleDispatcher().from();
      }
      
      public final IGlobalEnvironmentGet envGet() {
        return System.ComponentImpl.this.environment().globalGet();
      }
      
      public final IGlobalEnvironmentSet envSet() {
        return System.ComponentImpl.this.environment().globalSet();
      }
    }
    
    public final CycleManager.Component cycleManager() {
      return this.cycleManager;
    }
    
    private Controller.Component controller;
    
    private Controller implem_controller;
    
    private final class BridgeImpl_controller implements Controller.Requires {
      public final ICycle cycle() {
        return System.ComponentImpl.this.cycleManager().cycle();
      }
    }
    
    public final Controller.Component controller() {
      return this.controller;
    }
    
    private Configurator.Component configurator;
    
    private Configurator implem_configurator;
    
    private final class BridgeImpl_configurator implements Configurator.Requires {
      public final IConfigNetwork networkConfig() {
        return System.ComponentImpl.this.networkManager().config();
      }
      
      public final IConfigEnvironment envConfig() {
        return System.ComponentImpl.this.environment().config();
      }
      
      public final IConfigEcosystem agentConfig() {
        return System.ComponentImpl.this.ecox().config();
      }
    }
    
    public final Configurator.Component configurator() {
      return this.configurator;
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
  
  private System.ComponentImpl selfComponent;
  
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
  protected System.Provides provides() {
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
  protected System.Requires requires() {
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
  protected System.Parts parts() {
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
  protected abstract NetworkCycleManager make_networkManager();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract EnvironmentRepresentation make_environment();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract RobotsEcosystem make_ecox();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Dispatcher2<ILog> make_logDispatcher();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Printer make_printer();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Dispatcher3<ICycle> make_cycleDispatcher();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract CycleManager make_cycleManager();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Controller make_controller();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Configurator make_configurator();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized System.Component _newComponent(final System.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of System has already been used to create a component, use another one.");
    }
    this.init = true;
    System.ComponentImpl  _comp = new System.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
