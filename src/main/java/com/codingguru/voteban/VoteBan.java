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
import com.codingguru.voteban.utils.ServerTypeUtil;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class VoteBan extends JavaPlugin {

	private static VoteBan INSTANCE;
	private SettingsManager settingsManager;
	private ServerTypeUtil serverType;
	private BukkitAudiences adventureAPI;

	public void onEnable() {
		INSTANCE = this;

		setupServerType();
		
		ConsoleUtil.sendPluginSetup();

		saveDefaultConfig();

		settingsManager = new SettingsManager();
		settingsManager.setup(this);

		if (getConfig().getBoolean("use-mini-message")) {
			this.adventureAPI = BukkitAudiences.create(this);
		}

		getCommand("voteban").setExecutor(new VoteBanCmd());
		getCommand("votekick").setExecutor(new VoteKickCmd());
		getCommand("votemute").setExecutor(new VoteMuteCmd());
		getCommand("addvote").setExecutor(new AddVoteCmd());

		getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);

		FilterTask filterTask = new FilterTask();
		filterTask.runTaskAtFixedRate(20 * 300);
	}
	
	private void setupServerType() {
		try {
			Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
			serverType = ServerTypeUtil.FOLIA;
			return;
		} catch (ClassNotFoundException ignored) {
		}

		try {
			Class.forName("io.papermc.paper.ServerBuildInfo");
			serverType = ServerTypeUtil.PAPER;
			return;
		} catch (ClassNotFoundException ignored) {
		}

		serverType = ServerTypeUtil.SPIGOT;
	}
	
	public ServerTypeUtil getServerType() {
		return serverType;
	}

	public BukkitAudiences getAdventure() {
		return this.adventureAPI;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public static VoteBan getInstance() {
		return INSTANCE;
	}

}