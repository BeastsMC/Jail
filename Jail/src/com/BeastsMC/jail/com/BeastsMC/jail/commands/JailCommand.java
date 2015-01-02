package com.BeastsMC.jail.com.BeastsMC.jail.commands;

import com.BeastsMC.jail.JailPlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 * Created by Zane on 12/24/14.
 */
public class JailCommand implements GenericCommand {
    private final JailPlugin jailPlugin;

    public JailCommand(JailPlugin jailPlugin) {
        this.jailPlugin = jailPlugin;
    }

    /**
     * Command format:
     * /jail [username] [time in minutes] [reason]
     *
     * @param sender
     * @param args
     * @return
     */
    @Override
    public boolean handle(CommandSender sender, String[] args) {
        if (args.length <= 3) {
            sender.sendMessage(ChatColor.RED + "Error! Jail command should have a target, time, and reason!");
            return false;
        } else {

            String target = args[0];
            int time = -1;
            try {
                time = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Error! Time was not a positive whole number!");
                return false;
            }
            String reason = StringUtils.join(args, ' ', 2, args.length - 1);

            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(target);
            if (offlineTarget.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.RED + "Warning! " + target + " has never played before, but I will attempt to jail them!");
                return false;
            }


        }
        return true;
    }

    public void jailPlayer(OfflinePlayer target) {

    }
}
