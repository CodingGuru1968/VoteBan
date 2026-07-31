package com.codingguru.voteban.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.api.PluginManager;
import com.codingguru.voteban.util.ConsoleUtil;

public final class LanguageManager implements PluginManager {

	private final VoteBan plugin;
	private FileConfiguration langConfig;
	private File langFile;

	public LanguageManager(VoteBan plugin) {
		this.plugin = plugin;
	}

	@Override
	public void start() {
		initializeLanguageFile();
	}

	@Override
	public void stop() {
		langFile = null;
		langConfig = null;
	}

	private void initializeLanguageFile() {
		File langDirectory = new File(plugin.getDataFolder(), "lang");

		if (!langDirectory.exists() && !langDirectory.mkdirs()) {
			ConsoleUtil.warning("Failed to create the 'lang' directory.");
		}

		String languageName = plugin.getConfig().getString("language", "en");
		String resourcePath = "lang/" + languageName + ".yml";

		langFile = new File(plugin.getDataFolder(), resourcePath);

		if (!langFile.exists()) {
			try {
				plugin.saveResource(resourcePath, false);
			} catch (IllegalArgumentException ex) {
				ConsoleUtil.info(
						"Language file " + languageName + ".yml not found in plugin jar. Creating a blank file...");
				try {
					langFile.createNewFile();
				} catch (IOException ioException) {
					ConsoleUtil.warning(
							"Could not create language file: " + langFile.getName() + ioException.getMessage());
				}
			}
		}

		migrateAndCleanOldFiles(langFile);

		langConfig = YamlConfiguration.loadConfiguration(langFile);
	}

	public FileConfiguration getLang() {
		return langConfig;
	}

	public void saveLang() {
		if (langConfig == null || langFile == null) {
			return;
		}

		try {
			langConfig.save(langFile);
		} catch (IOException ex) {
			ConsoleUtil.warning("Could not save language file to " + langFile + " : " + ex.getMessage());
		}
	}

	private void migrateAndCleanOldFiles(File activeLangFile) {
		File oldLegacyFile = new File(plugin.getDataFolder(), "lang.yml");

		if (oldLegacyFile.exists()) {
			ConsoleUtil.info("Found legacy VoteBan language file. Migrating values before deletion...");

			FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(oldLegacyFile);
			FileConfiguration newConfig = YamlConfiguration.loadConfiguration(activeLangFile);

			Map<String, String> migrationMap = new HashMap<>();
			migrationMap.put("FAILED_VOTE_KICK_BROADCAST", "failed-vote-kick-broadcast");
			migrationMap.put("FAILED_VOTE_BAN_BROADCAST", "failed-vote-ban-broadcast");
			migrationMap.put("FAILED_VOTE_MUTE_BROADCAST", "failed-vote-mute-broadcast");
			migrationMap.put("SUCCESSFUL_VOTE_KICK_BROADCAST", "successful-vote-kick-broadcast");
			migrationMap.put("SUCCESSFUL_VOTE_BAN_BROADCAST", "successful-vote-ban-broadcast");
			migrationMap.put("SUCCESSFUL_VOTE_MUTE_BROADCAST", "successful-vote-mute-broadcast");
			migrationMap.put("VOTE_KICK_BROADCAST", "vote-kick-broadcast");
			migrationMap.put("VOTE_BAN_BROADCAST", "vote-ban-broadcast");
			migrationMap.put("VOTE_MUTE_BROADCAST", "vote-mute-broadcast");
			migrationMap.put("SUCCESSFUL_VOTE", "successful-vote");
			migrationMap.put("VOTE_ADDED", "vote-added");
			migrationMap.put("KICK_MESSAGE", "kick-message");
			migrationMap.put("ALREADY_VOTED_FOR", "already-voted-for");
			migrationMap.put("CHAT_DISABLED", "chat-disabled");
			migrationMap.put("ALREADY_VOTED", "already-voted");
			migrationMap.put("SERVER_OPERATOR", "server-operator");
			migrationMap.put("CANNOT_EXECUTE_THIS_PLAYER", "cannot-execute-this-player");
			migrationMap.put("CANNOT_EXECUTE_YOURSELF", "cannot-execute-yourself");
			migrationMap.put("NOT_ACTIVE_VOTE", "not-active-vote");
			migrationMap.put("ACTIVE_VOTE", "active-vote");
			migrationMap.put("INCORRECT_USAGE", "incorrect-usage");
			migrationMap.put("INCORRECT_VALUE", "incorrect-value");
			migrationMap.put("INCORRECT_UNIT", "incorrect-unit");
			migrationMap.put("NOT_BANNED", "not-banned");
			migrationMap.put("PLAYER_NOT_FOUND", "player-not-found");
			migrationMap.put("IN_GAME_ONLY", "in-game-only");
			migrationMap.put("NO_PERMISSION", "no-permission");
			migrationMap.put("ALREADY_BANNED", "already-banned");
			migrationMap.put("NOT_ENABLED", "not-enabled");
			migrationMap.put("RELOAD", "reload");
			migrationMap.put("NO_BAN_REASON", "no-ban-reason");

			boolean migrated = false;

			for (Map.Entry<String, String> entry : migrationMap.entrySet()) {
				if (oldConfig.contains(entry.getKey())) {
					newConfig.set(entry.getValue(), oldConfig.get(entry.getKey()));
					migrated = true;
				}
			}

			if (migrated) {
				try {
					newConfig.save(activeLangFile);
					ConsoleUtil.info("Successfully migrated old VoteBan values into the new format.");
				} catch (IOException e) {
					ConsoleUtil.warning("Failed to save the new VoteBan language file during migration!");
					e.printStackTrace();
				}
			}

			if (oldLegacyFile.delete()) {
				ConsoleUtil.info("Successfully deleted obsolete VoteBan legacy file.");
			} else {
				ConsoleUtil.warning("Could not delete old VoteBan legacy file.");
			}
		}
	}
}