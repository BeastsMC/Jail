package com.BeastsMC.jail.com.BeastsMC.jail.commands;

import com.BeastsMC.jail.JailPlugin;
import org.bukkit.command.CommandSender;

/**
 * Created by Zane on 12/24/14.
 */
public class JailStatusCommand implements GenericCommand {
    private final JailPlugin jailPlugin;

    public JailStatusCommand(JailPlugin jailPlugin) {
        this.jailPlugin = jailPlugin;
    }

    @Override
    public boolean handle(CommandSender sender, String[] args) {
        return false;
    }
}
