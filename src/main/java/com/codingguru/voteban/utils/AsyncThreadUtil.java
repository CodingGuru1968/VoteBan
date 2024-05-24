package com.codingguru.voteban.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.codingguru.voteban.handlers.ThreadHandler;

public abstract class AsyncThreadUtil implements Runnable {

	private final ScheduledExecutorService executor;
	private ThreadType threadType;
	private ScheduledFuture<?> task;

	public AsyncThreadUtil() {
		this.executor = Executors.newSingleThreadScheduledExecutor();
	}

	@Override
	public void run() {
		runTask();
		if (getThreadType() != ThreadType.REPEATING) {
			this.cancel();
		}
	}

	public abstract void runTask();

	public void submitRepeatingScheduledTask(TimeUnit unit, int delay, int repeator) {
		task = executor.scheduleAtFixedRate(this, delay, repeator, unit);
		threadType = ThreadType.REPEATING;
	}

	public void submitRepeatingTask(TimeUnit unit, int delay) {
		task = executor.scheduleAtFixedRate(this, 0, delay, unit);
		threadType = ThreadType.REPEATING;
	}

	public void submitScheduledTask(TimeUnit unit, int delay) {
		task = executor.schedule(this, delay, unit);
		threadType = ThreadType.ONE_TIME;
	}

	public void submitTask() {
		executor.submit(this);
		threadType = ThreadType.ONE_TIME;
	}

	public ThreadType getThreadType() {
		return threadType;
	}

	public void cancel() {
		if (this.executor != null)
			this.executor.shutdownNow();
		if (this.task != null)
			this.task.cancel(true);
		ThreadHandler.getInstance().setActiveVote(null);
	}

}