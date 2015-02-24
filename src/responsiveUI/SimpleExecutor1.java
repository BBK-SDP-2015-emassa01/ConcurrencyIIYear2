package responsiveUI;

import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * An implementation of {@see SimpleExecutor}.
 */
public class SimpleExecutor1 implements Executor{
    private TaskManager manager;

    /** 
     * Creates a new executor.
     */
    public SimpleExecutor1() {
	  this.manager = new TaskManager();
	  Thread t = new Thread(manager);
	  t.start();
    }

    @Override
    public void execute(Runnable task) {
	  this.manager.add(task);
    }

    public void shutdown() {
	  this.manager.shutdown();
    }
    
    public int getMaxPendingTime(){
    	int time = 0;
    	Queue<Runnable> q = this.manager.getTaskQueue();
    	for(Runnable r: q){
    		time = time + 1000;
    	}
		return time;
    	
    }
}
