package com.BeastsMC.jail;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    /**
     * Converts the player inventory to a String array of Base64 strings. First string is the content and second string is the armor.
     *
     * @param playerInventory to turn into an array of strings.
     * @return Array of strings: [ main content, armor content ]
     * @throws IllegalStateException
     */
    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());
        return new String[]{content, armor};
    }

    /**
     * A method to serialize an {@link ItemStack} array to Base64 String.
     * <p/>
     * <p/>
     * <p/>
     * Based off of {@link #toBase64(Inventory)}.
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     * @throws IllegalStateException
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * A method to serialize an inventory to Base64 string.
     * <p/>
     * <p/>
     * <p/>
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     * <p/>
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param inventory to serialize
     * @return Base64 string of the provided inventory
     * @throws IllegalStateException
     */
    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * A method to get an {@link Inventory} from an encoded, Base64, string.
     * <p/>
     * <p/>
     * <p/>
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     * <p/>
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param data Base64 string of data containing an inventory.
     * @return Inventory created from the Base64 string.
     * @throws IOException
     */
    public static Inventory fromBase64(String data) throws IOException {
        if (data.isEmpty()) return Bukkit.getServer().createInventory(null, 0);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    /**
     * Gets an array of ItemStacks from Base64 string.
     * <p/>
     * <p/>
     * <p/>
     * Base off of {@link #fromBase64(String)}.
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        if (data.isEmpty()) return new ItemStack[]{};
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static void restoreInventory(Player player, Prisoner prisoner) {
        try {
            Inventory content = fromBase64(prisoner.getInventory());
            ItemStack[] armor = itemStackArrayFromBase64(prisoner.getArmor());
            for (ItemStack item : armor) {
                if (item == null)
                    continue;
                else if (item.getType().toString().toLowerCase().contains("helmet"))
                    player.getInventory().setHelmet(item);
                else if (item.getType().toString().toLowerCase().contains("chestplate"))
                    player.getInventory().setChestplate(item);
                else if (item.getType().toString().toLowerCase().contains("leg"))
                    player.getInventory().setLeggings(item);
                else if (item.getType().toString().toLowerCase().contains("boots"))
                    player.getInventory().setBoots(item);
                else if (player.getInventory().firstEmpty() == -1)
                    player.getWorld().dropItem(player.getLocation(), item);
                else
                    player.getInventory().addItem(item);
            }
            for (ItemStack item : content.getContents()) {
                if (item == null) continue;
                else if (player.getInventory().firstEmpty() == -1)
                    player.getWorld().dropItem(player.getLocation(), item);
                else
                    player.getInventory().addItem(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Unable to restore " + player.getName() + "'s inventory.");
        }
    }


}
