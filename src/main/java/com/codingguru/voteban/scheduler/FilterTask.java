package com.codingguru.voteban.scheduler;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.handlers.VoteHandler;

public class FilterTask extends Schedule {

	public FilterTask(VoteBan plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		VoteHandler.getInstance().filterAlreadyVotedPlayers();
	}
}