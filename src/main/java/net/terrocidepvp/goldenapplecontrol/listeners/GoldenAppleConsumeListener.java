package net.terrocidepvp.goldenapplecontrol.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.terrocidepvp.goldenapplecontrol.Main;
import net.terrocidepvp.goldenapplecontrol.handlers.GoldenAppleCooldowns;
import net.terrocidepvp.goldenapplecontrol.handlers.GoldenAppleEffects;
import net.terrocidepvp.goldenapplecontrol.utils.ColorCodeUtil;
import net.terrocidepvp.goldenapplecontrol.utils.TimeUtil;

public class GoldenAppleConsumeListener implements Listener {
    // Make sure the plugin variable works.
    private final Plugin plugin;

    // Set up the listener.
    public GoldenAppleConsumeListener(final Plugin plugin) {
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
                Main.getInstance().getConfig().getString("plugin-messages.prefix"));
        final List<String> goldenAppleConsumeMessage = Main.getInstance().getConfig()
                .getStringList("items.golden-apple.cooldown.messages.consume");
        final List<String> goldenAppleCooldownMessage = Main.getInstance().getConfig()
                .getStringList("items.golden-apple.cooldown.messages.cooldown");
        final List<String> goldenAppleExpiredMessage = Main.getInstance().getConfig()
                .getStringList("items.golden-apple.cooldown.messages.expired");
        final Boolean goldenAppleEnabled = Main.getInstance().getConfig()
                .getBoolean("items.golden-apple.cooldown.enabled");
        final Boolean goldenAppleUseFormattedTime = Main.getInstance().getConfig()
                .getBoolean("items.golden-apple.cooldown.use-formatted-time");
        final Boolean goldenAppleUseExpiredMessage = Main.getInstance().getConfig()
                .getBoolean("items.golden-apple.cooldown.use-expired-message");
        final Boolean goldenAppleUseConsumptionControl = Main.getInstance().getConfig()
                .getBoolean("items.golden-apple.consumption-control.enabled");
        Long goldenAppleDuration = Main.getInstance().getConfig().getLong("items.golden-apple.cooldown.duration");
        // Initialise the golden apple ItemStack.
        final ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 0);

        // Initialise the player as the person who triggered the event.
        final Player player = event.getPlayer();
        // Set inHand to the ItemStack that the player is holding.
        final ItemStack inHand = event.getItem();
        // If the material is a golden apple, do the following.
        if (inHand.getType().equals(Material.GOLDEN_APPLE) && inHand.getDurability() == goldenApple.getDurability()) {
            // Check the player doesn't have a bypass perm.
            if (!player.hasPermission("goldenapplecontrol.bypass")) {
                if (goldenAppleEnabled) {
                    // If the cooldown is active, do the following.
                    for (String everything : Main.getInstance().getConfig().getKeys(true)) {
                        if (everything == null) {
                            continue;
                        }
                        if (everything.startsWith("permission-nodes.")) {
                            everything = everything.substring(17);
                            if (player.hasPermission("goldenapplecontrol." + everything)) {
                                if (Main.getInstance().getConfig()
                                        .get("permission-nodes." + everything + ".golden-apple-duration") == null) {
                                    continue;
                                }
                                goldenAppleDuration = Main.getInstance().getConfig()
                                        .getLong("permission-nodes." + everything + ".golden-apple-duration");
                                break;
                            }
                        }
                    }

                    if (GoldenAppleCooldowns.getHandler().getGoldenAppleCD(player.getUniqueId()) != 0.0D) {
                        // Adjust messages based on the formatted time boolean
                        // in the configuration file.
                        if (goldenAppleUseFormattedTime) {
                            for (String str : goldenAppleCooldownMessage) {
                                str = str.replaceAll("%TIME%", TimeUtil.formatTime(
                                        GoldenAppleCooldowns.getHandler().getGoldenAppleCD(player.getUniqueId())));
                                player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                            }
                        } else {
                            for (String str : goldenAppleCooldownMessage) {
                                str = str.replaceAll("%TIME%", Double.toString(
                                        GoldenAppleCooldowns.getHandler().getGoldenAppleCD(player.getUniqueId())));
                                player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                            }
                        }

                        // Cancel the player consume event.
                        event.setCancelled(true);
                        // Return from this block of code.
                        return;
                    }

                    // Set the golden apple cooldown.
                    GoldenAppleCooldowns.getHandler().setGoldenAppleCD(player.getUniqueId(), goldenAppleDuration);
                    if (goldenAppleUseFormattedTime) {
                        for (String str : goldenAppleConsumeMessage) {
                            str = str.replaceAll("%TIME%", TimeUtil.formatTime(
                                    GoldenAppleCooldowns.getHandler().getGoldenAppleCD(player.getUniqueId())));
                            player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                        }
                    } else {
                        for (String str : goldenAppleConsumeMessage) {
                            str = str.replaceAll("%TIME%", Double.toString(
                                    GoldenAppleCooldowns.getHandler().getGoldenAppleCD(player.getUniqueId())));
                            player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                        }
                    }

                    // Call the custom effects handler.
                    if (goldenAppleUseConsumptionControl) {
                        GoldenAppleEffects.effectsHandler(event);
                    }

                    if (goldenAppleUseExpiredMessage) {
                        // Set up the scheduler.
                        scheduler.runTaskLaterAsynchronously(plugin, new Runnable() {
                            @Override
                            public void run() {
                                for (final String str : goldenAppleExpiredMessage) {
                                    // sendMessage is thread-safe.
                                    player.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&', str));
                                }
                            }
                            // Set the scheduled task to occur after the golden
                            // apple duration has passed.
                        }, goldenAppleDuration * 20);
                    }

                } // Call the custom effects handler.
                else if (goldenAppleUseConsumptionControl) {
                    GoldenAppleEffects.effectsHandler(event);
                    return;
                }

            }
            // Call the custom effects handler.
            else if (goldenAppleUseConsumptionControl) {
                GoldenAppleEffects.effectsHandler(event);
                return;
            }
        }
    }
}