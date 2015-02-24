import java.util.concurrent.Executor;

/**
 * An implementation of {@see SimpleExecutor}.
 */
public class SimpleExecutorImpl implements SimpleExecutor {
    private TaskManager manager;

    /** 
     * Creates a new executor.
     *
     * @param manager the task manager to use internally --- dependency injection!
     */
    public SimpleExecutorImpl(TaskManager manager) {
	  this.manager = manager;         // No need to write: this.manager = new XxxManager(); !!
	  Thread t = new Thread(manager); // Code easier to test!!
	  t.start();
    }

    @Override
    public void execute(Runnable task) {
	  this.manager.add(task);
    }

    @Override
    public void shutdown() {
	  this.manager.shutdown();
    }
}