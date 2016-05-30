package net.terrocidepvp.goldenapplecontrol.handlers;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class ItemManager {

    private final GoldenAppleControl plugin;
    private final Set<Item> items = new HashSet<>();

    public ItemManager(final GoldenAppleControl plugin) {
        this.plugin = plugin;
        generateItems();
    }

    public void generateItems() {
        for (final String str : plugin.getConfig().getConfigurationSection("items").getKeys(false)) {

            // Get material.
            final String confMaterial = plugin.getConfig().getString("items." + str + ".material");
            if (confMaterial == null) {
                plugin.getLogger().severe("Null (empty) material for 'items." + str + ".material'! " +
                        "Skipping this item.");
                continue;
            }
            final Material material;
            try {
                material = Material.valueOf(confMaterial.toUpperCase().replaceAll("[^A-Z0-9_]", "_"));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().severe("Invalid material (" + confMaterial + ") for 'items." + str + ".material'! " +
                        "Skipping this item.");
                continue;
            }

            // Get data.
            final int confData = plugin.getConfig().getInt("items." + str + ".data");

            // Construct CoolDown object.
            final CoolDown coolDown;
            if (plugin.getConfig().getBoolean("items." + str + ".cooldown.enabled")) {
                coolDown = new CoolDown(plugin.getConfig().getDouble("items." + str + ".cooldown.duration"),
                                        plugin.getConfig().getBoolean("items." + str + ".use-formatted-time"),
                                        plugin.getConfig().getBoolean("items." + str + ".use-expired-message"),
                                        plugin.getConfig().getStringList("items." + str + ".consume-message"),
                                        plugin.getConfig().getStringList("items." + str + ".cooldown-message"),
                                        plugin.getConfig().getStringList("items." + str + ".expired-message"));
            } else {
                coolDown = null;
            }

            // Construct ConsumptionControl object.
            final ConsumptionControl consumptionControl;
            if (plugin.getConfig().getBoolean("items." + str + ".consumption-control.enabled")) {
                consumptionControl = new ConsumptionControl(plugin.getConfig().getInt("items." + str + ".consumption-control.food-level"),
                                                            plugin.getConfig().getDouble("items." + str + ".consumption-control.saturation"),
                                                            plugin.getConfig().getStringList("items." + str + ".consumption-control.effects"));
            } else {
                consumptionControl = null;
            }

            // Add the item to the set.
            items.add(new Item(material, confData, coolDown, consumptionControl));
        }
    }

    private boolean nullCheck(final String configSection, final String check, final Object data) {
        if (data == null) {
            plugin.getLogger().severe("Null (empty) material for 'items." + configSection + "." + check + "'! " +
                    "Skipping this item.");
            return true;
        } else {
            return false;
        }
    }

}
