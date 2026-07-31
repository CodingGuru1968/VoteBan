package com.codingguru.voteban.util;

public final class ColorUtil {

	@SuppressWarnings("deprecation")
	public static String replace(String s) {
		if (s == null)
			return "";

		return org.bukkit.ChatColor.translateAlternateColorCodes('&', s);
	}

}