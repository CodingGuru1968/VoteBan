package com.codingguru.voteban.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import com.codingguru.voteban.VoteBan;

public class ConsoleUtil {

	private final static ConsoleCommandSender CONSOLE = Bukkit.getServer().getConsoleSender();

	public static void sendPluginSetup() {
		CONSOLE.sendMessage(ChatColor.GREEN + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		CONSOLE.sendMessage(ChatColor.GREEN + "Plugin Name: " + ChatColor.YELLOW + "VoteBan");
		CONSOLE.sendMessage(ChatColor.GREEN + "Plugin Version: " + ChatColor.YELLOW
				+ VoteBan.getInstance().getDescription().getVersion());
		CONSOLE.sendMessage(ChatColor.GREEN + "Server Version: " + ChatColor.YELLOW
				+ Bukkit.getBukkitVersion());
		CONSOLE.sendMessage(ChatColor.GREEN + "Author: " + ChatColor.YELLOW + "CodingGuru");
		CONSOLE.sendMessage(ChatColor.GREEN + "Discord: " + ChatColor.YELLOW + "https://discord.gg/CbJxH5NPvX");
		CONSOLE.sendMessage(ChatColor.GREEN + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public static void info(String message) {
		CONSOLE.sendMessage(message);
	}

	public static void warning(String message) {
		CONSOLE.sendMessage("[WARNING] " + message);
	}

}