package net.terrocidepvp.goldenapplecontrol.handlers;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.terrocidepvp.goldenapplecontrol.Main;

public class GoldenAppleEffects {
    @SuppressWarnings("deprecation")
    public static void effectsHandler(final PlayerItemConsumeEvent event) {
        // Set up the config.
        final int foodLevel = Main.getInstance().getConfig()
                .getInt("items.golden-apple.consumption-control.food-level");
        final float saturation = Float
                .valueOf(Main.getInstance().getConfig().getString("items.golden-apple.consumption-control.saturation"));
        final List<String> effects = Main.getInstance().getConfig()
                .getStringList("items.golden-apple.consumption-control.effects");
        // Initialise variables.
        int duration;
        int amplifier;
        final ItemStack air = new ItemStack(Material.AIR, 1, (short) 0);

        // Set up a variable with player info.
        final Player player = event.getPlayer();

        // Simulate item consumption.
        event.setCancelled(true);

        final ItemStack itemInHand;
        final boolean itemInMainHand;
        if (!(Main.versionAsDouble <= 1.8)) {
            if (player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_APPLE) {
                itemInHand = player.getInventory().getItemInMainHand();
                itemInMainHand = true;
            } else {
                itemInHand = player.getInventory().getItemInOffHand();
                itemInMainHand = false;
            }
        } else {
            itemInHand = player.getInventory().getItemInHand();
            itemInMainHand = false;
        }

        // Set up metadata.
        final ItemMeta meta = itemInHand.getItemMeta();

        // Clear NBT data temporarily.
        itemInHand.setItemMeta(null);

        // Check if there's only one item because it can't use the setAmount()
        // thing to set it to zero.
        if (itemInHand.getAmount() == 1) {
            if (Main.versionAsDouble >= 1.9) {
                if (itemInMainHand) {
                    player.getInventory().setItemInMainHand(air);
                } else {
                    player.getInventory().setItemInOffHand(air);
                }
            } else {
                player.getInventory().setItemInHand(air);
            }
        } else {
            itemInHand.setAmount(itemInHand.getAmount() - 1);
        }

        // Restore NBT data after checks.
        itemInHand.setItemMeta(meta);

        // Give the player the right amount of saturation.
        if (!(player.getSaturation() >= 20) && !(player.getSaturation() + saturation >= 20)) {
            player.setSaturation(player.getSaturation() + saturation);
        } else {
            player.setSaturation(20);
        }

        // Give the player the right amount of food levels.
        if (!(player.getFoodLevel() >= 20) && !(player.getFoodLevel() + foodLevel >= 20)) {
            player.setFoodLevel(player.getFoodLevel() + foodLevel);
        } else {
            player.setFoodLevel(20);
        }

        // Iterate between the several effects in the config.
        for (final String potionEffects : effects) {
            // Split the potion effects between type, duration, and amplifier.
            final String inf[] = potionEffects.split(":");
            // Check that there is an argument for each part in the string.
            if (inf.length == 3) {
                // Check if the potion effect type is not null.
                if (PotionEffectType.getByName(inf[0].toUpperCase()) != null) {
                    // Set the potion effect type as a variable.
                    final PotionEffectType type = PotionEffectType.getByName(inf[0].toUpperCase());
                    // Check whether the duration is too high and amend it.
                    try {
                        duration = Math.min(Integer.parseInt(inf[1]), Integer.MAX_VALUE);
                    } catch (final NumberFormatException e) {
                        duration = Integer.MAX_VALUE;
                    }
                    // Check if the amplifier is too high and amend it.
                    try {
                        amplifier = Math.min(Integer.parseInt(inf[2]), 255);
                    } catch (final NumberFormatException e) {
                        amplifier = 255;
                    }
                    // If the player doesn't have any existing potion effects,
                    // apply it.
                    if (!player.hasPotionEffect(type)) {
                        final PotionEffect effect = new PotionEffect(type, duration, amplifier);
                        player.addPotionEffect(effect);
                    } else {
                        // If they do have the existing potion effects, remove
                        // it and apply the new one.
                        player.removePotionEffect(type);
                        final PotionEffect effect = new PotionEffect(type, duration, amplifier);
                        player.addPotionEffect(effect);
                    }
                }
            }
        }
    }
}
