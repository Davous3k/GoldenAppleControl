package net.terrocidepvp.goldenapplecontrol.listeners;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import net.terrocidepvp.goldenapplecontrol.handlers.ConsumptionControl;
import net.terrocidepvp.goldenapplecontrol.handlers.CoolDown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ConsumeListener implements Listener {

    private GoldenAppleControl plugin;

    public ConsumeListener(GoldenAppleControl plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Material eventItemMaterial = event.getItem().getType();
        short eventItemData = event.getItem().getDurability();

        GoldenAppleControl.getInstance().getItemManager().getItems().stream().filter(item ->
                (item.getMaterial() == eventItemMaterial && item.getData() == eventItemData)
                && ((item.getPermissionNode() == null)
                        || (item.getPermissionNode() != null && player.hasPermission(item.getPermissionNode())))).forEach(item -> {

            Optional<CoolDown> coolDown = Optional.ofNullable(item.getCoolDown());
            if (coolDown.isPresent()) {
                if (coolDown.get().getCooldowns().get(player.getUniqueId()) == null) {
                    // TODO Set cooldown, display consume message, schedule expiry message with runnable
                } else {
                    // TODO Replace variables like %TIME% in the message
                    coolDown.get().getCooldownMsg().forEach(player::sendMessage);
                    event.setCancelled(true);
                }
            }

            Optional<ConsumptionControl> consumptionControl = Optional.ofNullable(item.getConsumptionControl());
            if (consumptionControl.isPresent()) {
                if (!event.isCancelled()) event.setCancelled(true);

                float saturation = consumptionControl.get().getSaturation();
                if (saturation != 0.0d) {
                    if (!(player.getSaturation() >= 20) && !(player.getSaturation() + saturation >= 20)) {
                        player.setSaturation(player.getSaturation() + saturation);
                    } else {
                        player.setSaturation(20);
                    }
                }

                int foodLevel = consumptionControl.get().getFoodLevel();
                if (!(player.getFoodLevel() >= 20) && !(player.getFoodLevel() + foodLevel >= 20)) {
                    player.setFoodLevel(player.getFoodLevel() + foodLevel);
                } else {
                    player.setFoodLevel(20);
                }

                // Get correct held item based on server version.
                ItemStack itemInHand;
                boolean itemInMainHand;
                if (GoldenAppleControl.getInstance().getServerVersion() <= 1.9) {
                    if (player.getInventory().getItemInMainHand().getType() == item.getMaterial()) {
                        itemInHand = player.getInventory().getItemInMainHand();
                        itemInMainHand = true;
                    } else {
                        itemInHand = player.getInventory().getItemInOffHand();
                        itemInMainHand = false;
                    }
                } else {
                    //noinspection deprecation
                    itemInHand = player.getInventory().getItemInHand();
                    itemInMainHand = false;
                }

                // -1 the item count.
                if (itemInHand.getAmount() >= 1) {
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                } else {
                    if (GoldenAppleControl.getInstance().getServerVersion() <= 1.9) {
                        if (itemInMainHand) {
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                        } else {
                            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                        }
                    } else {
                        //noinspection deprecation
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                    }
                }


                // TODO Apply custom effects (if any) to the player

            }
        });

    }
}
