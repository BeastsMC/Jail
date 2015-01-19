package com.BeastsMC.jail;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
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
        if (loadedPrisoner.isDirty()) {
            Player player = Bukkit.getPlayer(pid);
            if (player == null) {
                JailPlugin.instance.getLogger().severe("ERROR! Logged in player was NULL");
                return;
            }
            final String[] inv = CommonFunctions.playerInventoryToBase64(player.getInventory());

            JailPlugin.instance.getServer().getScheduler().runTaskAsynchronously(JailPlugin.instance,
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                JailPlugin.instance.getMysql().saveInventory(pid.toString(), inv);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        }
    }
}
