package myPool;

import java.util.concurrent.*;

public class PoolClass {
    private ExecutorService pool;

    public PoolClass(int dim) {
       pool =Executors.newFixedThreadPool(dim);
    }

    public PoolClass(){
        this(5);
    }

    public <T> Future<T> submit(Callable<T> callable){
        return pool.submit(callable);
    }

    public void StopPool(){
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Killing the bitch
                // Wait a while for the bitch
                if (!pool.awaitTermination(10, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        finally {
            pool.shutdownNow();
        }
    }
}
