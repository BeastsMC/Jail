package com.BeastsMC.jail.com.BeastsMC.jail.commands;

import com.BeastsMC.jail.Jail;
import org.bukkit.command.CommandSender;

/**
 * Created by Zane on 12/24/14.
 */
public class JailCreateCommand implements GenericCommand {
    private final Jail jail;

    public JailCreateCommand(Jail jail) {
        this.jail = jail;
    }

    @Override
    public boolean handle(CommandSender sender, String[] args) {
        return false;
    }
}
