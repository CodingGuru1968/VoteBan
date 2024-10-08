package com.codingguru.voteban.scheduler;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.codingguru.voteban.handlers.VoteHandler;
import com.codingguru.voteban.utils.MessagesUtil;
import com.google.common.collect.Lists;

public class StartVoteTask extends BukkitRunnable {

	private final UUID playerUUID;
	private final String playerName;
	private final String reason;
	private final VoteType voteType;
	private List<UUID> playersVoted;
	private int playersOnline;
	private int countdown;

	public StartVoteTask(Player target, Player sender, String reason, VoteType voteType, boolean addVote) {
		VoteHandler.getInstance().setActiveVote(this);
		this.reason = reason == null ? MessagesUtil.NO_BAN_REASON.toString() : reason;
		this.playerUUID = target.getUniqueId();
		this.playerName = target.getName();
		this.voteType = voteType;
		this.playersVoted = Lists.newArrayList();
		this.countdown = voteType.getCountdown();
		this.playersOnline = Bukkit.getOnlinePlayers().size();
		VoteHandler.getInstance().addAlreadyVotedPlayer(playerUUID, voteType);

		if (voteType.isStoppingChat()) {
			if (!voteType.stopChatRequiresPermission()
					|| (sender.hasPermission("VOTEBAN.*") || sender.hasPermission("VOTEBAN.STOPCHAT"))) {
				VoteHandler.getInstance().setChatDisabled(true);
			}
		}
		
		if (addVote) {
			addVote(sender);
		}
	}

	@Override
	public void run() {
		if (isBroadcastingTime(countdown)) {
			Bukkit.broadcastMessage(voteType.getBroadcastMessage().replaceAll("%player%", playerName)
					.replaceAll("%timeleft%", countdown + "").replaceAll("%reason%", reason));
		}

		if (countdown == 0) {
			completeTask();
			return;
		}

		countdown--;
	}

	private void completeTask() {
		VoteHandler.getInstance().setActiveVote(null);
		VoteHandler.getInstance().setChatDisabled(false);

		boolean isSuccessful = voteType.getVoteCalculatorType().isSuccessful(voteType, getVotes(), playersOnline);

		if (isSuccessful) {
			voteType.execute(playerUUID, playerName, reason);
			Bukkit.broadcastMessage(getSuccessfulVoteMessage());
		} else {
			Bukkit.broadcastMessage(getFailedVoteMessage());
		}

		cancel();
	}

	private boolean isBroadcastingTime(int time) {
		return voteType.getAnnouncementTimes().contains(time);
	}

	private boolean isInstant() {
		return voteType.isInstant();
	}

	private String getSuccessfulVoteMessage() {
		return voteType.getSuccessfulVoteMessage().replaceAll("%player%", playerName)
				.replaceAll("%timeleft%", countdown + "").replaceAll("%reason%", reason)
				.replaceAll("%votes%", getVotes() + "");
	}

	private String getFailedVoteMessage() {
		return voteType.getFailedVoteMessage().replaceAll("%player%", playerName)
				.replaceAll("%timeleft%", countdown + "").replaceAll("%reason%", reason)
				.replaceAll("%votes%", getVotes() + "");
	}

	private int getMinimumRequiredVotes() {
		return voteType.getMinVotes();
	}

	private int getVotes() {
		return playersVoted.size();
	}

	public boolean canVote(Player sender) {
		return !playersVoted.contains(sender.getUniqueId());
	}

	public void addVote(Player sender) {
		playersVoted.add(sender.getUniqueId());

		MessagesUtil.sendMessage(sender, MessagesUtil.SUCCESSFUL_VOTE.toString().replaceAll("%player%", playerName));

		if (voteType.isAnnouncingVotes()) {
			Bukkit.broadcastMessage(MessagesUtil.VOTE_ADDED.toString().replaceAll("%votedplayer%", sender.getName())
					.replaceAll("%target%", playerName).replaceAll("%type%", voteType.toString()));
		}

		if (getVotes() >= getMinimumRequiredVotes() && isInstant()) {
			completeTask();
		}
	}
}