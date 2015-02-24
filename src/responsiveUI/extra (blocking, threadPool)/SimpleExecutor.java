import java.util.concurrent.Executor;

/**
 * An executor that can be shut down.
 */
public interface SimpleExecutor extends Executor {
    /**
     * Calling this method will stop the executor. 
     *
     * If the executor still has pending tasks in one or 
     * more queues, those tasks will be completed before
     * the executor is finished. 
     */
    public void shutdown();
}