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
			VoteBan.getInstance().getConfig().getInt("vote-ban.decision.countdown"),
			VoteBan.getInstance().getConfig().getBoolean("vote-ban.decision.instant"),
			VoteBan.getInstance().getConfig().getBoolean("vote-ban.announce-votes"),
			VoteBan.getInstance().getConfig().getBoolean("vote-ban.stop-chat.enabled"),
			VoteBan.getInstance().getConfig().getBoolean("vote-ban.stop-chat.requires-permission"),
			VoteBan.getInstance().getConfig().getIntegerList("vote-ban.broadcast-times")),
	MUTE(
			"muted", MessagesUtil.SUCCESSFUL_VOTE_MUTE_BROADCAST.toString(),
			MessagesUtil.FAILED_VOTE_MUTE_BROADCAST.toString(), MessagesUtil.VOTE_MUTE_BROADCAST.toString(),
			VoteBan.getInstance().getConfig().getInt("vote-mute.decision.countdown"),
			VoteBan.getInstance().getConfig().getBoolean("vote-mute.decision.instant"),
			VoteBan.getInstance().getConfig().getBoolean("vote-mute.announce-votes"),
			VoteBan.getInstance().getConfig().getBoolean("vote-mute.stop-chat.enabled"),
			VoteBan.getInstance().getConfig().getBoolean("vote-mute.stop-chat.requires-permission"),
			VoteBan.getInstance().getConfig().getIntegerList("vote-mute.broadcast-times")),
	KICK(
			"kicked", MessagesUtil.SUCCESSFUL_VOTE_KICK_BROADCAST.toString(),
			MessagesUtil.FAILED_VOTE_KICK_BROADCAST.toString(), MessagesUtil.VOTE_KICK_BROADCAST.toString(),
			VoteBan.getInstance().getConfig().getInt("vote-kick.decision.countdown"),
			VoteBan.getInstance().getConfig().getBoolean("vote-kick.decision.instant"),
			VoteBan.getInstance().getConfig().getBoolean("vote-kick.announce-votes"),
			VoteBan.getInstance().getConfig().getBoolean("vote-kick.stop-chat.enabled"),
			VoteBan.getInstance().getConfig().getBoolean("vote-kick.stop-chat.requires-permission"),
			VoteBan.getInstance().getConfig().getIntegerList("vote-kick.broadcast-times"));

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
		this.voteCalculatorType = getVoteCalculatorType(name().toLowerCase());
		this.minVotes = getMinVotes(name().toLowerCase());
		this.announcementTimes = announcementTimes;
	}

	private int getMinVotes(String name) {
		if (VoteBan.getInstance().getConfig().isSet("vote-" + name + ".votes." + voteCalculatorType.toString())) {
			return VoteBan.getInstance().getConfig().getInt("vote-" + name + ".votes." + voteCalculatorType.toString());
		}
		ConsoleUtil.warning(
				"Could not find an amount of votes needed to execute command in the config. Defaulting to 10...");
		return 10;
	}

	private VoteResultCalculatorType getVoteCalculatorType(String name) {
		if (VoteBan.getInstance().getConfig().isSet("vote-" + name + ".votes.type")) {
			return VoteResultCalculatorType
					.getVoteCalculatorType(VoteBan.getInstance().getConfig().getString("vote-" + name + ".votes.type"));
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
			String banCommand = VoteBan.getInstance().getConfig().getString("vote-ban.ban-cmd")
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
			String muteCommand = VoteBan.getInstance().getConfig().getString("vote-mute.mute-cmd")
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