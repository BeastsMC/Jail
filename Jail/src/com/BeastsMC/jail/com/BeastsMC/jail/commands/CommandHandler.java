package com.BeastsMC.jail.com.BeastsMC.jail.commands;

import com.BeastsMC.jail.Jail;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * Created by Zane on 12/24/14.
 */
public class CommandHandler implements CommandExecutor {
    private final Jail jail;
    private final HashMap<String, GenericCommand> commands;
    public CommandHandler(Jail jail) {
        this.jail = jail;
        commands = new HashMap<String, GenericCommand>();
        commands.put("jail", new JailCommand(jail));
        commands.put("unjail", new UnjailCommand(jail));
        commands.put("jailcreate", new JailCreateCommand(jail));
        commands.put("jailstatus", new JailStatusCommand(jail));
        commands.put("jailremove", new JailRemoveCommand(jail));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        return commands.get(cmd.toLowerCase()).handle(sender, args);
    }
}
