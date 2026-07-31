package com.codingguru.voteban.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.codingguru.voteban.handlers.VoteHandler;
import com.codingguru.voteban.util.LangDefaults;
import com.codingguru.voteban.util.MessageBuilder;

@SuppressWarnings("deprecation")
public class AsyncPlayerChat implements Listener {

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {				
		if (VoteHandler.getInstance().isChatDisabled()) {
			e.setCancelled(true);
			new MessageBuilder.Builder("chat-disabled", LangDefaults.CHAT_DISABLED).send(e.getPlayer());
		}
	}
}
