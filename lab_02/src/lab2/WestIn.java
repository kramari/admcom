package lab2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class WestIn implements Runnable {

	LinkedBlockingQueue<Visitor> _visitorsOnin;
	private Museum _museum;

	@Override
	public void run() {
		_visitorsOnin = new LinkedBlockingQueue<Visitor>();
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Boolean isOpen;
				synchronized (_museum._isOpen) {
					isOpen = _museum._isOpen;
					
					if (!isOpen) {
						continue;
					}

					TimeUnit.MILLISECONDS.sleep(10);
					while (!_visitorsOnin.isEmpty()) {
						TimeUnit.MILLISECONDS.sleep(10);
						_museum.addNewVisitor(_visitorsOnin.remove());
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void addToIn(Visitor v) {
		System.out.println("Visitor " + v.get_name().toString()
				+ " wait to in!");
		_visitorsOnin.add(v);
	}

	public void set_museum(Museum _museum) {
		this._museum = _museum;
	}

	public void goAway(Visitor v) {
		System.out.println("Visitor " + v.get_name().toString() + " go away!");
		_visitorsOnin.remove(v);
	}
}
