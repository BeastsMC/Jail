package com.BeastsMC.jail;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Zane on 1/2/15.
 */
public class PrisonerLoginCallback {
    private final UUID pid;

    public PrisonerLoginCallback(UUID pid) {
        this.pid = pid;
    }

    public void handle(Prisoner loadedPrisoner) {

    }
}
