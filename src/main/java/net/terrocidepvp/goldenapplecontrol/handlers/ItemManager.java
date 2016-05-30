package net.terrocidepvp.goldenapplecontrol.handlers;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.*;
import java.util.stream.Collectors;

public class ItemManager {

    private Set<Item> items = new HashSet<>();

    private ItemManager(GoldenAppleControl plugin) {
        plugin.getConfig().getConfigurationSection("items").getKeys(false).forEach(str -> {
            Optional<String> confMaterial = Optional.ofNullable(plugin.getConfig().getString("items." + str + ".material"));
            if (!confMaterial.isPresent()) {
                plugin.getLogger().severe("Null (empty) material for 'items." + str + ".material'! " +
                        "Skipping this item.");
                return;
            }
            Material material;
            try {
                material = Material.valueOf(confMaterial.get().toUpperCase().replaceAll("[^A-Z0-9_]", "_"));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().severe("Invalid material (" + confMaterial + ") for 'items." + str + ".material'! " +
                        "Skipping this item.");
                return;
            }

            int confData = plugin.getConfig().getInt("items." + str + ".data");

            // Construct CoolDown object.
            CoolDown coolDown = null;
            if (plugin.getConfig().getBoolean("items." + str + ".cooldown.enabled")) {
                coolDown = new CoolDown(plugin.getConfig().getDouble("items." + str + ".cooldown.duration"),
                        plugin.getConfig().getBoolean("items." + str + ".use-formatted-time"),
                        plugin.getConfig().getBoolean("items." + str + ".use-expired-message"),
                        t(plugin.getConfig().getStringList("items." + str + ".consume-message")),
                        t(plugin.getConfig().getStringList("items." + str + ".cooldown-message")),
                        t(plugin.getConfig().getStringList("items." + str + ".expired-message")));
            }

            String permissionNode = plugin.getConfig().getString("items." + str + ".permission");

            // Construct ConsumptionControl object.
            ConsumptionControl consumptionControl = null;
            if (plugin.getConfig().getBoolean("items." + str + ".consumption-control.enabled")) {
                consumptionControl = new ConsumptionControl(plugin.getConfig().getInt("items." + str + ".consumption-control.food-level"),
                        Float.valueOf(plugin.getConfig().getString("items." + str + ".consumption-control.saturation")),
                        plugin.getConfig().getStringList("items." + str + ".consumption-control.effects"));
            }

            // Add the item to the set.
            items.add(new Item(material, confData, permissionNode, coolDown, consumptionControl));
        });
    }

    public static ItemManager createItemManager(GoldenAppleControl plugin) {
        return new ItemManager(plugin);
    }

    public Set<Item> getItems() {
        return items;
    }

    private List<String> t(List<String> s) {
        return s.stream().map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toCollection(ArrayList::new));
    }

}
