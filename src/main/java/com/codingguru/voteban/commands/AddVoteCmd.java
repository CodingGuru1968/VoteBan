package com.codingguru.voteban.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.codingguru.voteban.handlers.ThreadHandler;
import com.codingguru.voteban.scheduler.StartVoteTask;
import com.codingguru.voteban.utils.MessagesUtil;

public class AddVoteCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("addvote")) {
			if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
				MessagesUtil.sendMessage(sender, MessagesUtil.IN_GAME_ONLY.toString());
				return false;
			}

			if (!sender.hasPermission("VOTEBAN.*") && !sender.hasPermission("VOTEBAN.VOTE")) {
				MessagesUtil.sendMessage(sender, MessagesUtil.NO_PERMISSION.toString());
				return false;
			}

			if (!ThreadHandler.getInstance().hasActiveVote()) {
				MessagesUtil.sendMessage(sender, MessagesUtil.NOT_ACTIVE_VOTE.toString());
				return false;
			}

			Player player = (Player) sender;
			StartVoteTask votingThread = ThreadHandler.getInstance().getActiveVote();

			if (!votingThread.canVote(player)) {
				MessagesUtil.sendMessage(sender, MessagesUtil.ALREADY_VOTED.toString());
				return false;
			}

			votingThread.addVote(player);
		}
		return false;
	}
}
