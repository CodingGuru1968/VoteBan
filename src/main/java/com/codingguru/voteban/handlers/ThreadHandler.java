package com.codingguru.voteban.handlers;

import com.codingguru.voteban.scheduler.StartVoteTask;

public class ThreadHandler {

	private final static ThreadHandler INSTANCE = new ThreadHandler();
	private StartVoteTask activeVote;

	public void setActiveVote(StartVoteTask activeVote) {
		this.activeVote = activeVote;
	}
	
	public StartVoteTask getActiveVote() {
		return activeVote;
	}

	public boolean hasActiveVote() {
		return activeVote != null;
	}

	public static ThreadHandler getInstance() {
		return INSTANCE;
	}
}
