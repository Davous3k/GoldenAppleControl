package net.terrocidepvp.goldenapplecontrol.listeners;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import net.terrocidepvp.goldenapplecontrol.handlers.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ConsumeListener implements Listener {

    private final GoldenAppleControl plugin;

    public ConsumeListener(final GoldenAppleControl plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsume(final PlayerItemConsumeEvent event) {
        if (event.isCancelled()) return;

        // Get data from event.
        final Player player = event.getPlayer();
        final Material eventItemMaterial = event.getItem().getType();
        final short eventItemData = event.getItem().getDurability();

        for (final Item item : GoldenAppleControl.getInstance().getItemManager().getItems()) {
            // Item check.
            if (!(item.getMaterial() == eventItemMaterial
                    && item.getData() == eventItemData)) continue;

            // Permission node check.
            if (!(item.getPermissionNode() != null
                    && player.hasPermission(item.getPermissionNode()))) continue;

            if (item.getCoolDown() != null) {
                if (item.getCoolDown().getCooldowns().get(player.getUniqueId()) == null) {
                    // TODO set cooldown, consume message, schedule expiry message with runnable
                } else {
                    for (final String str : item.getCoolDown().getCooldownMsg()) {
                        // TODO replace variables like %TIME%
                        player.sendMessage(str);
                    }
                    event.setCancelled(true);
                }
            }

            // Check if consumption control is enabled.
            if (item.getConsumptionControl() != null) {
                // Cancel event to manually control the outcome.
                if (!event.isCancelled()) event.setCancelled(true);

                // Give the player the right amount of saturation.
                final float saturation = item.getConsumptionControl().getSaturation();
                if (saturation != 0.0d) {
                    if (!(player.getSaturation() >= 20) && !(player.getSaturation() + saturation >= 20)) {
                        player.setSaturation(player.getSaturation() + saturation);
                    } else {
                        player.setSaturation(20);
                    }
                }

                // Give the player the right amount of food levels.
                final int foodLevel = item.getConsumptionControl().getFoodLevel();
                if (!(player.getFoodLevel() >= 20) && !(player.getFoodLevel() + foodLevel >= 20)) {
                    player.setFoodLevel(player.getFoodLevel() + foodLevel);
                } else {
                    player.setFoodLevel(20);
                }

                // TODO all the shit for consumption control like deducting item, applying effects etc.

            }


            // No more item checks since it successfully executed for the correct item.
            break;
        }

    }
}
