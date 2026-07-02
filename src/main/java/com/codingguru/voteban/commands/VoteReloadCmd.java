package com.codingguru.voteban.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.utils.MessagesUtil;

public class VoteReloadCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("VOTEBAN.*") && !sender.hasPermission("VOTEBAN.RELOAD")) {
			MessagesUtil.sendMessage(sender, MessagesUtil.NO_PERMISSION.toString());
			return false;
		}

		VoteBan PLUGIN = VoteBan.getInstance();
		PLUGIN.getSettingsManager().setup(PLUGIN);
		PLUGIN.reloadConfig();
		MessagesUtil.sendMessage(sender, MessagesUtil.RELOAD.toString());
		return false;
	}
}
