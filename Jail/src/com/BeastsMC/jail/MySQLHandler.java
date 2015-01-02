package com.BeastsMC.jail;

import org.bukkit.Location;

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

    private final JailPlugin jailPlugin;
    private final String url;
    private final String username;
    private final String password;

    private Connection conn;

    private HashMap<JailSQLQueries, PreparedStatement> pstmts;

    public MySQLHandler(JailPlugin jailPlugin, String host, short port, String database, String username, String password) {
        this.jailPlugin = jailPlugin;
        this.username = username;
        this.password = password;
        this.url = String.format("jdbc:mysql://%s:%i/%s", host, port, database);

        if(!openConnection()) {
            jailPlugin.getLogger().severe("Could not initialize connection to MySQL! Make sure the database info is correct.");
            jailPlugin.getServer().getPluginManager().disablePlugin(jailPlugin);
        }

        if(!createTables()) {
            jailPlugin.getLogger().severe("Could not create tables! Make sure the database info is correct.");
            jailPlugin.getServer().getPluginManager().disablePlugin(jailPlugin);
        }

        if(!prepareStatements()) {
            jailPlugin.getLogger().severe("Could not create tables! Make sure the database info is correct.");
            jailPlugin.getServer().getPluginManager().disablePlugin(jailPlugin);
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
                jailPlugin.getLogger().severe("Could not connect to database! SQLException");
                jailPlugin.getLogger().severe("Reattempting connection. Current retries: " + tries + "; Max: " + MAX_RETRIES);
                e.printStackTrace();
            } catch(ClassNotFoundException e) {
                tries = MAX_RETRIES; //Exception will not be resolved by retrying
                jailPlugin.getLogger().severe("Could not connect to database! Class not found");
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
            jailPlugin.getLogger().severe("UNABLE TO PREPARE STATEMENT: " + type.query);
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
                    rs.getBoolean("dirty")
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

    public void jailPlayer(String prisoner, String jailer, int time, String reason, String inventory, boolean dirty) throws SQLException {
        PreparedStatement stmt = getPreparedStatement(JailSQLQueries.ADD_PRISONER);
        stmt.setString(1, prisoner);
        stmt.setString(2, jailer);
        stmt.setString(3, jailPlugin.getMainJailName());
        stmt.setInt(4, time);
        stmt.setInt(5, time);
        stmt.setString(6, inventory);
        stmt.setBoolean(7, dirty);
        stmt.executeUpdate();
    }

    public boolean unjailPlayer(String prisoner) throws SQLException {
        PreparedStatement stmt = getPreparedStatement(JailSQLQueries.UNJAIL_PRISONER);
        stmt.setString(1, prisoner);
        int removed = stmt.executeUpdate();
        return removed > 0;
    }

    public void tickPrisoners(String[] prisoners) throws SQLException {
        PreparedStatement stmt = getPreparedStatement(JailSQLQueries.TICK_PRISONER);
        for (String prisoner : prisoners) {
            stmt.setString(1, prisoner);
            stmt.addBatch();
        }
        stmt.executeBatch();
    }

    public boolean addJail(String name, Location corner1, Location corner2, Location telein, Location teleout) throws SQLException {
        PreparedStatement stmt = getPreparedStatement(JailSQLQueries.ADD_JAIL);

        stmt.setString(1, name);
        stmt.setString(2, CommonFunctions.locationToString(corner1, false, false));
        stmt.setString(3, CommonFunctions.locationToString(corner2, false, false));
        stmt.setString(4, CommonFunctions.locationToString(telein, true, true));
        stmt.setString(5, CommonFunctions.locationToString(teleout, true, true));

        int count = stmt.executeUpdate();
        return count == 1;

    }

    public boolean removeJail(String name) throws SQLException {
        PreparedStatement stmt = getPreparedStatement(JailSQLQueries.REMOVE_JAIL);
        stmt.setString(1, name);
        int count = stmt.executeUpdate();
        return count == 1;
    }
}
