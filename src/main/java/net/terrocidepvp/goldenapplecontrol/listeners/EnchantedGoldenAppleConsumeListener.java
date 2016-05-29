package net.terrocidepvp.goldenapplecontrol.listeners;

import java.util.List;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.terrocidepvp.goldenapplecontrol.handlers.EnchantedGoldenAppleCooldowns;
import net.terrocidepvp.goldenapplecontrol.handlers.EnchantedGoldenAppleEffects;
import net.terrocidepvp.goldenapplecontrol.utils.ColorCodeUtil;
import net.terrocidepvp.goldenapplecontrol.utils.TimeUtil;

public class EnchantedGoldenAppleConsumeListener implements Listener {
    // Make sure the plugin variable works.
    private final Plugin plugin;

    // Set up the listener.
    public EnchantedGoldenAppleConsumeListener(final Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    // Check on player item consume event.
    public void onPlayerConsumeItem(final PlayerItemConsumeEvent event) {
        // Set up the scheduler.
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        // Set up messages.
        final String prefix = ColorCodeUtil.translateAlternateColorCodes('&',
                GoldenAppleControl.getInstance().getConfig().getString("plugin-messages.prefix"));
        final List<String> enchantedGoldenAppleConsumeMessage = GoldenAppleControl.getInstance().getConfig()
                .getStringList("items.enchanted-golden-apple.cooldown.messages.consume");
        final List<String> enchantedGoldenAppleCooldownMessage = GoldenAppleControl.getInstance().getConfig()
                .getStringList("items.enchanted-golden-apple.cooldown.messages.cooldown");
        final List<String> enchantedGoldenAppleExpiredMessage = GoldenAppleControl.getInstance().getConfig()
                .getStringList("items.enchanted-golden-apple.cooldown.messages.expired");
        final Boolean enchantedGoldenAppleEnabled = GoldenAppleControl.getInstance().getConfig()
                .getBoolean("items.enchanted-golden-apple.cooldown.enabled");
        final Boolean enchantedGoldenAppleUseFormattedTime = GoldenAppleControl.getInstance().getConfig()
                .getBoolean("items.enchanted-golden-apple.cooldown.use-formatted-time");
        final Boolean enchantedGoldenAppleUseExpiredMessage = GoldenAppleControl.getInstance().getConfig()
                .getBoolean("items.enchanted-golden-apple.cooldown.use-expired-message");
        final Boolean enchantedGoldenAppleUseConsumptionControl = GoldenAppleControl.getInstance().getConfig()
                .getBoolean("items.enchanted-golden-apple.consumption-control.enabled");
        Long enchantedGoldenAppleDuration = GoldenAppleControl.getInstance().getConfig()
                .getLong("items.enchanted-golden-apple.cooldown.duration");
        // Initialise the golden apple ItemStack.
        final ItemStack enchantedGoldenApple = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);

        // Initialise the player as the person who triggered the event.
        final Player player = event.getPlayer();
        // Set inHand to the ItemStack that the player is holding.
        final ItemStack inHand = event.getItem();
        // If the material is an enchanted golden apple, do the following.
        if (inHand.getType().equals(Material.GOLDEN_APPLE)
                && inHand.getDurability() == enchantedGoldenApple.getDurability()) {
            // Check the player doesn't have a bypass perm.
            if (!player.hasPermission("goldenapplecontrol.bypass")) {
                if (enchantedGoldenAppleEnabled) {
                    // If the cooldown is active, do the following.
                    for (String everything : GoldenAppleControl.getInstance().getConfig().getKeys(true)) {
                        if (everything == null) {
                            continue;
                        }
                        if (everything.startsWith("permission-nodes.")) {
                            everything = everything.substring(17);
                            if (player.hasPermission("goldenapplecontrol." + everything)) {
                                if (GoldenAppleControl.getInstance().getConfig().get("permission-nodes." + everything
                                        + ".enchanted-golden-apple-duration") == null) {
                                    continue;
                                }
                                enchantedGoldenAppleDuration = GoldenAppleControl.getInstance().getConfig()
                                        .getLong("permission-nodes." + everything + ".enchanted-golden-apple-duration");
                                break;
                            }
                        }
                    }

                    if (EnchantedGoldenAppleCooldowns.getHandler()
                            .getEnchantedGoldenAppleCD(player.getUniqueId()) != 0.0D) {
                        // Adjust messages based on the formatted time boolean
                        // in the configuration file.
                        if (enchantedGoldenAppleUseFormattedTime) {
                            for (String str : enchantedGoldenAppleCooldownMessage) {
                                str = str.replaceAll("%TIME%", TimeUtil.formatTime(EnchantedGoldenAppleCooldowns
                                        .getHandler().getEnchantedGoldenAppleCD(player.getUniqueId())));
                                player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                            }
                        } else {
                            for (String str : enchantedGoldenAppleCooldownMessage) {
                                str = str.replaceAll("%TIME%", Double.toString(EnchantedGoldenAppleCooldowns
                                        .getHandler().getEnchantedGoldenAppleCD(player.getUniqueId())));
                                player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                            }
                        }

                        // Cancel the player consume event.
                        event.setCancelled(true);
                        // Return from this block of code.
                        return;
                    }

                    // Set the enchanted golden apple cooldown.
                    EnchantedGoldenAppleCooldowns.getHandler().setEnchantedGoldenAppleCD(player.getUniqueId(),
                            enchantedGoldenAppleDuration);
                    if (enchantedGoldenAppleUseFormattedTime) {
                        for (String str : enchantedGoldenAppleConsumeMessage) {
                            str = str.replaceAll("%TIME%", TimeUtil.formatTime(EnchantedGoldenAppleCooldowns
                                    .getHandler().getEnchantedGoldenAppleCD(player.getUniqueId())));
                            player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                        }
                    } else {
                        for (String str : enchantedGoldenAppleConsumeMessage) {
                            str = str.replaceAll("%TIME%", Double.toString(EnchantedGoldenAppleCooldowns.getHandler()
                                    .getEnchantedGoldenAppleCD(player.getUniqueId())));
                            player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                        }
                    }

                    // Call the custom effects handler.
                    if (enchantedGoldenAppleUseConsumptionControl) {
                        EnchantedGoldenAppleEffects.effectsHandler(event);
                    }

                    if (enchantedGoldenAppleUseExpiredMessage) {
                        // Set up the scheduler.
                        scheduler.runTaskLaterAsynchronously(plugin, new Runnable() {
                            @Override
                            public void run() {
                                for (final String str : enchantedGoldenAppleExpiredMessage) {
                                    // sendMessage is thread-safe.
                                    player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                                }
                            }
                            // Set the scheduled task to occur after the golden
                            // apple duration has passed.
                        }, enchantedGoldenAppleDuration * 20);
                    }

                } // Call the custom effects handler.
                else if (enchantedGoldenAppleUseConsumptionControl) {
                    EnchantedGoldenAppleEffects.effectsHandler(event);
                    return;
                }

            }
            // Call the custom effects handler.
            else if (enchantedGoldenAppleUseConsumptionControl) {
                EnchantedGoldenAppleEffects.effectsHandler(event);
                return;
            }
        }
    }
}
