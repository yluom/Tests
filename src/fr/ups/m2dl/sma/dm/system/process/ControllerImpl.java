/**
 * 
 */
package fr.ups.m2dl.sma.dm.system.process;

import fr.ups.m2dl.sma.dm.system.components.process.Controller;

/**
 * @author SERIN Kevin
 *
 */
public class ControllerImpl extends Controller {

	@Override
	protected IAction make_actions() {
		return new IAction() {
			private ControllerThread thread;
			
			@Override
			public void start(int speed) {
				if(speed <= 0) {
					speed = 1;
				}
				long waitTime = 5000 / speed;
				if(waitTime == 0) {
					waitTime = 1;
				}
				
				thread = new ControllerThread(waitTime);
				thread.start();
			}
			
			@Override
			public void pause() {
				thread.turnOff();
				try {
					thread.join();
				} catch (InterruptedException e) {
				}
			}
			
			@Override
			public void nextCycle() {
				requires().cycle().doCyle();
			}
		};
	}
	
	private class ControllerThread extends Thread {
		private boolean stop;
		private long delay;
		
		public ControllerThread(long delay) {
			this.stop = false;
			this.delay = delay;
		}
		
		@Override
		public void run() {
			while(!stop) {
				requires().cycle().doCyle();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
			}
		}
		
		public void turnOff() {
			this.stop = true;
			this.interrupt();
		}
	}

}
