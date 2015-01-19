package com.BeastsMC.jail;

import com.BeastsMC.jail.com.BeastsMC.jail.commands.CommandHandler;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Zane on 12/24/14.
 */
public class JailPlugin extends JavaPlugin {

    private MySQLHandler mysql;

    public static JailPlugin instance;

    //Configuration settings
    private String mainJailName;
    private Jail mainJail;
    private boolean saveInventories;
    private boolean saveLocatons;
    private HashSet<String> whitelistedCommands;

    private HashSet<String> jailedPlayers;
    private ArrayList<Prisoner> jailedOnlinePlayers;

    public void onEnable() {
        instance = this;
        try {
            loadConfiguration();
        } catch (IOException |InvalidConfigurationException e) {
            getLogger().severe("Unable to load configuration! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            e.printStackTrace();
        }
        setupMySQL();
        setupListeners();
        setupCommands();

        try {
            jailedPlayers = mysql.getAllPrisonerUUIDs();
        } catch (SQLException e) {
            jailedPlayers = new HashSet<String>();
            getLogger().severe("Unable to load prisoners! Check MySQL info.");
            e.printStackTrace();
        }
        jailedOnlinePlayers = new ArrayList<Prisoner>();
    }

    public void onDisable() {
        instance = null;
        //TODO save to database
    }

    private void setupCommands() {
        CommandHandler executor = new CommandHandler(this);
        getCommand("jail").setExecutor(executor);
        getCommand("jailstatus").setExecutor(executor);
        getCommand("unjail").setExecutor(executor);
        getCommand("jailcreate").setExecutor(executor);
        getCommand("jailremove").setExecutor(executor);
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

        mainJailName = getConfig().getString("main-jail-name");
        saveInventories = getConfig().getBoolean("save.inventories");
        saveLocatons = getConfig().getBoolean("save.locations");
        whitelistedCommands = new HashSet<String>(getConfig().getStringList("whitelisted-commands"));
    }


    public String getMainJailName() {
        return mainJailName;
    }

    public Jail getMainJail() {
        return mainJail;
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

    public HashSet<String> getJailedPlayers() {
        return jailedPlayers;
    }

    public ArrayList<Prisoner> getJailedOnlinePlayers() {
        return jailedOnlinePlayers;
    }
    public MySQLHandler getMysql() { return mysql; }
}
