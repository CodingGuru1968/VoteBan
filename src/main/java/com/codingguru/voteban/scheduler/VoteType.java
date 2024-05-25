package com.codingguru.voteban.scheduler;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.utils.ConsoleUtil;
import com.codingguru.voteban.utils.MessagesUtil;

public enum VoteType {

	BAN(
			"banned", MessagesUtil.SUCCESSFUL_VOTE_BAN_BROADCAST.toString(),
			MessagesUtil.FAILED_VOTE_BAN_BROADCAST.toString(), MessagesUtil.VOTE_BAN_BROADCAST.toString(),
			VoteBan.getInstance().getConfig().getInt("VOTE_BAN.COUNTDOWN"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_BAN.INSTANT"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_BAN.ANNOUNCE_VOTES"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_BAN.STOP_CHAT.ENABLED"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_BAN.STOP_CHAT.REQUIRES_PERMISSION"),
			VoteBan.getInstance().getConfig().getIntegerList("VOTE_BAN.BROADCAST_TIMES")),
	MUTE(
			"muted", MessagesUtil.SUCCESSFUL_VOTE_MUTE_BROADCAST.toString(),
			MessagesUtil.FAILED_VOTE_MUTE_BROADCAST.toString(), MessagesUtil.VOTE_MUTE_BROADCAST.toString(),
			VoteBan.getInstance().getConfig().getInt("VOTE_MUTE.COUNTDOWN"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_MUTE.INSTANT"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_MUTE.ANNOUNCE_VOTES"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_MUTE.STOP_CHAT.ENABLED"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_MUTE.STOP_CHAT.REQUIRES_PERMISSION"),
			VoteBan.getInstance().getConfig().getIntegerList("VOTE_MUTE.BROADCAST_TIMES")),
	KICK(
			"kicked", MessagesUtil.SUCCESSFUL_VOTE_KICK_BROADCAST.toString(),
			MessagesUtil.FAILED_VOTE_KICK_BROADCAST.toString(), MessagesUtil.VOTE_KICK_BROADCAST.toString(),
			VoteBan.getInstance().getConfig().getInt("VOTE_KICK.COUNTDOWN"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_KICK.INSTANT"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_KICK.ANNOUNCE_VOTES"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_KICK.STOP_CHAT.ENABLED"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_KICK.STOP_CHAT.REQUIRES_PERMISSION"),
			VoteBan.getInstance().getConfig().getIntegerList("VOTE_KICK.BROADCAST_TIMES"));

	private final String niceName;
	private final String successfulVoteMessage;
	private final String failedVoteMessage;
	private final String broadcastMessage;
	private final VoteResultCalculatorType voteCalculatorType;
	private final List<Integer> announcementTimes;
	private final int minVotes;
	private final int countdown;
	private final boolean isInstant;
	private final boolean announceVotes;
	private final boolean stopChat;
	private final boolean stopChatRequiresPermission;

	VoteType(String niceName, String successfulVoteMessage, String failedVoteMessage, String broadcastMessage,
			int countdown, boolean isInstant, boolean announceVotes, boolean stopChat,
			boolean stopChatRequiresPermission, List<Integer> announcementTimes) {
		this.niceName = niceName;
		this.successfulVoteMessage = successfulVoteMessage;
		this.failedVoteMessage = failedVoteMessage;
		this.broadcastMessage = broadcastMessage;
		this.countdown = countdown;
		this.isInstant = isInstant;
		this.announceVotes = announceVotes;
		this.stopChat = stopChat;
		this.stopChatRequiresPermission = stopChatRequiresPermission;
		this.voteCalculatorType = getVoteCalculatorType(name());
		this.minVotes = getMinVotes(name());
		this.announcementTimes = announcementTimes;
	}

	private int getMinVotes(String name) {
		if (VoteBan.getInstance().getConfig().isSet("VOTE_" + name + ".VOTES." + voteCalculatorType.name())) {
			return VoteBan.getInstance().getConfig().getInt("VOTE_" + name + ".VOTES." + voteCalculatorType.name());
		} else if (VoteBan.getInstance().getConfig().isSet("VOTE_" + name + ".MIN_VOTES")) {
			return VoteBan.getInstance().getConfig().getInt("VOTE_" + name + ".MIN_VOTES");
		}
		ConsoleUtil.warning(
				"Could not find an amount of votes needed to execute command in the config. Defaulting to 10...");
		return 10;
	}

	private VoteResultCalculatorType getVoteCalculatorType(String name) {
		if (VoteBan.getInstance().getConfig().isSet("VOTE_" + name + ".VOTES")) {
			return VoteResultCalculatorType
					.getVoteCalculatorType(VoteBan.getInstance().getConfig().getString("VOTE_" + name + ".VOTES.TYPE"));
		}
		ConsoleUtil.warning("Could not find a vote type calculation type in the config. Defaulting to MIN_VOTES...");
		return VoteResultCalculatorType.MIN_VOTES;
	}

	public VoteResultCalculatorType getVoteCalculatorType() {
		return voteCalculatorType;
	}

	public String getBroadcastMessage() {
		return broadcastMessage;
	}

	public String getSuccessfulVoteMessage() {
		return successfulVoteMessage;
	}

	public String getFailedVoteMessage() {
		return failedVoteMessage;
	}

	public int getMinVotes() {
		if (minVotes == 0)
			return 10;
		return minVotes;
	}

	public int getCountdown() {
		if (countdown == 0)
			return 30;
		return countdown;
	}

	public boolean isAnnouncingVotes() {
		return announceVotes;
	}

	public boolean isInstant() {
		return isInstant;
	}

	public boolean isStoppingChat() {
		return stopChat;
	}

	public boolean stopChatRequiresPermission() {
		return stopChatRequiresPermission;
	}

	public List<Integer> getAnnouncementTimes() {
		return announcementTimes;
	}

	public void execute(UUID playerUUID, String playerName, String reason) {
		switch (this) {
		case BAN:
			String banCommand = VoteBan.getInstance().getConfig().getString("VOTE_BAN.BAN_CMD")
					.replaceAll("%player%", playerName).replaceAll("%reason%", reason);
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), banCommand);
			break;
		case KICK:
			Player player = Bukkit.getPlayer(playerUUID);

			if (player == null)
				return;

			player.kickPlayer(MessagesUtil.KICK_MESSAGE.toString().replaceAll("%reason%", reason));
			break;
		case MUTE:
			String muteCommand = VoteBan.getInstance().getConfig().getString("VOTE_MUTE.MUTE_CMD")
					.replaceAll("%player%", playerName).replaceAll("%reason%", reason);
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), muteCommand);
			break;
		}
	}

	@Override
	public String toString() {
		return niceName;
	}
}