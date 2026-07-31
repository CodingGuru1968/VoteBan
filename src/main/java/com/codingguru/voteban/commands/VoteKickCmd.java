package com.codingguru.voteban.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.handlers.VoteHandler;
import com.codingguru.voteban.scheduler.StartVoteTask;
import com.codingguru.voteban.scheduler.VoteType;
import com.codingguru.voteban.util.LangDefaults;
import com.codingguru.voteban.util.MessageBuilder;

public class VoteKickCmd implements CommandExecutor {

	private final VoteBan plugin;

	public VoteKickCmd(VoteBan plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (plugin.getConfig().getBoolean("vote-kick.requires-permission")
				&& !sender.hasPermission("voteban.*") && !sender.hasPermission("voteban.startkick")) {
			new MessageBuilder.Builder("no-permission", LangDefaults.NO_PERMISSION).send(sender);
			return false;
		}

		if (!plugin.getConfig().getBoolean("vote-kick.enabled")) {
			new MessageBuilder.Builder("not-enabled", LangDefaults.NOT_ENABLED).send(sender);
			return false;
		}

		if (VoteHandler.getInstance().hasActiveVote()) {
			new MessageBuilder.Builder("active-vote", LangDefaults.ACTIVE_VOTE).send(sender);
			return false;
		}

		if (args.length == 0) {
			new MessageBuilder.Builder("incorrect-usage", LangDefaults.INCORRECT_USAGE)
					.set("%command%", "/votekick <player> [reason]").send(sender);
			return false;
		}

		Player target = Bukkit.getPlayer(args[0]);

		if (target == null) {
			new MessageBuilder.Builder("player-not-found", LangDefaults.PLAYER_NOT_FOUND).set("%player%", args[0])
					.send(sender);
			return false;
		}

		if (sender instanceof Player && sender.getName().equalsIgnoreCase(target.getName())) {
			new MessageBuilder.Builder("cannot-execute-yourself", LangDefaults.CANNOT_EXECUTE_YOURSELF).send(sender);
			return false;
		}

		if (target.hasPermission("voteban.*") || target.hasPermission("voteban.bypass")) {
			new MessageBuilder.Builder("cannot-execute-this-player", LangDefaults.CANNOT_EXECUTE_THIS_PLAYER)
					.set("%player%", args[0]).send(sender);
			return false;
		}

		if (!VoteHandler.getInstance().isVoteAllowed(VoteType.KICK, target.getUniqueId())) {
			new MessageBuilder.Builder("already-voted-for", LangDefaults.ALREADY_VOTED_FOR).set("%player%", args[0])
					.send(sender);
			return false;
		}

		boolean addVote = plugin.getConfig().getBoolean("vote-kick.automatically-add-vote");

		if (args.length == 1) {
			StartVoteTask startVoteTask = new StartVoteTask(plugin, target, (Player) sender, null,
					VoteType.KICK, addVote);
			startVoteTask.runTaskAtFixedRate(20);
			return true;
		}

		String reason = "";

		for (int i = 1; i < args.length; i++) {
			reason = String.valueOf(reason) + args[i] + " ";
		}

		StartVoteTask kickTask = new StartVoteTask(plugin, target, (Player) sender, reason,
				VoteType.KICK, addVote);
		kickTask.runTaskAtFixedRate(20);
		return false;
	}
}