package com.johncorby.coreapi.util;

import com.johncorby.coreapi.CoreApiPlugin;
import com.johncorby.coreapi.PrintObject;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * Convenient version of BukkitRunnable
 * Unlike BukkitRunnable, you can run it again after it's cancelled
 * It also won't throw exceptions :)))
 */
public abstract class Runnable implements java.lang.Runnable, PrintObject {
    private BukkitTask task;

    public final synchronized boolean isCancelled() {
        return task == null || task.isCancelled();
    }

    public synchronized void cancel() {
        if (checkScheduled()) return;
        Bukkit.getScheduler().cancelTask(getTaskId());
        task = null;
    }

    public final synchronized BukkitTask runTask() {
        if (checkNotScheduled()) return null;
        return setupTask(Bukkit.getScheduler().runTask(CoreApiPlugin.PLUGIN, this));
    }

    public final synchronized BukkitTask runTaskAsynchronously() {
        if (checkNotScheduled()) return null;
        return setupTask(Bukkit.getScheduler().runTaskAsynchronously(CoreApiPlugin.PLUGIN, this));
    }

    public final synchronized BukkitTask runTaskLater(final long delay) {
        if (checkNotScheduled()) return null;
        return setupTask(Bukkit.getScheduler().runTaskLater(CoreApiPlugin.PLUGIN, this, delay));
    }

    public final synchronized BukkitTask runTaskLaterAsynchronously(final long delay) {
        if (checkNotScheduled()) return null;
        return setupTask(Bukkit.getScheduler().runTaskLaterAsynchronously(CoreApiPlugin.PLUGIN, this, delay));
    }

    public final synchronized BukkitTask runTaskTimer(final long delay, final long period) {
        if (checkNotScheduled()) return null;
        return setupTask(Bukkit.getScheduler().runTaskTimer(CoreApiPlugin.PLUGIN, this, delay, period));
    }

    public final synchronized BukkitTask runTaskTimerAsynchronously(final long delay, final long period) {
        if (checkNotScheduled()) return null;
        return setupTask(Bukkit.getScheduler().runTaskTimerAsynchronously(CoreApiPlugin.PLUGIN, this, delay, period));
    }

    public final synchronized int getTaskId() {
        if (checkScheduled()) return -1;
        return task.getTaskId();
    }

    private boolean checkScheduled() {
        try {
            if (task == null)
                throw new IllegalStateException("Not scheduled");
        } catch (Exception e) {
            MessageHandler.error(e);
            return true;
        }
        return false;
    }

    private boolean checkNotScheduled() {
        try {
            if (task != null)
                if (!task.isCancelled())
                    throw new IllegalStateException("Still running as task " + getTaskId());
                else {
                    MessageHandler.debug("Cancelled task " + getTaskId());
                    cancel();
                }
        } catch (Exception e) {
            MessageHandler.error(e);
            return true;
        }
        return false;
    }

    private BukkitTask setupTask(final BukkitTask task) {
        return this.task = task;
    }
}
