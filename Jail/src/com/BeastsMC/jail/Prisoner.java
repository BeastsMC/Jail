package com.BeastsMC.jail;

import java.util.UUID;

/**
 * Created by Zane on 12/28/14.
 */
public class Prisoner {
    private final UUID pid;
    private final UUID staffId;
    private final String reason;
    private final int punishment;
    private final int remaining;
    private final String inventory;
    private final String jail;

    public Prisoner(String pid, String staffId, String reason, int punishment, int remaining, String inventory, String jail) {
        this.pid = UUID.fromString(pid);
        this.staffId = UUID.fromString(staffId);
        this.reason = reason;
        this.punishment = punishment;
        this.remaining = remaining;
        this.inventory = inventory;
        this.jail = jail;
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

    public String getJail() {
        return jail;
    }
}
