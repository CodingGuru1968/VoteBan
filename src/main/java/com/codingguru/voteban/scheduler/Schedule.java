package com.codingguru.voteban.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.util.ServerTypeUtil;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

public abstract class Schedule implements Runnable {

	protected final VoteBan plugin;
	private final boolean USING_FOLIA;

	private BukkitTask bukkitTask;
	private ScheduledTask foliaTask;

	public Schedule(VoteBan plugin) {
		this.plugin = plugin;
		this.USING_FOLIA = plugin.getServerType() == ServerTypeUtil.FOLIA;
	}

	public void runTask() {
		if (USING_FOLIA) {
			Bukkit.getGlobalRegionScheduler().execute(plugin, this);
		} else {
			bukkitTask = Bukkit.getScheduler().runTask(plugin, this);
		}
	}

	public void runTaskLater(long delay) {
		if (USING_FOLIA) {
			foliaTask = Bukkit.getGlobalRegionScheduler().runDelayed(plugin, t -> this.run(), delay);
		} else {
			bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, this, delay);
		}
	}

	public void runTaskAtFixedRate(long delay) {
		if (USING_FOLIA) {
			foliaTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, t -> this.run(), delay, delay);
		} else {
			bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, this, delay, delay);
		}
	}

	public void cancel() {
		if (USING_FOLIA) {
			if (foliaTask != null) {
				foliaTask.cancel();
				foliaTask = null;
			}
		} else {
			if (bukkitTask != null) {
				bukkitTask.cancel();
				bukkitTask = null;
			}
		}
	}
}