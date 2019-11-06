package br.sky.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Andre Lopes
 */
public class Executor {

    private static final ExecutorService executor;

    static {
        executor = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            //A daemon thread is a thread that does not prevent the JVM from exiting 
            //when the program finishes but the thread is still running.
            t.setDaemon(true);

            return t;
        });

    }

    public static ExecutorService getExecutorService() {
        return executor;
    }

    public static void dispose() {
        try {
            executor.shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
