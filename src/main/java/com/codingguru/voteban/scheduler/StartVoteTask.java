package com.codingguru.voteban.scheduler;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.handlers.ThreadHandler;
import com.codingguru.voteban.handlers.VoteHandler;
import com.codingguru.voteban.utils.AsyncThreadUtil;
import com.codingguru.voteban.utils.MessagesUtil;
import com.google.common.collect.Lists;

public class StartVoteTask extends AsyncThreadUtil {

	private final UUID playerUUID;
	private final String playerName;
	private final String reason;
	private final VoteType voteType;
	private List<UUID> playersVoted;
	private int countdown;

	public StartVoteTask(Player player, String reason, VoteType voteType) {
		ThreadHandler.getInstance().setActiveVote(this);
		this.reason = reason == null ? MessagesUtil.NO_BAN_REASON.toString() : reason;
		this.playerUUID = player.getUniqueId();
		this.playerName = player.getName();
		this.voteType = voteType;
		this.playersVoted = Lists.newArrayList();
		this.countdown = voteType.getCountdown();

		if (voteType.isStoppingChat()) {
			if (!voteType.stopChatRequiresPermission()
					|| (player.hasPermission("VOTEBAN.*") || player.hasPermission("VOTEBAN.STOPCHAT"))) {
				VoteHandler.getInstance().setChatDisabled(true);
			}
		}
	}

	@Override
	public void runTask() {
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
		VoteHandler.getInstance().setChatDisabled(false);

		Bukkit.getScheduler().runTask(VoteBan.getInstance(), () -> {
			boolean isSuccessful = getVotes() >= getMinimumRequiredVotes();

			if (isSuccessful) {
				voteType.execute(playerUUID, playerName, reason);
				Bukkit.broadcastMessage(getSuccessfulVoteMessage());
			} else {
				Bukkit.broadcastMessage(getFailedVoteMessage());
			}
		});

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