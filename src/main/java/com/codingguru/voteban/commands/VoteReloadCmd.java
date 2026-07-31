package com.codingguru.voteban.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.util.LangDefaults;
import com.codingguru.voteban.util.MessageBuilder;

public class VoteReloadCmd implements CommandExecutor {

	private final VoteBan plugin;

	public VoteReloadCmd(VoteBan plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("voteban.*") && !sender.hasPermission("voteban.reload")) {
			new MessageBuilder.Builder("no-permission", LangDefaults.NO_PERMISSION).send(sender);
			return false;
		}

		plugin.reload();
		new MessageBuilder.Builder("reload", LangDefaults.RELOAD).send(sender);
		return false;
	}
}
