package com.codingguru.voteban.scheduler;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.util.ConsoleUtil;
import com.codingguru.voteban.util.LangDefaults;
import com.codingguru.voteban.util.MessageBuilder;

public enum VoteType {

	BAN("banned", "successful-vote-ban-broadcast", "failed-vote-ban-broadcast", "vote-ban-broadcast"),
	MUTE("muted", "successful-vote-mute-broadcast", "failed-vote-mute-broadcast", "vote-mute-broadcast"),
	KICK("kicked", "successful-vote-kick-broadcast", "failed-vote-kick-broadcast", "vote-kick-broadcast");

	private final String niceName;
	private final String successfulMessagePath;
	private final String failedMessagePath;
	private final String broadcastMessagePath;

	private final VoteResultCalculatorType voteCalculatorType;
	private final List<Integer> announcementTimes;
	private final int minVotes;
	private final int countdown;
	private final boolean isInstant;
	private final boolean announceVotes;
	private final boolean stopChat;
	private final boolean stopChatRequiresPermission;

	VoteType(String niceName, String successfulMessagePath, String failedMessagePath, String broadcastMessagePath) {
		this.niceName = niceName;
		this.successfulMessagePath = successfulMessagePath;
		this.failedMessagePath = failedMessagePath;
		this.broadcastMessagePath = broadcastMessagePath;
		String configKey = name().toLowerCase();
		VoteBan plugin = VoteBan.getInstance();
		this.countdown = plugin.getConfig().getInt("vote-" + configKey + ".decision.countdown");
		this.isInstant = plugin.getConfig().getBoolean("vote-" + configKey + ".decision.instant");
		this.announceVotes = plugin.getConfig().getBoolean("vote-" + configKey + ".announce-votes");
		this.stopChat = plugin.getConfig().getBoolean("vote-" + configKey + ".stop-chat.enabled");
		this.stopChatRequiresPermission = plugin.getConfig()
				.getBoolean("vote-" + configKey + ".stop-chat.requires-permission");
		this.announcementTimes = plugin.getConfig().getIntegerList("vote-" + configKey + ".broadcast-times");
		this.voteCalculatorType = getVoteCalculatorType(configKey);
		this.minVotes = getMinVotes(configKey);
	}

	private int getMinVotes(String name) {
		VoteBan plugin = VoteBan.getInstance();
		if (plugin.getConfig().isSet("vote-" + name + ".votes." + voteCalculatorType.toString())) {
			return plugin.getConfig().getInt("vote-" + name + ".votes." + voteCalculatorType.toString());
		}
		ConsoleUtil.warning(
				"Could not find an amount of votes needed to execute command in the config. Defaulting to 10...");
		return 10;
	}

	private VoteResultCalculatorType getVoteCalculatorType(String name) {
		VoteBan plugin = VoteBan.getInstance();
		if (plugin.getConfig().isSet("vote-" + name + ".votes.type")) {
			return VoteResultCalculatorType
					.getVoteCalculatorType(plugin.getConfig().getString("vote-" + name + ".votes.type"));
		}
		ConsoleUtil.warning("Could not find a vote type calculation type in the config. Defaulting to MIN_VOTES...");
		return VoteResultCalculatorType.MIN_VOTES;
	}

	public VoteResultCalculatorType getVoteCalculatorType() {
		return voteCalculatorType;
	}

	public String getBroadcastMessagePath() {
		return broadcastMessagePath;
	}

	public String getSuccessfulMessagePath() {
		return successfulMessagePath;
	}

	public String getFailedMessagePath() {
		return failedMessagePath;
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

	@SuppressWarnings("deprecation")
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

			String kickMessage = new MessageBuilder.Builder("kick-message", LangDefaults.KICK_MESSAGE)
					.set("%reason%", reason).buildString();
			player.kickPlayer(kickMessage);
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