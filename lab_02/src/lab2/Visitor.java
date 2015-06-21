package lab2;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Visitor implements Runnable {
	private static Random _random = new Random();

	private Integer _name;
	private Exitable _exitable;
	private Enterable _enterable;
	private volatile Boolean _isImTheMuseum;

	public static Visitor genVisitor() { // фабрика
		return new Visitor(_random.nextInt(1000));
	}

	@Override
	public void run() {
		_enterable.enter(this);
		while (true) {
			try {
				TimeUnit.MILLISECONDS.sleep(500);
				int temp = _random.nextInt(10);
				if (temp > 7) {
					_exitable.exit(this);
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Boolean get_isImTheMuseum() {
		synchronized (_isImTheMuseum) {
			return _isImTheMuseum;
		}
	}

	public void set_isImTheMuseum(Boolean isImTheMuseum) {
		synchronized (_isImTheMuseum) {
			_isImTheMuseum = isImTheMuseum;
		}
	}

	private Visitor(Integer name) {
		_isImTheMuseum = false;
		_name = name;
	}

	public Integer get_name() {
		return _name;
	}

	public void set_name(Integer _name) {
		this._name = _name;
	}

	public void set_exitable(Exitable _exitable) {
		this._exitable = _exitable;
	}

	public void set_enterable(Enterable _enterable) {
		this._enterable = _enterable;
	}
}
