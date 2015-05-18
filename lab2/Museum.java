package lad2.lab2;

class East extends Thread {
	public volatile static boolean finish = false;     // запрос на закрытие потока
	
	public void run(){
		while(true){                                   // в цикле добавляем посетителей, если открыта восточная дверь
			synchronized (Museum.monitor) {
				if(Museum.isOpen()) {
					Museum.cameVisitor();
					System.out.println("\tCame visitor to the east door. In Museum = " + Museum.getVisitorCount());
				}
			}
			if(finish) {
				System.out.println("Close East door");
				return;                                  // закрываем поток когда "отправлен" запрос
			}
			try{
				sleep(800);
			}catch(InterruptedException e){}
		}
	}
}

class West extends Thread{
	public volatile static boolean finish = false;      // запрос на закрытие потока
	
	public void run(){
		while(true){
			synchronized (Museum.monitor) {
				if(Museum.hasVisitors()){                // удаляем посетителей в цикле, пока их больше 0
					Museum.leftVisitor();
					System.out.println("\tLeft visitor from the west door. In Museum = " + Museum.getVisitorCount());
				}
				if(!Museum.hasVisitors() && finish) {
					System.out.println("Close West door");
					return;                             // закрываем поток, музей пуст и отправлен запрос
				}
			}
			try{
				sleep(1500);                             // потом засыпаем на 1.5 секунды
			}catch(InterruptedException e){}
		}
	}
}

class Control extends Thread{
	public volatile static boolean finish = false;       // запрос на закрытие потока
	
	public synchronized void run(){
		while(true){
			try {
				System.out.println("\t\tControl wait");
				this.wait();
			} catch (InterruptedException e) {          // Музейщик по пинку директора (по прерыванию)
				if (finish) {
					System.out.println("The controller was died");
					return;                             // Закрывает поток, когда отправлен запрос
				}
				synchronized (Museum.monitor) {
					System.out.print("Control change Museum State."); // открывает/закрывает вход в музей
					Museum.changeMuseumState();
				}
			}
		}
	}
}

class Director extends Thread{
	
	public void run(){
		for(int i = 0; i < 5; i++){
			System.out.print("Director send a command: ");
			if(Museum.isOpen()){
				System.out.println("close the east door");
			} else {
				System.out.println("open the east door");
			}
			Museum.museumStaff.interrupt();                    // прерывание на открытие/закрытие двери
			try {
				sleep(5000);
			} catch (InterruptedException e) {}
		}
		East.finish = true;                                    // закрытие дверей навсегда
		West.finish = true;
		try {
			Museum.eastGate.join();
			Museum.westGate.join();
		} catch (InterruptedException e) {}
		Control.finish = true;                                  // Close the control thread
		Museum.museumStaff.interrupt();
		System.out.println("The director was died");
		try {
			Museum.museumStaff.join();
		} catch (InterruptedException e) {}
		return;
	}
}

public class Museum
{
	private volatile static int visitorCount = 0;
	private volatile static boolean museumIsOpen = false;
		// монитор для синхронизации изменения и вывода кол-ва посетителей
		// (используется в потоках входа и выхода)
	public static final Object monitor = new Object();
	public static final Control museumStaff = new Control();
	public static final West westGate = new West();
	public static final East eastGate = new East();
	public static final Director dir = new Director();
	public synchronized static void cameVisitor() { visitorCount++; }
	public synchronized static void leftVisitor() { visitorCount--; }
	public synchronized static int getVisitorCount() { return visitorCount; }
	public synchronized static boolean hasVisitors() { return (visitorCount > 0); }
	public synchronized static void changeMuseumState() { museumIsOpen = !museumIsOpen; }
	public synchronized static boolean isOpen() { return museumIsOpen; }
	
	public static void main(String[] args) {
		System.out.println("Let's go");
		westGate.setName("westGate");
		eastGate.setName("eastGate");
		westGate.start();
		eastGate.start();
		museumStaff.setName("museumStaff");
		museumStaff.start();
		dir.setName("director");
		dir.start();
		try {
			dir.join();
		} catch (InterruptedException e) {}
		System.out.println("The End");
		return;
	}
}