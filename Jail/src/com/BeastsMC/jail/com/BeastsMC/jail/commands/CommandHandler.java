package com.BeastsMC.jail.com.BeastsMC.jail.commands;

import com.BeastsMC.jail.Jail;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Zane on 12/24/14.
 */
public class CommandHandler implements CommandExecutor {
    private final Jail jail;
    public CommandHandler(Jail jail) {
        this.jail = jail;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String cmd, String[] args) {
        return false;
    }
}
