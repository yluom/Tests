/**
 * 
 */
package fr.ups.m2dl.sma.dm.start;

import java.util.Scanner;

import fr.ups.m2dl.sma.dm.gui.ConsoleGui;
import fr.ups.m2dl.sma.dm.system.SystemImpl;
import fr.ups.m2dl.sma.dm.system.components.graphic.Interface;
import fr.ups.m2dl.sma.dm.system.components.graphic.RunnableComponent;
import fr.ups.m2dl.sma.dm.system.components.graphic.RunnableComponent.Component;

/**
 * @author SERIN Kevin
 *
 */
public class StartConsole {

	public static void main(String[] args) {
		Component component = new RunnableComponent() {
			
			@Override
			protected fr.ups.m2dl.sma.dm.system.components.system.System make_system() {
				return new SystemImpl();
			}
			
			@Override
			protected Runnable make_run() {
				return new Runnable() {
					
					@Override
					public void run() {
						Scanner sc = new Scanner(System.in);
						
						System.out.print("nb agents: ");
						int nbAgents = sc.nextInt();
						sc.nextLine();
						System.out.print("nb boxes: ");
						int nbBoxes = sc.nextInt();
						sc.nextLine();
						parts().system().config().initialize(nbAgents, nbBoxes);
						
						parts().gui().cycle().afterCycle();
						while(true) {
							System.out.print("Press enter to continue");
							sc.nextLine();
							parts().system().actions().nextCycle();
						}
					}
				};
			}
			
			@Override
			protected Interface make_gui() {
				return new ConsoleGui();
			}
		}.newComponent();
		
		component.run().run();
	}

}
