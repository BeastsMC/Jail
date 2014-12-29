package com.BeastsMC.jail;

/**
 * Created by Zane on 12/28/14.
 */
public enum JailSQLQueries {
    JAIL_TABLE_DEFINITION("CREATE TABLE IF NOT EXISTS jails (name varchar(255), world varchar(255), corner1 blob, corner2 blob, telein blob, teleout blob)"),
    PRISON_TABLE_DEFINITION("CREATE TABLE IF NOT EXISTS prisoners (prisoner_uuid char(36), jailer_uuid char(36), jail_name varchar(255), punishment int, remaining int, reason varchar(255), inventory text, PRIMARY_KEY(prisoner_uuid))"),
    FETCH_PRISONER_FROM_UUID("SELECT * FROM prisoners WHERE uuid=?"),
    FETCH_JAILS("SELECT * FROM jails"),
    FETCH_PRISONER_UUIDS("SELECT prisoner_uuid FROM prisoners"),
    TICK_PRISONER("UPDATE TABLE prisoners SET remaining=remaining-60 WHERE prisoner_uuid=?"),
    ADD_JAIL("INSERT INTO jails VALUES(?, ?, ?, ?, ?, ?)"),
    REMOVE_JAIL("DELETE FROM jails WHERE name=?"),
    ADD_PRISONER("INSERT INTO prisoners VALUES(?, ?, ?, ?, ?, ?, ?)");
    public String query;
    JailSQLQueries(String query) {
        this.query = query;
    }
}
