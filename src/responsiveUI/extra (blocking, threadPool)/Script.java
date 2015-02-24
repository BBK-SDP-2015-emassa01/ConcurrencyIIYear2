import java.util.concurrent.Executor;

public class Script {
    public static void main(String args[]) {
	  try {
		Script script = new Script();
		script.launch();
	  } catch (InterruptedException ex) {
		System.out.println("The main thread was interrupted");
	  }
	  System.out.println("Main thread finished!");
    }

    public void launch() throws InterruptedException {
	  TaskManager manager = new TaskManagerImpl();
	  // TaskManager manager = new BlockingQueueTaskManager();
	  // TaskManager manager = new ThreadPoolTaskManager();
	  SimpleExecutor executor = new SimpleExecutorImpl(manager);
	  executor.execute(new MyTask(1, 1000));
	  executor.execute(new MyTask(2, 2000));
	  executor.execute(new MyTask(3, 3000));
	  executor.execute(new MyTask(4, 1000));
	  System.out.println("Main thread sleeping...");
	  Thread.sleep(1000 * rand_1_10());
	  executor.execute(new MyTask(5, 1000));
	  executor.execute(new MyTask(6, 1000));
	  executor.execute(new MyTask(7, 2000));
	  executor.execute(new MyTask(8, 1500));
	  executor.execute(new MyTask(9, 1000));
	  Thread.sleep(1000 * rand_1_10());
	  executor.execute(new MyTask(10, 3000));
	  executor.execute(new MyTask(11, 1500));
	  executor.execute(new MyTask(12, 1500));
	  executor.execute(new MyTask(13, 1000));
	  executor.execute(new MyTask(14, 1000));
	  executor.shutdown();
    }

    /*
     * Returns a random int between 1 and 10
     */
    private static int rand_1_10() {
	  return (int) Math.ceil(10 * Math.random());
    }

    /**
     * A simple Runnable that has an ID and sleeps for a while
     */
    private class MyTask implements Runnable {

	  private int id;
	  private int timeToSleep;

	  /**
	   * Creates a new task.
	   *
	   * @param id the ID of this task
	   * @param timeToSleep the amount of ms to sleep
	   */
	  public MyTask(int id, int timeToSleep) {
		this.id = id;
		this.timeToSleep = timeToSleep;
	  }

	  /**
	   * Sleeps the amount of ms specified at creation time
	   */
	  public void run() {
		try {
		    System.out.println("Task " + id + " starting now.");
		    Thread.sleep(timeToSleep);
		    System.out.println("Task " + id + " finished.");
		} catch (InterruptedException ex) {
		    System.out.println("Task " + id + " was interrupted.");
		}
	  }

	  /**
	   * Returns a string that contains this task's ID
	   */
	  public String toString() {
		return "(Task " + id + ")";
	  }
    }
}