package com.codingguru.voteban.scheduler;

import org.bukkit.scheduler.BukkitRunnable;

import com.codingguru.voteban.handlers.VoteHandler;

public class FilterTask extends BukkitRunnable {

	@Override
	public void run() {
		VoteHandler.getInstance().filterAlreadyVotedPlayers();
	}
}