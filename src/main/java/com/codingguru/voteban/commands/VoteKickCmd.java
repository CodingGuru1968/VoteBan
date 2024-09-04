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
import com.codingguru.voteban.utils.MessagesUtil;

public class VoteKickCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("votekick")) {
			if (!VoteBan.getInstance().getConfig().getBoolean("vote-kick.enabled")) {
				MessagesUtil.sendMessage(sender, MessagesUtil.NOT_ENABLED.toString());
				return false;
			}

			if (VoteBan.getInstance().getConfig().getBoolean("vote-kick.requires-permission")
					&& !sender.hasPermission("VOTEBAN.*") && !sender.hasPermission("VOTEBAN.STARTKICK")) {
				MessagesUtil.sendMessage(sender, MessagesUtil.NO_PERMISSION.toString());
				return false;
			}

			if (VoteHandler.getInstance().hasActiveVote()) {
				MessagesUtil.sendMessage(sender, MessagesUtil.ACTIVE_VOTE.toString());
				return false;
			}

			if (args.length == 0) {
				MessagesUtil.sendMessage(sender,
						MessagesUtil.INCORRECT_USAGE.toString().replaceAll("%command%", "/votekick <player> [reason]"));
				return false;
			}

			Player target = Bukkit.getPlayer(args[0]);

			if (target == null) {
				MessagesUtil.sendMessage(sender,
						MessagesUtil.PLAYER_NOT_FOUND.toString().replaceAll("%player%", args[0]));
				return false;
			}

			if (sender instanceof Player && sender.getName().equalsIgnoreCase(target.getName())) {
				MessagesUtil.sendMessage(sender, MessagesUtil.CANNOT_EXECUTE_YOURSELF.toString());
				return false;
			}

			if (target.hasPermission("VOTEBAN.*") || target.hasPermission("VOTEBAN.BYPASS")) {
				MessagesUtil.sendMessage(sender,
						MessagesUtil.CANNOT_EXECUTE_THIS_PLAYER.toString().replaceAll("%player%", args[0]));
				return false;
			}

			if (!VoteHandler.getInstance().isVoteAllowed(VoteType.KICK, target.getUniqueId())) {
				MessagesUtil.sendMessage(sender,
						MessagesUtil.ALREADY_VOTED_FOR.toString().replace("%player%", args[0]));
				return false;
			}

			boolean addVote = VoteBan.getInstance().getConfig().getBoolean("vote-kick.automatically-add-vote");

			if (args.length == 1) {
				StartVoteTask startVoteTask = new StartVoteTask(target, (Player) sender, null, VoteType.KICK, addVote);
				startVoteTask.runTaskTimer(VoteBan.getInstance(), 20, 20);
				return true;
			}

			String reason = "";

			for (int i = 1; i < args.length; i++) {
				reason = String.valueOf(reason) + args[i] + " ";
			}

			StartVoteTask kickTask = new StartVoteTask(target, (Player) sender, reason, VoteType.KICK, addVote);
			kickTask.runTaskTimer(VoteBan.getInstance(), 20, 20);
		}
		return false;
	}
}