package com.BeastsMC.jail.com.BeastsMC.jail.commands;

import com.BeastsMC.jail.Jail;
import org.bukkit.command.CommandSender;

/**
 * Created by Zane on 12/30/14.
 */
public class JailRemoveCommand implements GenericCommand {
    private final Jail jail;

    public JailRemoveCommand(Jail jail) {
        this.jail = jail;
    }

    @Override
    public boolean handle(CommandSender sender, String[] args) {
        return false;
    }
}
