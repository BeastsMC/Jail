package com.BeastsMC.jail;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by Zane on 12/30/14.
 */
public class CommonFunctions {

    /**
     * Converts from a Bukkit Location to a String
     *
     * @param loc       the location to convert
     * @param precise   if true, all coordinates will be converted with decimals
     * @param direction if true, direction will be converted as well
     * @return str - the String representation of the input Location
     */
    public static String locationToString(Location loc, boolean precise, boolean direction) {
        StringBuilder builder = new StringBuilder();
        if (precise) {
            builder.append(loc.getX()).append(';');
            builder.append(loc.getY()).append(';');
            builder.append(loc.getZ()).append(';');
        } else {
            builder.append(loc.getBlockX()).append(';');
            builder.append(loc.getBlockY()).append(';');
            builder.append(loc.getBlockZ()).append(';');
        }
        if (direction) {
            builder.append(loc.getYaw()).append(';');
            builder.append(loc.getPitch()).append(';');
        }

        builder.append(loc.getWorld().getName());

        return builder.toString();

    }

    /**
     * Converts from a String into a Bukkit Location.
     *
     * @param strLoc    the string to convert
     * @param direction if true, will convert yaw/pitch as well
     * @return loc - the Location represented by the String
     */
    public static Location locationFromString(String strLoc, boolean direction) {
        //TODO Validate output is correct so we don't return a non-world Location
        double x, y, z;
        float yaw = 0, pitch = 0;
        int index = 0;
        String world;
        String[] exploded = strLoc.split(";");
        x = Double.parseDouble(exploded[index++]);
        y = Double.parseDouble(exploded[index++]);
        z = Double.parseDouble(exploded[index++]);
        if (direction) {
            yaw = Float.parseFloat(exploded[index++]);
            pitch = Float.parseFloat(exploded[index++]);
        }
        world = exploded[index];

        Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        return loc;

    }

    /**
     * Checks if a point is contained within the cuboid defined by the two supplied corners.
     *
     * @param corner1 the first corner of the cuboid
     * @param corner2 the second corner of the cuboid
     * @param point   the point to check if the cuboid contains
     * @return true is point is inside cuboid defined by the corners, else false
     */
    public static boolean locationBetweenLocations(Location corner1, Location corner2, Location point) {
        if (corner1.getWorld() != corner2.getWorld() || corner1.getWorld() != point.getWorld()) {
            return false;
        }

        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());

        return (point.getBlockX() >= minX && point.getBlockX() <= maxX) &&
                (point.getBlockY() >= minY && point.getBlockY() <= maxY) &&
                (point.getBlockZ() >= minZ && point.getBlockZ() <= maxZ);
    }


}
