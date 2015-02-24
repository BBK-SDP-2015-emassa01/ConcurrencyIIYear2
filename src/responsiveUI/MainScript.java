package responsiveUI;
import java.util.concurrent.Executor;

public class MainScript {

	    public static void main(String args[]) {
		  try {
			MainScript script = new MainScript();
			script.launch();
			
		  } catch (InterruptedException ex) {
			System.out.println("The main thread was interrupted");
		  }
		  System.out.println("Main thread finished!");
	    }

	    public void launch() throws InterruptedException {
		  SimpleExecutor1 executor = new SimpleExecutor1();
		  System.out.println("MAX :"+executor.getMaxPendingTime());
		  executor.execute(new TimedTask(1, 1000));
		  executor.execute(new TimedTask(2, 2000));
		  executor.execute(new TimedTask(3, 3000));
		  executor.execute(new TimedTask(4, 1000));
		  
		  executor.execute(new TimedTask(5, 1000));
		  executor.execute(new TimedTask(6, 1000));
		  executor.execute(new TimedTask(7, 1000));
		  executor.execute(new TimedTask(8, 1000));
		  executor.execute(new TimedTask(9, 1000));
		  System.out.println("MAX :"+executor.getMaxPendingTime());
		  System.out.println("Main thread sleeping...");
		  Thread.sleep(10000);
		  executor.shutdown();
	    }

	    /**
	     * A simple Runnable that has an ID and sleeps for a while
	     */
//	    private class MyTask implements Runnable {
//
//		  private int id;
//		  private int timeToSleep;
//
//		  /**
//		   * Creates a new task.
//		   *
//		   * @param id the ID of this task
//		   * @param timeToSleep the amount of ms to sleep
//		   */
//		  public MyTask(int id, int timeToSleep) {
//			this.id = id;
//			this.timeToSleep = timeToSleep;
//		  }
//
//		  /**
//		   * Sleeps the amount of ms specified at creation time
//		   */
//		  public void run() {
//			try {
//			    System.out.println("Task " + id + " starting now.");
//			    Thread.sleep(timeToSleep);
//			    System.out.println("Task " + id + " finished.");
//			} catch (InterruptedException ex) {
//			    System.out.println("Task " + id + " was interrupted.");
//			}
//		  }
//
//		  /**
//		   * Returns a string that contains this task's ID
//		   */
//		  public String toString() {
//			return "(Task " + id + ")";
//		  }
//	    }
	}

