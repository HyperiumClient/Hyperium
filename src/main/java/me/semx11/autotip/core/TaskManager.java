package me.semx11.autotip.core;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import me.semx11.autotip.util.ErrorReport;

public class TaskManager {

    private final ExecutorService executor;
    private final ScheduledExecutorService scheduler;

    private final Map<TaskType, Future> tasks;

    public TaskManager() {
        this.executor = Executors.newCachedThreadPool(this.getFactory("AutotipThread"));
        this.scheduler = Executors.newScheduledThreadPool(3, this.getFactory("AutotipScheduler"));
        this.tasks = new ConcurrentHashMap<>();
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void schedule(Runnable runnable, long delay) {
        try {
            scheduler.schedule(runnable, delay, SECONDS).get();
        } catch (InterruptedException | ExecutionException e) {
            ErrorReport.reportException(e);
        }
    }

    public <T> T scheduleAndAwait(Callable<T> callable, long delay) {
        try {
            return scheduler.schedule(callable, delay, SECONDS).get();
        } catch (InterruptedException | ExecutionException e) {
            ErrorReport.reportException(e);
            return null;
        }
    }

    public void executeTask(TaskType type, Runnable task) {
        if (tasks.containsKey(type)) {
            return;
        }
        Future<?> future = executor.submit(task);
        tasks.put(type, future);
        this.catchFutureException(type, future);
    }

    public void addRepeatingTask(TaskType type, Runnable command, long delay, long period) {
        if (tasks.containsKey(type)) {
            return;
        }
        ScheduledFuture future = scheduler.scheduleAtFixedRate(command, delay, period, SECONDS);
        tasks.put(type, future);
        this.catchFutureException(type, future);
    }

    public void cancelTask(TaskType type) {
        if (tasks.containsKey(type)) {
            tasks.get(type).cancel(true);
            tasks.remove(type);
        }
    }

    private void catchFutureException(TaskType type, Future future) {
        this.executor.execute(() -> {
            try {
                future.get();
            } catch (CancellationException ignored) {
                // Manual cancellation of a repeating task.
            } catch (InterruptedException | ExecutionException e) {
                ErrorReport.reportException(e);
            } finally {
                tasks.remove(type);
            }
        });
    }

    private ThreadFactory getFactory(String name) {
        return new ThreadFactoryBuilder()
                .setNameFormat(name)
                .setUncaughtExceptionHandler((t, e) -> ErrorReport.reportException(e))
                .build();
    }

    public enum TaskType {
        LOGIN, KEEP_ALIVE, TIP_WAVE, TIP_CYCLE, LOGOUT
    }

}
