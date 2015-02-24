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
public class TaskManagerImpl implements TaskManager {
    private Queue<Runnable> taskQueue;
    private boolean running;

    /** 
     * A new task manager.
     */
    public TaskManagerImpl() {
	  this.taskQueue = new LinkedList<Runnable>();
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
    public synchronized void add(Runnable task) {
	  if (running) {
		System.out.println("Adding " + task + " to queue...");
		this.taskQueue.offer(task);
		System.out.println("Waking up " + task + " task manager's thread.");
		notifyAll();
	  } else {
		throw new RuntimeException("Cannot add tasks to a stopped manager.");
	  }
    }

    @Override    
    public void run() {
	  while (running) {
		while (!taskQueue.isEmpty()) {
		    Runnable next = taskQueue.poll();
		    System.out.println("Executing task: " + next + ". Queue size: " + this.taskQueue.size() + ".");
		    next.run();
		    waitALittle();
		}
	  }
	  System.out.println("Task Manager's thread finished!");
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
}