package com.BeastsMC.jail;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

/**
 * Created by Zane on 12/24/14.
 */
public class PlayerListener implements Listener {

    private final Jail jail;

    public PlayerListener(Jail jail) {
        this.jail = jail;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void playerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID pid = event.getUniqueId();
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void playerLogout(PlayerQuitEvent event) {
        //TODO should be async
        UUID pid = event.getPlayer().getUniqueId();
    }

    @EventHandler(ignoreCancelled = true)
    public void playerBlockBreak(BlockBreakEvent event) {

    }

    @EventHandler(ignoreCancelled = true)
    public void playerBlockPlace(BlockPlaceEvent event) {

    }

    @EventHandler(ignoreCancelled = true)
    public void playerCommand(PlayerCommandPreprocessEvent event) {

    }

    @EventHandler(ignoreCancelled = true)
    public void playerTeleport(PlayerTeleportEvent event) {

    }

    @EventHandler(ignoreCancelled = true)
    public void playerMove(PlayerMoveEvent event) {

    }
}
