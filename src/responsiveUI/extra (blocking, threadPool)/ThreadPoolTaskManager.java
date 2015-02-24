import java.util.LinkedList;
import java.util.Queue;

/**
 * A manager of tasks. 
 *
 * Instances of this class keep a queue of tasks and 
 * execute them as soon as possible. 
 *
 * If the tasks is empty, they wait for new tasks. 
 */
public class ThreadPoolTaskManager implements TaskManager {
    private static final int MAX_THREADS = 5;
    private Queue<Runnable> taskQueue;
    private boolean running;

    /*
     * The pool of threads. A cell in the array is null if
     * and only if there is no thread running on that index.
     */
    private Thread[] threadList; 

    /** 
     * A new task manager.
     */
    public ThreadPoolTaskManager() {
	  this.taskQueue = new LinkedList<Runnable>();
	  this.threadList = new Thread[MAX_THREADS];
	  for (int i = 0; i < MAX_THREADS; i++) {
		threadList[i] = null; 
	  }
	  this.running = true;
    }

    /** 
     * Tells this task manager to finish executing any 
     * tasks already in queues and then stop. 
     */
    public void shutdown() {
	  this.running = false;
    }

    /** 
     * Adds a task to the queue of tasks. 
     *
     * @param task the task
     */
    /*
     * Implementation note: I have made only part of the method
     * synchronized so that it is more likely that the Java Virtual
     * Machine will interleave additions of tasks to the queue (on the
     * main thread) and execution of tasks (on the TaskManager's
     * thread) and it looks better/clearer on screen.
     *
     * In a real application, in which the tasks are not added almost
     * all together and locally (i.e. so the JVM has very little
     * "time" to switch to the TaskManager's thread after the main
     * thread releases the lock at the end of add(Runnable) but before
     * it acquires the lock again in the next execution of
     * add(Runnable)) synchronizing the whole method would be a better
     * option for the sake of clarity of code.
     * 
     * A more realistic add() method would look like
     *     public synchronized void add(Runnable task) {
     *         if (running) {
     *             this.taskQueue.offer(task);
     *             notifyAll();
     *         } else {
     *             throw new RuntimeException("Cannot add tasks to a stopped manager.");
     *         }
     *     }
     */
    public void add(Runnable task) {
	  if (running) {
		System.out.println("Adding " + task + " to queue...");
		synchronized (this) {
		    this.taskQueue.offer(task);
		    System.out.println("Waking up " + task + " task manager's thread.");
		    notifyAll();
		}
	  } else {
		throw new RuntimeException("Cannot add tasks to a stopped manager.");
	  }
    }

    @Override
    public void run() {
	  while (running || !taskQueue.isEmpty()) {
		int nextThreadAvailable = findAvailableThread();
		if (nextThreadAvailable != -1 && !taskQueue.isEmpty()) {
		    Runnable nextTask = taskQueue.poll();
		    System.out.println("Executing task: " + nextTask + ". Queue size: " + this.taskQueue.size() + ".");
		    launchTask(nextTask, nextThreadAvailable);
		}
		waitALittle();
	  }
	  System.out.println("Task Manager's thread finished!");
    }

    private synchronized void launchTask(Runnable task, int id) {
	  WrappedTask wrappedTask = new WrappedTask(task, id, this);
	  Thread thread = new Thread(wrappedTask);
	  this.threadList[id] = thread;
	  thread.start();
	  System.out.println("Thread " + id + " is now taken.");
    }

    private synchronized int findAvailableThread() {
	  for (int i = 0; i < MAX_THREADS; i++) {
		if (threadList[i] == null) {
		    return i;
		}
	  }
	  return -1;	  
    }

    private synchronized void freeThread(int id) {
	  // TODO: no need for the arrays of booleans --- can use null
	  this.threadList[id] = null;
	  System.out.println("Thread " + id + " is now free!");
	  notifyAll();
    }

    /*
     * This method is just a glorified call to wait(). 
     * 
     * It is separated into its own method for clarity. Otherwise, the run() method
     * is more difficult to read with all the exception handling of wait() and the 
     * calls to println().
     */
    private void waitALittle() {
	  if (!this.taskQueue.isEmpty() || !running) {
		return;
	  }
	  try {
		System.out.println("Task manager is waiting because it has nothing to do...");
		synchronized (this) {
		    wait();
		}
		System.out.println("New tasks waiting! Task manager starts again");
	  } catch (InterruptedException ex) {
		System.out.println("Task manager's thread was interrupted. Starting again...");
	  }
    }

    /*
     * Takes a task and adds a single statement at the end
     * to make sure the thread is marked as available
     */
    private class WrappedTask implements Runnable {
	  private Runnable myTask;
	  private int threadId;
	  private ThreadPoolTaskManager manager;

	  public WrappedTask(Runnable task, int threadId, ThreadPoolTaskManager manager) {
		this.myTask = task;
		this.threadId = threadId;
		this.manager = manager;
	  }

	  @Override
	  public void run() {
		// Step 1: run the task
		try {
		    myTask.run();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		// Step 2: tell the manager that the task has finished
		manager.freeThread(this.threadId);
	  }

	  @Override
	  public String toString() {
		return this.myTask.toString();
	  }
    }


}