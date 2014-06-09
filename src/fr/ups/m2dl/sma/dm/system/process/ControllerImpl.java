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
				long waitTime = calculateDelay();
				
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
			
			private long calculateDelay() {
				long waitTime = 3000 - (250 * speed);
				if(waitTime <= 0) {
					waitTime = 1;
				}
				return waitTime;
			}
			
			@Override
			public void nextCycle() {
				requires().cycle().doCyle();
			}
			
			@Override
			public void increaseSpeed() {
				this.speed++;
				this.thread.setDelay(calculateDelay());				
			}
			
			@Override
			public void decreaseSpeed() {
				this.speed--;
				if(speed <= 0) {
					this.speed = 1;
				}
				this.thread.setDelay(calculateDelay());	
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
