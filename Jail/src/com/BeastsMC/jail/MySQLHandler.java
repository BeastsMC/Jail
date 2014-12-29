package com.BeastsMC.jail;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Zane on 12/24/14.
 * DO NOT CALL METHODS SYNCHRONOUSLY
 */
public class MySQLHandler {

    private static final int MAX_RETRIES = 3;
    private static final String JAIL_TABLE_DEFINITION = "CREATE TABLE IF NOT EXISTS jails (name varchar(255), world varchar(255), corner1 blob, corner2 blob, telein blob, teleout blob)";
    private static final String PRISON_TABLE_DEFINITION = "CREATE TABLE IF NOT EXISTS prisoners (prisoner_uuid char(36), jailer_uuid char(36), jail_name varchar(255), punishment int, remaining int, reason varchar(255), inventory text, PRIMARY_KEY(prisoner_uuid))";

    private final Jail jail;
    private final String url;
    private final String username;
    private final String password;

    private Connection conn;

    private HashMap<JailSQLQueries, PreparedStatement> pstmts;

    public MySQLHandler(Jail jail, String host, short port, String database, String username, String password) {
        this.jail = jail;
        this.username = username;
        this.password = password;
        this.url = String.format("jdbc:mysql://%s:%i/%s", host, port, database);

        if(!openConnection()) {
            jail.getLogger().severe("Could not initialize connection to MySQL! Make sure the database info is correct.");
            jail.getServer().getPluginManager().disablePlugin(jail);
        }

        if(!createTables()) {
            jail.getLogger().severe("Could not create tables! Make sure the database info is correct.");
            jail.getServer().getPluginManager().disablePlugin(jail);
        }

        if(!prepareStatements()) {
            jail.getLogger().severe("Could not create tables! Make sure the database info is correct.");
            jail.getServer().getPluginManager().disablePlugin(jail);
        }

    }
    private boolean openConnection() {
        int tries = 0;
        boolean success = false;
        while(!success & tries < MAX_RETRIES) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                this.conn = DriverManager.getConnection(url, username, password);
                success = true;
            } catch(SQLException e) {
                tries++;
                jail.getLogger().severe("Could not connect to database! SQLException");
                jail.getLogger().severe("Reattempting connection. Current retries: " + tries + "; Max: " + MAX_RETRIES);
                e.printStackTrace();
            } catch(ClassNotFoundException e) {
                tries = MAX_RETRIES; //Exception will not be resolved by retrying
                jail.getLogger().severe("Could not connect to database! Class not found");
                e.printStackTrace();
            }
        }
        return success;
    }

    private boolean createTables() {
        try {
            getPreparedStatement(JailSQLQueries.JAIL_TABLE_DEFINITION).executeUpdate();
            getPreparedStatement(JailSQLQueries.PRISON_TABLE_DEFINITION).executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean prepareStatements() {
        boolean ret = true;
        for(JailSQLQueries type : JailSQLQueries.values()) {
            ret = ret && prepareStatement(type);
        }
        return ret;
    }

    private boolean prepareStatement(JailSQLQueries type) {
        try {
            if(conn.isClosed() && !openConnection()) {
                return false;
            }
            if(pstmts.containsKey(type)) {
                if(pstmts.get(type).isClosed()) {
                    PreparedStatement stmt = conn.prepareStatement(type.query);
                    pstmts.put(type, stmt);
                }
            } else {
                PreparedStatement stmt = conn.prepareStatement(type.query);
                pstmts.put(type, stmt);
            }
        } catch (SQLException e) {
            jail.getLogger().severe("UNABLE TO PREPARE STATEMENT: " + type.query);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private PreparedStatement getPreparedStatement(JailSQLQueries type) {
        PreparedStatement stmt;
        try {
            if(pstmts.get(type).isClosed()) {
                prepareStatement(type);
            }
            return pstmts.get(type);
        } catch (SQLException e) {
            return null;
        }
    }

    public Prisoner getPrisoner(UUID uuid) throws SQLException {
        PreparedStatement stmt = getPreparedStatement(JailSQLQueries.FETCH_PRISONER_FROM_UUID);
        stmt.setString(1, uuid.toString());
        ResultSet rs = stmt.executeQuery();
        Prisoner prisoner = null;
        if(rs.first()) {
            prisoner = new Prisoner(
                    rs.getString("prisoner_uuid"),
                    rs.getString("jailer_uuid"),
                    rs.getString("reason"),
                    rs.getInt("punishment"),
                    rs.getInt("remaining"),
                    rs.getString("inventory"),
                    rs.getString("jail")
            );
        }
        return prisoner;
    }

    public HashSet<String> getAllPrisonerUUIDs() throws SQLException {

        HashSet<String> results = new HashSet<String>();

        PreparedStatement stmt = getPreparedStatement(JailSQLQueries.FETCH_PRISONER_UUIDS);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            results.add(rs.getString("prisoner_uuid"));
        }

        return results;
    }

    public void jailPlayer(String prisoner, String jailer, int time, String reason, String inventory, boolean online) throws SQLException {
        PreparedStatement stmt = getPreparedStatement(JailSQLQueries.ADD_PRISONER);
        stmt.setString(1, prisoner.getUniqueId().toString());
        stmt.setString(2, jailer.getUniqueId().toString());
        stmt.setString(3, jail.getMainJail());
        stmt.setInt(4, time);
        stmt.setInt(5, time);
        stmt.setString(6, inventory);
        stmt.setBoolean(7, online);
    }
}
