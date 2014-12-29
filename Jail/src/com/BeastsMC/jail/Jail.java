package com.BeastsMC.jail;

import com.BeastsMC.jail.com.BeastsMC.jail.commands.CommandHandler;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Zane on 12/24/14.
 */
public class Jail extends JavaPlugin {

    private MySQLHandler mysql;

    //Configuration settings
    private boolean saveInventories;
    private boolean saveLocatons;
    private HashSet<String> whitelistedCommands;

    public void onEnable() {
        try {
            loadConfiguration();
            setupMySQL();
            setupListeners();
            setupCommands();
        } catch (IOException |InvalidConfigurationException e) {
            getLogger().severe("Unable to load configuration! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            e.printStackTrace();
        }
    }

    public void onDisable() {

    }

    private void setupCommands() {
        CommandHandler executor = new CommandHandler(this);
        getCommand("jail").setExecutor(executor);
        getCommand("jailstatus").setExecutor(executor);
        getCommand("unjail").setExecutor(executor);
        getCommand("jailcreate").setExecutor(executor);
    }

    private void setupListeners() {

        PlayerListener pListener = new PlayerListener(this);
        getServer().getPluginManager().registerEvents(pListener, this);
    }

    private void setupMySQL() {
        this.mysql = new MySQLHandler(this,
                                 getConfig().getString("database.host"),
                                 (short)getConfig().getInt("database.port"),
                                 getConfig().getString("database.database"),
                                 getConfig().getString("database.username"),
                                 getConfig().getString("database.password")
        );
    }

    private void loadConfiguration() throws IOException, InvalidConfigurationException {
        saveDefaultConfig();
        getConfig().load(new File(getDataFolder(), "config.yml"));

        saveInventories = getConfig().getBoolean("save.inventories");
        saveLocatons = getConfig().getBoolean("save.locations");
        whitelistedCommands = new HashSet<String>(getConfig().getStringList("whitelisted-commands"));
    }



    public HashSet<String> getWhitelistedCommands() {
        return whitelistedCommands;
    }

    public boolean saveInventories() {
        return saveInventories;
    }

    public boolean saveLocatons() {
        return saveLocatons;
    }
}
