package lab2;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Director implements Runnable{
	
	Random _random;
	private Museum _museum;
	
	@Override
	public void run() {
		_random = new Random();
		while(!Thread.currentThread().isInterrupted()){
			try {
				TimeUnit.SECONDS.sleep(_random.nextInt(10) + 5);
				_museum.closeOpenMuseum();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void set_museum(Museum _museum) {
		this._museum = _museum;
	}
}
