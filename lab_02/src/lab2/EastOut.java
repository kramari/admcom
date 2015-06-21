package lab2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class EastOut implements Runnable {

	LinkedBlockingQueue<Visitor> _visitorsOnOut;
	private Museum _museum;

	@Override
	public void run() {
		_visitorsOnOut = new LinkedBlockingQueue<Visitor>();
		while (!Thread.currentThread().isInterrupted()) {
			try {
				TimeUnit.MILLISECONDS.sleep(10);
				while (!_visitorsOnOut.isEmpty()) {
					TimeUnit.MILLISECONDS.sleep(10);
					_museum.exitVisitor(_visitorsOnOut.remove());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void addToRemove(Visitor v) {
//		System.out.println("Visitor " + v.get_name().toString() + " wait to out!");
		_visitorsOnOut.add(v);
	}

	public void set_museum(Museum _museum) {
		this._museum = _museum;
	}
}

