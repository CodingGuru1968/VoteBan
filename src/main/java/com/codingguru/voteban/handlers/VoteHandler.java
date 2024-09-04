package com.codingguru.voteban.handlers;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.obj.VotedDetails;
import com.codingguru.voteban.scheduler.StartVoteTask;
import com.codingguru.voteban.scheduler.VoteType;
import com.google.common.collect.Maps;

public class VoteHandler {

	private final static VoteHandler INSTANCE = new VoteHandler();
	private boolean isChatDisabled;
	private Map<UUID, VotedDetails> alreadyVotedPlayers;
	private StartVoteTask activeVote;

	public VoteHandler() {
		this.isChatDisabled = false;
		this.alreadyVotedPlayers = Maps.newHashMap();
	}

	public void setActiveVote(StartVoteTask activeVote) {
		this.activeVote = activeVote;
	}

	public StartVoteTask getActiveVote() {
		return activeVote;
	}

	public boolean hasActiveVote() {
		return activeVote != null;
	}

	public void filterAlreadyVotedPlayers() {
		for (Iterator<UUID> iter = alreadyVotedPlayers.keySet().iterator(); iter.hasNext();) {
			UUID uuid = iter.next();
			VotedDetails details = alreadyVotedPlayers.get(uuid);
			if (System.currentTimeMillis() >= details.getEndTime()) {
				iter.remove();
			}
		}
	}

	public boolean isVoteAllowed(VoteType voteType, UUID uuid) {
		if (!VoteBan.getInstance().getConfig().getBoolean("already-voted.enabled"))
			return true;

		if (alreadyVotedPlayers.containsKey(uuid)) {
			VotedDetails votedDetails = alreadyVotedPlayers.get(uuid);

			if (System.currentTimeMillis() >= votedDetails.getEndTime()) {
				alreadyVotedPlayers.remove(uuid);
				return true;
			}

			if (!VoteBan.getInstance().getConfig().getBoolean("already-voted.allow-different-votes"))
				return false;

			if (voteType == votedDetails.getVoteType())
				return false;
		}

		return true;
	}

	public void removeAlreadyVotedPlayer(UUID uuid) {
		this.alreadyVotedPlayers.remove(uuid);
	}

	public void addAlreadyVotedPlayer(UUID uuid, VoteType type) {
		if (!VoteBan.getInstance().getConfig().getBoolean("already-voted.enabled"))
			return;

		long timeAdded = VoteBan.getInstance().getConfig().getInt("already-voted.length") * 1000;
		long endTime = System.currentTimeMillis() + timeAdded;
		VotedDetails votedDetails = new VotedDetails(endTime, type);
		this.alreadyVotedPlayers.put(uuid, votedDetails);
	}

	public void setChatDisabled(boolean isChatDisabled) {
		this.isChatDisabled = isChatDisabled;
	}

	public boolean isChatDisabled() {
		return isChatDisabled;
	}

	public static VoteHandler getInstance() {
		return INSTANCE;
	}

}