package com.codingguru.voteban;

import org.bukkit.plugin.java.JavaPlugin;

import com.codingguru.voteban.commands.AddVoteCmd;
import com.codingguru.voteban.commands.VoteBanCmd;
import com.codingguru.voteban.commands.VoteKickCmd;
import com.codingguru.voteban.commands.VoteMuteCmd;
import com.codingguru.voteban.listeners.AsyncPlayerChat;
import com.codingguru.voteban.managers.SettingsManager;
import com.codingguru.voteban.scheduler.FilterTask;
import com.codingguru.voteban.utils.ConsoleUtil;

public class VoteBan extends JavaPlugin {

	private static VoteBan INSTANCE;
	private SettingsManager settingsManager;

	public void onEnable() {
		INSTANCE = this;

		ConsoleUtil.sendPluginSetup();

		saveDefaultConfig();

		settingsManager = new SettingsManager();
		settingsManager.setup(this);
		
		getCommand("voteban").setExecutor(new VoteBanCmd());
		getCommand("votekick").setExecutor(new VoteKickCmd());
		getCommand("votemute").setExecutor(new VoteMuteCmd());
		getCommand("addvote").setExecutor(new AddVoteCmd());

		getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);
		
		FilterTask filterTask = new FilterTask();
		filterTask.runTaskTimer(VoteBan.getInstance(), 20 * 300, 20 * 300);
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public static VoteBan getInstance() {
		return INSTANCE;
	}

}