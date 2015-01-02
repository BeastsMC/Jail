package com.BeastsMC.jail.com.BeastsMC.jail.commands;

import com.BeastsMC.jail.JailPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * Created by Zane on 12/24/14.
 */
public class CommandHandler implements CommandExecutor {
    private final JailPlugin jailPlugin;
    private final HashMap<String, GenericCommand> commands;

    public CommandHandler(JailPlugin jailPlugin) {
        this.jailPlugin = jailPlugin;
        commands = new HashMap<String, GenericCommand>();
        commands.put("jail", new JailCommand(jailPlugin));
        commands.put("unjail", new UnjailCommand(jailPlugin));
        commands.put("jailcreate", new JailCreateCommand(jailPlugin));
        commands.put("jailstatus", new JailStatusCommand(jailPlugin));
        commands.put("jailremove", new JailRemoveCommand(jailPlugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        return commands.get(cmd.toLowerCase()).handle(sender, args);
    }
}
