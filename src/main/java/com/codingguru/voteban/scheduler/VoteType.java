package com.codingguru.voteban.scheduler;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.utils.LangUtil;

public enum VoteType {

	BAN(
			"banned", LangUtil.SUCCESSFUL_VOTE_BAN_BROADCAST.toString(), LangUtil.FAILED_VOTE_BAN_BROADCAST.toString(),
			LangUtil.VOTE_BAN_BROADCAST.toString(), VoteBan.getInstance().getConfig().getInt("VOTE_BAN.MIN_VOTES"),
			VoteBan.getInstance().getConfig().getInt("VOTE_BAN.COUNTDOWN"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_BAN.INSTANT"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_BAN.ANNOUNCE_VOTES"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_BAN.STOP_CHAT.ENABLED"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_BAN.STOP_CHAT.REQUIRES_PERMISSION"),
			VoteBan.getInstance().getConfig().getIntegerList("VOTE_BAN.BROADCAST_TIMES")),
	MUTE(
			"muted", LangUtil.SUCCESSFUL_VOTE_MUTE_BROADCAST.toString(), LangUtil.FAILED_VOTE_MUTE_BROADCAST.toString(),
			LangUtil.VOTE_MUTE_BROADCAST.toString(), VoteBan.getInstance().getConfig().getInt("VOTE_MUTE.MIN_VOTES"),
			VoteBan.getInstance().getConfig().getInt("VOTE_MUTE.COUNTDOWN"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_MUTE.INSTANT"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_MUTE.ANNOUNCE_VOTES"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_MUTE.STOP_CHAT.ENABLED"),
			VoteBan.getInstance().getConfig().getBoolean("VOTE_MUTE.STOP_CHAT.REQUIRES_PERMISSION"),
			VoteBan.getInstance().getConfig().getIntegerList("VOTE_MUTE.BROADCAST_TIMES")),
	KICK(
			"kicked", LangUtil.SUCCESSFUL_VOTE_KICK_BROADCAST.toString(),
			LangUtil.FAILED_VOTE_KICK_BROADCAST.toString(), LangUtil.VOTE_KICK_BROADCAST.toString(),
			VoteBan.getInstance().getConfig().getInt("VOTE_KICK.MIN_VOTES")
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
	private final List<Integer> announcementTimes;
	private final int minVotes;
	private final int countdown;
	private final boolean isInstant;
	private final boolean announceVotes;
	private final boolean stopChat;
	private final boolean stopChatRequiresPermission;

	VoteType(String niceName, String successfulVoteMessage, String failedVoteMessage, String broadcastMessage,
			int minVotes, int countdown, boolean isInstant, boolean announceVotes, boolean stopChat,
			boolean stopChatRequiresPermission, List<Integer> announcementTimes) {
		this.niceName = niceName;
		this.successfulVoteMessage = successfulVoteMessage;
		this.failedVoteMessage = failedVoteMessage;
		this.broadcastMessage = broadcastMessage;
		this.minVotes = minVotes;
		this.countdown = countdown;
		this.isInstant = isInstant;
		this.announceVotes = announceVotes;
		this.stopChat = stopChat;
		this.stopChatRequiresPermission = stopChatRequiresPermission;
		this.announcementTimes = announcementTimes;
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

			player.kickPlayer(LangUtil.KICK_MESSAGE.toString().replaceAll("%reason%", reason));
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