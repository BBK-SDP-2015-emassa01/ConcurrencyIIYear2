import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * A manager of tasks. 
 *
 * Instances of this class keep a blocking queue of tasks and 
 * execute them as soon as possible. 
 *
 * If the tasks is empty, they wait for new tasks. 
 */
public class BlockingQueueTaskManager implements TaskManager {
    /**
     * Inner class WrappedTask is just a wrapper for tasks in order to
     * be able to mark the END one.
     *
     * A blocking queue has its own internal threads but does not
     * provide easy access to its internal elements when it is
     * blocked. A common way of ending a program using a blocking
     * queue is by inserting a especial element in the queue to mark
     * the "end". 
     *
     * In this case, we add a flag to our tasks to mark the "end" one. 
     */
    private class WrappedTask {
	  public Runnable myTask = null;
	  public boolean isEnd = false;
    }

    /*
     * The queue of tasks
     */
    private BlockingQueue<WrappedTask> taskQueue; 

    /*
     * True when shutdown() has been called, false otherwise
     */
    private boolean closing;

    /** 
     * A new task manager.
     */
    public BlockingQueueTaskManager() {
	  this.taskQueue = new ArrayBlockingQueue<WrappedTask>(100);
	  this.closing = false;
    }

    /** 
     * Tells this task manager to finish executing any 
     * tasks already in queues and then stop. 
     */
    public synchronized void shutdown() { // Method is non-atomic --- must be synchronized!
	  try {
		this.closing = true;
		WrappedTask endTask = new WrappedTask();
		endTask.isEnd = true;
		this.taskQueue.put(endTask);
	  } catch (InterruptedException ex) {
		ex.printStackTrace();
	  }
    }

    /** 
     * Adds a task to the queue of tasks. 
     *
     * @param task the task
     */
    public synchronized void add(Runnable task) {
	  if (!closing) {
		try {
		    System.out.println("Adding " + task + " to queue...");
		    WrappedTask nextTask = new WrappedTask();
		    nextTask.myTask = task;
		    this.taskQueue.put(nextTask);
		} catch (InterruptedException ex) {
		    ex.printStackTrace();
		}
	  } else {
		throw new RuntimeException("Cannot add tasks to a stopped manager!");
	  }
    }

    @Override    
    public void run() {
	  while (true) {
		try {
		    System.out.println("Reading next task. Queue size: " + this.taskQueue.size() + "...");
		    WrappedTask next = this.taskQueue.take(); // if queue empty, this blocks
		    if (next.isEnd) {
			  break;
		    }
		    System.out.println("Executing task: " + next.myTask + ". Queue size: " + this.taskQueue.size() + ".");
		    next.myTask.run();
		} catch (InterruptedException ex) {
		    ex.printStackTrace();
		}
	  }
	  System.out.println("Task Manager's thread finished!");
    }

}