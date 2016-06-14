package org.elsys.water;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class WaterConstructor {
;
	private ReentrantLock lock = new ReentrantLock();

	private int oxygenCounter = 0;
	private int hydrogenCounter = 0;

	private CyclicBarrier barrier = new CyclicBarrier(3);

	private Semaphore oxygenSemaphore = new Semaphore(0);
	private Semaphore hydrogenSemaphore = new Semaphore(0);

	public void proceedOxygen(Oxygen oxygen) throws Exception {
		
		lock.lock();
		boolean unlocked = false;
		oxygenCounter ++;
		if(hydrogenCounter >= 2)
		{
			
			for(int i = 0; i < 2; i++)
			{
				hydrogenSemaphore.release();
			}
				
			hydrogenCounter-=2;
			oxygenSemaphore.release();
			oxygenCounter--;
		}
		else
		{
			lock.unlock();
			unlocked = (true);
			
		}
		
		oxygenSemaphore.acquire();


		oxygen.bond();
		barrier.await();
		
		
		if(unlocked != true)
			lock.unlock();

		
		
	}

	public void proceedHydrogen(Hydrogen hydrogen) throws Exception {
		lock.lock();
		boolean running = false;
		hydrogenCounter ++;
		if (hydrogenCounter >= 2 && oxygenCounter >=1)
		{
			for(int i = 0; i < 2; i++)
				{
				hydrogenSemaphore.release();
				}
			
			hydrogenCounter-=2;
			
			oxygenSemaphore.release();

			oxygenCounter--;
			

		}
		else
		{
			lock.unlock();
			running = true;
		}

		hydrogenSemaphore.acquire();
		
		

		
		hydrogen.bond();
		
		barrier.await();
		
		if(running != true)
			lock.unlock();
	}

	public static void main(String[] args) {
		WaterConstructor constructor = new WaterConstructor();
		Random random = new Random();
		int oxygen = 0;
		int hydrogen = 0;

		while (true) {
			if (random.nextInt(3) == 0) {
				Oxygen o = new Oxygen(++oxygen, constructor);
				new Thread(o).start();
			} else {
				Hydrogen h = new Hydrogen(++hydrogen, constructor);
				new Thread(h).start();
			}
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
