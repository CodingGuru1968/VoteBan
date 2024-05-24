package com.codingguru.voteban.utils;

import org.bukkit.command.CommandSender;

import com.codingguru.voteban.VoteBan;

public enum MessagesUtil {

	PREFIX("&8[&cVoteBan&8] ", false),
	RELOAD("&aYou have successfully reloaded all configuration files.", true),

	FAILED_VOTE_KICK_BROADCAST(
			"&a%player% &edid not receive enough votes and will not be kicked from the server.", true),
	FAILED_VOTE_BAN_BROADCAST("&a%player% &edid not receive enough votes and will remain playing this server.", true),
	FAILED_VOTE_MUTE_BROADCAST("&a%player% &edid not receive enough votes and will remain unmuted.", true),

	SUCCESSFUL_VOTE_KICK_BROADCAST("&c&l%player% has been voted to be kicked from the server. Reason: %reason%", true),
	SUCCESSFUL_VOTE_BAN_BROADCAST("&c&l%player% has been voted to be banned from the server. Reason: %reason%", true),
	SUCCESSFUL_VOTE_MUTE_BROADCAST("&c&l%player% has been voted to be muted from the chat. Reason: %reason%", true),

	VOTE_KICK_BROADCAST(
			"&eThere is currently a vote kick in progress for &a%player%&e. If you agree this player should be kicked, type &a/addvote&e. Reason: &a%reason%",
			true),
	VOTE_BAN_BROADCAST(
			"&eThere is currently a vote ban in progress for &a%player%&e. If you agree this player should be banned, type &a/addvote&e. Reason: &a%reason%",
			true),
	VOTE_MUTE_BROADCAST(
			"&eThere is currently a vote mute in progress for &a%player%&e. If you agree this player should be muted, type &a/addvote&e. Reason: &a%reason%",
			true),

	SUCCESSFUL_VOTE("&eYou have successfully added your vote for %player%.", true),

	VOTE_ADDED("&e%votedplayer% has voted for %target% to be %type%.", true),

	KICK_MESSAGE("&cYou have been voted to be kicked by players of the server!\nKick Reason: %reason%", false),

	CHAT_DISABLED("&cChat is currently disabled because there is a vote in progress.", true),
	ALREADY_VOTED("&cYou have already voted for this.", true),
	SERVER_OPERATOR(
			"&cYou cannot ban another server operator. Please demote this player first then execute this command again.",
			true),
	CANNOT_EXECUTE_THIS_PLAYER("&cYou cannot start a vote command on: %player%", true),
	CANNOT_EXECUTE_YOURSELF("&cYou cannot execute this command on yourself.", true),
	NOT_ACTIVE_VOTE("&cThere is currently not a vote active.", true),
	ACTIVE_VOTE("&cThere is already an active vote. Please wait for this to end before creating a new one.", true),
	INCORRECT_USAGE("&cCorrect Usage: %command%", true),
	INCORRECT_VALUE("&cYou have entered an incorrect value for the length.", true),
	INCORRECT_UNIT(
			"&cYou have entered an incorrect value for the unit. Please enter: second, minute, hour, day, month", true),
	NOT_BANNED("&cThis player has not been banned from this server.", true),
	PLAYER_NOT_FOUND("&c%player% is not currently online.", true),
	IN_GAME_ONLY("&cThis command can only be executed in game.", true),
	NO_PERMISSION("&cYou do not have permission to execute this command.", true),
	ALREADY_BANNED("&cThis player is already permanently banned from this server.", true),
	NOT_ENABLED("&cThis command is not currently enabled.", true),
	NO_BAN_REASON("Not Specified", true);

	private String defaultValue;
	private boolean usePrefix;

	MessagesUtil(String defaultValue, boolean usePrefix) {
		this.defaultValue = defaultValue;
		this.usePrefix = usePrefix;
	}

	public String getDefault() {
		return this.defaultValue;
	}

	public String getPath() {
		return this.name();
	}

	public boolean usePrefix() {
		return this.usePrefix;
	}

	@Override
	public String toString() {
		boolean usePrefix = false;
		String prefix = null;
		String message;

		if (VoteBan.getInstance().getConfig().getBoolean("USE_PREFIX") && this.usePrefix()) {
			usePrefix = true;
			prefix = ColorUtil.replace(VoteBan.getInstance().getSettingsManager().getLang()
					.getString(MessagesUtil.PREFIX.getPath(), "&8[&cVoteBan&8] "));
		}

		if (VoteBan.getInstance().getSettingsManager().getLang().isSet(this.getPath())) {
			message = ColorUtil
					.replace(VoteBan.getInstance().getSettingsManager().getLang().getString(this.getPath()));
		} else {
			message = ColorUtil.replace(defaultValue);
		}

		return usePrefix ? prefix + message : message;
	}

	public static void sendMessage(CommandSender sender, String replacedString) {
		if (replacedString.equalsIgnoreCase(""))
			return;

		String[] message = replacedString.split("\\\\n");

		for (String msg : message) {
			sender.sendMessage(msg.replace("\\n", ""));
		}
	}
}