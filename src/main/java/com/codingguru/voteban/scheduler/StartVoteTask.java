package com.codingguru.voteban.scheduler;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.handlers.VoteHandler;
import com.codingguru.voteban.util.LangDefaults;
import com.codingguru.voteban.util.MessageBuilder;
import com.google.common.collect.Lists;

public class StartVoteTask extends Schedule {

	private final UUID playerUUID;
	private final String playerName;
	private final String reason;
	private final VoteType voteType;
	private List<UUID> playersVoted;
	private int playersOnline;
	private int countdown;

	public StartVoteTask(VoteBan plugin, Player target, Player sender, String reason, VoteType voteType,
			boolean addVote) {
		super(plugin);
		VoteHandler.getInstance().setActiveVote(this);
		this.reason = reason == null
				? new MessageBuilder.Builder("no-ban-reason", LangDefaults.NO_BAN_REASON).buildString()
				: reason;
		this.playerUUID = target.getUniqueId();
		this.playerName = target.getName();
		this.voteType = voteType;
		this.playersVoted = Lists.newArrayList();
		this.countdown = voteType.getCountdown();
		this.playersOnline = Bukkit.getOnlinePlayers().size();
		VoteHandler.getInstance().addAlreadyVotedPlayer(playerUUID, voteType);

		if (voteType.isStoppingChat()) {
			if (!voteType.stopChatRequiresPermission()
					|| (sender.hasPermission("voteban.*") || sender.hasPermission("voteban.stopchat"))) {
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
			new MessageBuilder.Builder(voteType.getBroadcastMessagePath(), getDefaultBroadcastMessage())
					.set("%player%", playerName).set("%timeleft%", String.valueOf(countdown)).set("%reason%", reason)
					.broadcast();
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
			new MessageBuilder.Builder(voteType.getSuccessfulMessagePath(), getDefaultSuccessfulMessage())
					.set("%player%", playerName).set("%timeleft%", String.valueOf(countdown)).set("%reason%", reason)
					.set("%votes%", String.valueOf(getVotes())).broadcast();
		} else {
			new MessageBuilder.Builder(voteType.getFailedMessagePath(), getDefaultFailedMessage())
					.set("%player%", playerName).set("%timeleft%", String.valueOf(countdown)).set("%reason%", reason)
					.set("%votes%", String.valueOf(getVotes())).broadcast();
		}

		cancel();
	}

	private boolean isBroadcastingTime(int time) {
		return voteType.getAnnouncementTimes().contains(time);
	}

	private boolean isInstant() {
		return voteType.isInstant();
	}

	private String getDefaultBroadcastMessage() {
		switch (voteType) {
		case BAN:
			return LangDefaults.VOTE_BAN_BROADCAST;
		case MUTE:
			return LangDefaults.VOTE_MUTE_BROADCAST;
		case KICK:
		default:
			return LangDefaults.VOTE_KICK_BROADCAST;
		}
	}

	private String getDefaultSuccessfulMessage() {
		switch (voteType) {
		case BAN:
			return LangDefaults.SUCCESSFUL_VOTE_BAN_BROADCAST;
		case MUTE:
			return LangDefaults.SUCCESSFUL_VOTE_MUTE_BROADCAST;
		case KICK:
		default:
			return LangDefaults.SUCCESSFUL_VOTE_KICK_BROADCAST;
		}
	}

	private String getDefaultFailedMessage() {
		switch (voteType) {
		case BAN:
			return LangDefaults.FAILED_VOTE_BAN_BROADCAST;
		case MUTE:
			return LangDefaults.FAILED_VOTE_MUTE_BROADCAST;
		case KICK:
		default:
			return LangDefaults.FAILED_VOTE_KICK_BROADCAST;
		}
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

		new MessageBuilder.Builder("successful-vote", LangDefaults.SUCCESSFUL_VOTE).set("%player%", playerName)
				.send(sender);

		if (voteType.isAnnouncingVotes()) {
			new MessageBuilder.Builder("vote-added", LangDefaults.VOTE_ADDED).set("%votedplayer%", sender.getName())
					.set("%target%", playerName).set("%type%", voteType.toString()).broadcast();
		}

		if (getVotes() >= getMinimumRequiredVotes() && isInstant()) {
			completeTask();
		}
	}
}