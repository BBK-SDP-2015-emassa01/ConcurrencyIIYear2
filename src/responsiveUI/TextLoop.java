package responsiveUI;

import java.util.Scanner;
import java.util.concurrent.Executor;

public class TextLoop implements Runnable {
	public static final int COUNT = 10;
	private final String name;

	public TextLoop(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		for (int i = 0; i < COUNT; i++) {
			System.out.println("Loop:" + name + ", iteration:" + i + ".");
		}
	}

	public static void main(String args[]) {
		System.out.println("Option 0 for no threads; \nOption1 for threads.");
		String option = new Scanner(System.in).nextLine();
		if (option.length() < 1 || (!option.equals("0") && !option.equals("1"))) {
			System.out.println("USAGE: java TextLoop <mode>");
			System.out.println(" mode 0: without threads");
			System.out.println(" mode 1: with threads");
		} else if (option.equals("0")) {
			for (int i = 0; i < 10; i++) {
				Runnable r = new TextLoop("Thread " + i);
				r.run();
			}
		} else {
			for (int i = 0; i < 10; i++) {
				Runnable r = new TextLoop("Thread " + i);
				Executor e = new SimpleExecutor1();
				e.execute(r);

				// Thread t = new Thread(r);
				// t.start();
			}
		}
	}
}
