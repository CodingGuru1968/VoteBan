package com.codingguru.voteban.scheduler;

import com.codingguru.voteban.handlers.VoteHandler;

public class FilterTask extends Schedule {

	@Override
	public void run() {
		VoteHandler.getInstance().filterAlreadyVotedPlayers();
	}
}