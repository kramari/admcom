package lab2;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Museum {
	LinkedBlockingQueue<Visitor> _visitors;
	ExecutorService _executor; // пулл потоков
	public volatile Boolean _isOpen = false;

	public Museum() {
		_visitors = new LinkedBlockingQueue<Visitor>();
		_executor = Executors.newCachedThreadPool();
	}

	public void start() {
		WestIn westIn = new WestIn();
		westIn.set_museum(this);

		EastOut eastOut = new EastOut();
		eastOut.set_museum(this);

		Director director = new Director();
		director.set_museum(this);

		_executor.submit(director);
		_executor.submit(westIn);
		_executor.submit(eastOut);

		_executor.submit(new Runnable() {
			Random _random;

			@Override
			public void run() {
				_random = new Random();
				while (!Thread.currentThread().isInterrupted()) {
					try {
						TimeUnit.SECONDS.sleep(_random.nextInt(2) + 1);
						
						Visitor v = Visitor.genVisitor();

						v.set_exitable(new Exitable() {
							@Override
							public void exit(Visitor v) {
								if (v.get_isImTheMuseum() == true) {
									eastOut.addToRemove(v);
								} else {
									westIn.goAway(v);
								}
							}
						});

						v.set_enterable(new Enterable() {
							@Override
							public void enter(Visitor v) {
								westIn.addToIn(v);
							}
						});

						_executor.submit(v);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	public void addNewVisitor(Visitor v) {
		System.out.println("Visitor " + v.get_name().toString() + " is enter!");
		v.set_isImTheMuseum(true);
		_visitors.add(v);
	}

	public void exitVisitor(Visitor v) {
		System.out.println("Visitor " + v.get_name().toString() + " is exit!");
		_visitors.remove(v);
	}

	public void closeOpenMuseum() {
		Boolean state;
		synchronized (_isOpen) {
			_isOpen = !_isOpen;
			state = _isOpen;
		}

		if (state) {
			System.out.println("Museum is open!");
		} else {
			System.out.println("Museum is close!");
		}

	}

}
