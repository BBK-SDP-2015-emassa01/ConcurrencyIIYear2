/**
 * A manager of tasks. 
 *
 * Instances of classes implementing this interface keep a queue of
 * tasks and execute them as soon as possible.
 *
 * If the tasks is empty, they wait for new tasks. 
 */
public interface TaskManager extends Runnable {
    /** 
     * Tells this task manager to finish executing any 
     * tasks already in queues and then stop. 
     */
    public void shutdown();

    /** 
     * Adds a task to the queue of tasks. 
     *
     * @param task the task
     */
    public void add(Runnable task);

}