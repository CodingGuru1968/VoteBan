package com.codingguru.voteban.util;

public final class LangDefaults {

    private LangDefaults() {
    }
	
    public static final String FAILED_VOTE_KICK_BROADCAST = "&a%player% &edid not receive enough votes and will not be kicked from the server.";
    public static final String FAILED_VOTE_BAN_BROADCAST = "&a%player% &edid not receive enough votes and will remain playing this server.";
    public static final String FAILED_VOTE_MUTE_BROADCAST = "&a%player% &edid not receive enough votes and will remain unmuted.";
    public static final String SUCCESSFUL_VOTE_KICK_BROADCAST = "&c&l%player% has been voted to be kicked from the server. Reason: %reason%";
    public static final String SUCCESSFUL_VOTE_BAN_BROADCAST = "&c&l%player% has been voted to be banned from the server. Reason: %reason%";
    public static final String SUCCESSFUL_VOTE_MUTE_BROADCAST = "&c&l%player% has been voted to be muted from the chat. Reason: %reason%";
    public static final String VOTE_KICK_BROADCAST = "&eThere is currently a vote kick in progress for &a%player%&e. If you agree this player should be kicked, type &a/addvote&e. Reason: &a%reason%";
    public static final String VOTE_BAN_BROADCAST = "&eThere is currently a vote ban in progress for &a%player%&e. If you agree this player should be banned, type &a/addvote&e. Reason: &a%reason%";
    public static final String VOTE_MUTE_BROADCAST = "&eThere is currently a vote mute in progress for &a%player%&e. If you agree this player should be muted, type &a/addvote&e. Reason: &a%reason%";
    public static final String SUCCESSFUL_VOTE = "&eYou have successfully added your vote for %player%.";
    public static final String VOTE_ADDED = "&e%votedplayer% has voted for %target% to be %type%.";
    public static final String KICK_MESSAGE = "&cYou have been voted to be kicked by players of the server!\nKick Reason: %reason%";
    public static final String ALREADY_VOTED_FOR = "&c%player% recently had a vote started on them so cannot be voted on again.";
    public static final String CHAT_DISABLED = "&cChat is currently disabled because there is a vote in progress.";
    public static final String ALREADY_VOTED = "&cYou have already voted for this.";
    public static final String SERVER_OPERATOR = "&cYou cannot ban another server operator. Please demote this player first then execute this command again.";
    public static final String CANNOT_EXECUTE_THIS_PLAYER = "&cYou cannot start a vote command on: %player%";
    public static final String CANNOT_EXECUTE_YOURSELF = "&cYou cannot execute this command on yourself.";
    public static final String NOT_ACTIVE_VOTE = "&cThere is currently not a vote active.";
    public static final String ACTIVE_VOTE = "&cThere is already an active vote. Please wait for this to end before creating a new one.";
    public static final String INCORRECT_USAGE = "&cCorrect Usage: %command%";
    public static final String INCORRECT_VALUE = "&cYou have entered an incorrect value for the length.";
    public static final String INCORRECT_UNIT = "&cYou have entered an incorrect value for the unit. Please enter: second, minute, hour, day, month";
    public static final String NOT_BANNED = "&cThis player has not been banned from this server.";
    public static final String PLAYER_NOT_FOUND = "&c%player% is not currently online.";
    public static final String IN_GAME_ONLY = "&cThis command can only be executed in game.";
    public static final String NO_PERMISSION = "&cYou do not have permission to execute this command.";
    public static final String ALREADY_BANNED = "&cThis player is already permanently banned from this server.";
    public static final String NOT_ENABLED = "&cThis command is not currently enabled.";
    public static final String RELOAD = "&aYou have successfully reloaded all configuration files.";
    public static final String NO_BAN_REASON = "Not Specified";
    
}