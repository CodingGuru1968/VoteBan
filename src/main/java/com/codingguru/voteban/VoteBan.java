package com.codingguru.voteban;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.codingguru.voteban.commands.AddVoteCmd;
import com.codingguru.voteban.commands.VoteBanCmd;
import com.codingguru.voteban.commands.VoteKickCmd;
import com.codingguru.voteban.commands.VoteMuteCmd;
import com.codingguru.voteban.commands.VoteReloadCmd;
import com.codingguru.voteban.handlers.ManagerHandler;
import com.codingguru.voteban.listeners.AsyncPlayerChat;
import com.codingguru.voteban.managers.LanguageManager;
import com.codingguru.voteban.scheduler.FilterTask;
import com.codingguru.voteban.util.ConsoleUtil;
import com.codingguru.voteban.util.ServerTypeUtil;
import com.tchristofferson.configupdater.ConfigUpdater;

public class VoteBan extends JavaPlugin {

	private static VoteBan INSTANCE;
	private ServerTypeUtil serverType;

	@Override
	public void onEnable() {
		INSTANCE = this;

		setupServerType();

		ConsoleUtil.sendPluginSetup();

		saveDefaultConfig();
		
		try {
			ConfigUpdater.update(this, "config.yml", new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		reloadConfig();
		
		registerManagers();
		
		ManagerHandler.getInstance().startAll();

		registerHooksAndListeners();

		FilterTask filterTask = new FilterTask(this);
		filterTask.runTaskAtFixedRate(20 * 300);
	}
	
	public void onDisable() {
		ManagerHandler.getInstance().stopAll();
	}

	public void reload() {
		ManagerHandler.getInstance().stopAll();
		reloadConfig();
		ManagerHandler.getInstance().startAll();
	}
	
	private void registerHooksAndListeners() {
		getCommand("voteban").setExecutor(new VoteBanCmd(this));
		getCommand("votekick").setExecutor(new VoteKickCmd(this));
		getCommand("votemute").setExecutor(new VoteMuteCmd(this));
		getCommand("votereload").setExecutor(new VoteReloadCmd(this));
		getCommand("addvote").setExecutor(new AddVoteCmd());
		getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);
	}
	
	private void registerManagers() {
		ManagerHandler managerRegistry = ManagerHandler.getInstance();
		managerRegistry.register(LanguageManager.class, new LanguageManager(this));
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

	public static VoteBan getInstance() {
		return INSTANCE;
	}

}