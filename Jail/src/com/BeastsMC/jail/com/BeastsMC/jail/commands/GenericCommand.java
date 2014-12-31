package com.BeastsMC.jail.com.BeastsMC.jail.commands;

import org.bukkit.command.CommandSender;

/**
 * Created by Zane on 12/30/14.
 */
public interface GenericCommand {
    public boolean handle(CommandSender sender, String[] args);
}
