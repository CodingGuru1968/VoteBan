package com.codingguru.voteban.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.codingguru.voteban.VoteBan;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum MessagesUtil {

	FAILED_VOTE_KICK_BROADCAST("&a%player% &edid not receive enough votes and will not be kicked from the server."),
	FAILED_VOTE_BAN_BROADCAST("&a%player% &edid not receive enough votes and will remain playing this server."),
	FAILED_VOTE_MUTE_BROADCAST("&a%player% &edid not receive enough votes and will remain unmuted."),
	SUCCESSFUL_VOTE_KICK_BROADCAST("&c&l%player% has been voted to be kicked from the server. Reason: %reason%"),
	SUCCESSFUL_VOTE_BAN_BROADCAST("&c&l%player% has been voted to be banned from the server. Reason: %reason%"),
	SUCCESSFUL_VOTE_MUTE_BROADCAST("&c&l%player% has been voted to be muted from the chat. Reason: %reason%"),
	VOTE_KICK_BROADCAST("&eThere is currently a vote kick in progress for &a%player%&e. If you agree this player should be kicked, type &a/addvote&e. Reason: &a%reason%"),
	VOTE_BAN_BROADCAST("&eThere is currently a vote ban in progress for &a%player%&e. If you agree this player should be banned, type &a/addvote&e. Reason: &a%reason%"),
	VOTE_MUTE_BROADCAST("&eThere is currently a vote mute in progress for &a%player%&e. If you agree this player should be muted, type &a/addvote&e. Reason: &a%reason%"),
	SUCCESSFUL_VOTE("&eYou have successfully added your vote for %player%."),
	VOTE_ADDED("&e%votedplayer% has voted for %target% to be %type%."),
	KICK_MESSAGE("&cYou have been voted to be kicked by players of the server!\nKick Reason: %reason%"),
	ALREADY_VOTED_FOR("&c%player% recently had a vote started on them so cannot be voted on again."),
	CHAT_DISABLED("&cChat is currently disabled because there is a vote in progress."),
	ALREADY_VOTED("&cYou have already voted for this."),
	SERVER_OPERATOR("&cYou cannot ban another server operator. Please demote this player first then execute this command again."),
	CANNOT_EXECUTE_THIS_PLAYER("&cYou cannot start a vote command on: %player%"),
	CANNOT_EXECUTE_YOURSELF("&cYou cannot execute this command on yourself."),
	NOT_ACTIVE_VOTE("&cThere is currently not a vote active."),
	ACTIVE_VOTE("&cThere is already an active vote. Please wait for this to end before creating a new one."),
	INCORRECT_USAGE("&cCorrect Usage: %command%"),
	INCORRECT_VALUE("&cYou have entered an incorrect value for the length."),
	INCORRECT_UNIT("&cYou have entered an incorrect value for the unit. Please enter: second, minute, hour, day, month"),
	NOT_BANNED("&cThis player has not been banned from this server."),
	PLAYER_NOT_FOUND("&c%player% is not currently online."),
	IN_GAME_ONLY("&cThis command can only be executed in game."),
	NO_PERMISSION("&cYou do not have permission to execute this command."),
	ALREADY_BANNED("&cThis player is already permanently banned from this server."),
	NOT_ENABLED("&cThis command is not currently enabled."),
	RELOAD("&aYou have successfully reloaded all configuration files."),
	NO_BAN_REASON("Not Specified");

	private String defaultValue;

	MessagesUtil(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefault() {
		return this.defaultValue;
	}

	public String getPath() {
		return this.name();
	}

	@Override
	public String toString() {
		String message;

		if (VoteBan.getInstance().getSettingsManager().getLang().isSet(this.getPath())) {
			message = VoteBan.getInstance().getSettingsManager().getLang().getString(this.getPath());
		} else {
			message = defaultValue;
		}

		if (!VoteBan.getInstance().getConfig().getBoolean("use-mini-message")) {
			message = ColorUtil.replace(message);
		}

		return message;
	}

	public static void broadcast(String message) {
		Bukkit.getOnlinePlayers().stream().forEach(player -> sendMessage(player, message));
	}

	public static void sendMiniMessage(CommandSender sender, String message) {
		Audience audience = VoteBan.getInstance().getAdventure().sender(sender);
		MiniMessage mm = MiniMessage.miniMessage();
		Component replacedMessage = mm.deserialize(message);
		audience.sendMessage(replacedMessage);
	}

	public static void sendMessage(CommandSender sender, String message) {
		if (message.equalsIgnoreCase(""))
			return;

		if (VoteBan.getInstance().getConfig().getBoolean("use-mini-message")) {
			sendMiniMessage(sender, message);
			return;
		}

		String[] multimessage = message.split("\\\\n");

		for (String msg : multimessage) {
			sender.sendMessage(msg.replace("\\n", ""));
		}
	}
}