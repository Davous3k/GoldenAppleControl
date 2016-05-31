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
            Material material;
            try {
                if (confMaterial.isPresent()) {
                    material = Material.valueOf(confMaterial.get().toUpperCase().replaceAll("[^A-Z0-9_]", "_"));
                } else {
                    plugin.getLogger().severe("Null (non-existent) material for 'items." + str + ".material'! " +
                            "Skipping this item.");
                    return;
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().severe("Invalid material (" + confMaterial + ") for 'items." + str + ".material'! " +
                        "Skipping this item.");
                return;
            }

            int confData = plugin.getConfig().getInt("items." + str + ".data", 0);

            // Construct CoolDown object.
            Optional<CoolDown> coolDown = Optional.empty();
            if (plugin.getConfig().getBoolean("items." + str + ".cooldown.enabled", false)) {
                coolDown = Optional.of(new CoolDown(plugin.getConfig().getLong("items." + str + ".cooldown.duration"),
                        plugin.getConfig().getBoolean("items." + str + ".cooldown.use-formatted-time"),
                        plugin.getConfig().getBoolean("items." + str + ".cooldown.use-expired-message"),
                        ColorCodeUtil.translate(plugin.getConfig().getStringList("items." + str + ".cooldown.consume-message")),
                        ColorCodeUtil.translate(plugin.getConfig().getStringList("items." + str + ".cooldown.cooldown-message")),
                        ColorCodeUtil.translate(plugin.getConfig().getStringList("items." + str + ".cooldown.expired-message"))));
            }

            if (coolDown.isPresent()
                    && (plugin.getConfig().getBoolean("items." + str + "placeholder.enabled", false))) {
                Optional<String> registerThis = Optional.ofNullable(plugin.getConfig().getString("items." + str + "placeholder.register-this"));
                if (registerThis.isPresent()) {
                    // TODO register for clip (store in set/list) and register for maxim (static should do)
                } else {
                    plugin.getLogger().severe("We won't register a placeholder because you haven't specified a placeholder to register at 'items." + str + ".placeholder.register-this'!");
                }
            } else {
                plugin.getLogger().severe("Silly you! We won't register a placeholder because your item 'items." + str + ".placeholder' has no cooldown!");
            }

            Optional<String> permissionNode = Optional.ofNullable(plugin.getConfig().getString("items." + str + ".permission"));

            // Construct ConsumptionControl object.
            Optional<ConsumptionControl> consumptionControl = Optional.empty();
            if (plugin.getConfig().getBoolean("items." + str + ".consumption-control.enabled", false)) {
                consumptionControl = Optional.of(new ConsumptionControl(plugin.getConfig().getInt("items." + str + ".consumption-control.food-level", 0),
                        (float) plugin.getConfig().getDouble("items." + str + ".consumption-control.saturation", 0.0),
                        plugin.getConfig().getStringList("items." + str + ".consumption-control.effects")));
            }

            // Add the item to the set.
            items.add(new Item(str, material, confData, permissionNode.orElse(null), coolDown.orElse(null), consumptionControl.orElse(null)));
        });
    }

    public static ItemManager createItemManager(GoldenAppleControl plugin) {
        return new ItemManager(plugin);
    }

    public Set<Item> getItems() {
        return items;
    }

}
