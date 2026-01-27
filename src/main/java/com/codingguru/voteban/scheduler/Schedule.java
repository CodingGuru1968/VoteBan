package com.codingguru.voteban.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.codingguru.voteban.VoteBan;
import com.codingguru.voteban.utils.ServerTypeUtil;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

public abstract class Schedule implements Runnable {

	private final boolean USING_FOLIA = VoteBan.getInstance().getServerType() == ServerTypeUtil.FOLIA;

	private BukkitTask bukkitTask;
	private ScheduledTask foliaTask;

	public void runTask() {
		if (USING_FOLIA) {
			Bukkit.getGlobalRegionScheduler().execute(VoteBan.getInstance(), this);
		} else {
			bukkitTask = Bukkit.getScheduler().runTask(VoteBan.getInstance(), this);
		}
	}

	public void runTaskLater(long delay) {
		if (USING_FOLIA) {
			foliaTask = Bukkit.getGlobalRegionScheduler().runDelayed(VoteBan.getInstance(), t -> this.run(), delay);
		} else {
			bukkitTask = Bukkit.getScheduler().runTaskLater(VoteBan.getInstance(), this, delay);
		}
	}

	public void runTaskAtFixedRate(long delay) {
		if (USING_FOLIA) {
			foliaTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(VoteBan.getInstance(), t -> this.run(), delay,
					delay);
		} else {
			bukkitTask = Bukkit.getScheduler().runTaskTimer(VoteBan.getInstance(), this, delay, delay);
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