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
			private int speed;
			
			@Override
			public void start(int speed) {
				this.speed = speed;
				if(speed <= 0) {
					this.speed = 1;
				}
				long waitTime = speed;
				
				thread = new ControllerThread(waitTime);
				thread.start();
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
		
		public void setDelay(long delay) {
			this.delay = delay;
		}
		
		@Override
		public void run() {
			long time = System.currentTimeMillis();
			while(!stop) {
				requires().cycle().doCyle();
				
				// waiting
				time = System.currentTimeMillis();
				while(System.currentTimeMillis() - time < delay) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
					}
				}
			}
		}
		
		public void turnOff() {
			this.stop = true;
			this.interrupt();
		}
	}

}
