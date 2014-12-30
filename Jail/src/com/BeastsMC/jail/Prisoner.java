package com.BeastsMC.jail;

import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Created by Zane on 12/28/14.
 */
public class Prisoner {
    private final UUID pid;
    private final UUID staffId;
    private final String reason;
    private final int punishment;
    private int remaining;
    private final String inventory;
    private boolean dirty;
    private boolean online;

    public Prisoner(String pid, String staffId, String reason, int punishment, int remaining, String inventory, boolean dirty) {
        this.pid = UUID.fromString(pid);
        this.staffId = UUID.fromString(staffId);
        this.reason = reason;
        this.punishment = punishment;
        this.remaining = remaining;
        this.inventory = inventory;
        this.dirty = dirty;
    }

    public void setOnline() {
        online = Bukkit.getServer().getPlayer(pid) == null;
    }


    public String getReason() {
        return reason;
    }

    public UUID getPid() {
        return pid;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public int getPunishment() {
        return punishment;
    }

    public int getRemaining() {
        return remaining;
    }

    public String getInventory() {
        return inventory;
    }

    public boolean isDirty() {
        return dirty;
    }

    public boolean isOnline() {
        return online;
    }


}
