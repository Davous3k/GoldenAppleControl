package net.terrocidepvp.goldenapplecontrol.handlers;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import net.terrocidepvp.goldenapplecontrol.utils.ColorCodeUtil;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
            Optional<CoolDown> coolDown = Optional.empty();
            if (plugin.getConfig().getBoolean("items." + str + ".cooldown.enabled")) {
                coolDown = Optional.of(new CoolDown(plugin.getConfig().getLong("items." + str + ".cooldown.duration"),
                        plugin.getConfig().getBoolean("items." + str + ".cooldown.use-formatted-time"),
                        plugin.getConfig().getBoolean("items." + str + ".cooldown.use-expired-message"),
                        ColorCodeUtil.translate(plugin.getConfig().getStringList("items." + str + ".cooldown.consume-message")),
                        ColorCodeUtil.translate(plugin.getConfig().getStringList("items." + str + ".cooldown.cooldown-message")),
                        ColorCodeUtil.translate(plugin.getConfig().getStringList("items." + str + ".cooldown.expired-message"))));
            }

            Optional<String> permissionNode = Optional.ofNullable(plugin.getConfig().getString("items." + str + ".permission"));

            // Construct ConsumptionControl object.
            Optional<ConsumptionControl> consumptionControl = Optional.empty();
            if (plugin.getConfig().getBoolean("items." + str + ".consumption-control.enabled")) {
                consumptionControl = Optional.of(new ConsumptionControl(plugin.getConfig().getInt("items." + str + ".consumption-control.food-level"),
                        Float.valueOf(plugin.getConfig().getString("items." + str + ".consumption-control.saturation")),
                        plugin.getConfig().getStringList("items." + str + ".consumption-control.effects")));
            }

            // Add the item to the set.
            items.add(new Item(material, confData, permissionNode.orElseGet(null), coolDown.orElseGet(null), consumptionControl.orElseGet(null)));
        });
    }

    public static ItemManager createItemManager(GoldenAppleControl plugin) {
        return new ItemManager(plugin);
    }

    public Set<Item> getItems() {
        return items;
    }

}
