package com.codingguru.voteban.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.codingguru.voteban.handlers.VoteHandler;
import com.codingguru.voteban.utils.MessagesUtil;

public class AsyncPlayerChat implements Listener {

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		if (VoteHandler.getInstance().isChatDisabled()) {
			e.setCancelled(true);
			MessagesUtil.sendMessage(e.getPlayer(), MessagesUtil.CHAT_DISABLED.toString());
		}
	}
}
