package com.BeastsMC.jail;

import org.bukkit.Location;

/**
 * Created by Zane on 1/2/15.
 */
public class Jail {
    private final Location corner1;
    private final Location corner2;
    private final Location telein;
    private final Location teleout;


    public Jail(Location corner1, Location corner2, Location telein, Location teleout) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.telein = telein;
        this.teleout = teleout;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public Location getTelein() {
        return telein;
    }

    public Location getTeleout() {
        return teleout;
    }

    public boolean contains(Location point) {
        return CommonFunctions.locationBetweenLocations(corner1, corner2, point);
    }
}
