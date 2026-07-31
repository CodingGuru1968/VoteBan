package com.codingguru.voteban.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.codingguru.voteban.handlers.VoteHandler;
import com.codingguru.voteban.scheduler.StartVoteTask;
import com.codingguru.voteban.util.LangDefaults;
import com.codingguru.voteban.util.MessageBuilder;

public class AddVoteCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (sender instanceof ConsoleCommandSender) {
			new MessageBuilder.Builder("in-game-only", LangDefaults.IN_GAME_ONLY).send(sender);
			return false;
		}

		if (!sender.hasPermission("voteban.*") && !sender.hasPermission("voteban.vote")) {
			new MessageBuilder.Builder("no-permission", LangDefaults.NO_PERMISSION).send(sender);
			return false;
		}

		if (!VoteHandler.getInstance().hasActiveVote()) {
			new MessageBuilder.Builder("not-active-vote", LangDefaults.NOT_ACTIVE_VOTE).send(sender);
			return false;
		}

		Player player = (Player) sender;

		StartVoteTask votingThread = VoteHandler.getInstance().getActiveVote();

		if (!votingThread.canVote(player)) {
			new MessageBuilder.Builder("already-voted", LangDefaults.ALREADY_VOTED).send(sender);
			return false;
		}

		votingThread.addVote(player);
		return false;
	}
}
