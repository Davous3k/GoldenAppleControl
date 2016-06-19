package net.terrocidepvp.goldenapplecontrol.listeners;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import net.terrocidepvp.goldenapplecontrol.handlers.ConsumptionControl;
import net.terrocidepvp.goldenapplecontrol.handlers.CoolDown;
import net.terrocidepvp.goldenapplecontrol.utils.CooldownUtil;
import net.terrocidepvp.goldenapplecontrol.utils.ListUtil;
import net.terrocidepvp.goldenapplecontrol.utils.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
                        // If perm node is null or it isn't null but the player has perms.
                && ((item.getPermissionNode() == null)
                        || (item.getPermissionNode() != null && player.hasPermission(item.getPermissionNode())))).forEach(item -> {

            Optional<CoolDown> coolDown = Optional.ofNullable(item.getCoolDown());
            if (coolDown.isPresent()
                    && !player.hasPermission("goldenapplecontrol.bypass")
                    && !ListUtil.doesListContain(plugin.getBlockedWorlds(), player.getWorld().getName())) {

                Optional<Map<UUID, Long>> cooldowns = Optional.ofNullable(coolDown.get().getCooldowns());
                if (cooldowns.isPresent()) {
                    boolean formattedTime = coolDown.get().isUseFormattedTime();
                    double duration = CooldownUtil.getCooldown(cooldowns.get(), player.getUniqueId());
                    if (duration != 0) {
                        coolDown.get().getCooldownMsg().forEach(str -> player.sendMessage(str.replace("%TIME%", TimeUtil.formatTime(formattedTime, duration))));
                        event.setCancelled(true);
                        return;
                    } else {
                        CooldownUtil.setCooldown(cooldowns.get(), player.getUniqueId(), coolDown.get().getDuration());
                        coolDown.get().getConsumeMsg().forEach(str -> player.sendMessage(str.replace("%TIME%", TimeUtil.formatTime(formattedTime, coolDown.get().getDuration()))));
                        if (coolDown.get().isUseExpiredMsg()) {
                            plugin.getServer().getScheduler().runTaskLater(plugin, () -> coolDown.get().getExpiredMsg().forEach(player::sendMessage), coolDown.get().getDuration() * 20);
                        }
                    }
                }
            }

            Optional<ConsumptionControl> consumptionControl = Optional.ofNullable(item.getConsumptionControl());
            if (consumptionControl.isPresent()) {
                event.setCancelled(true);

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
                if (plugin.getServerVersion()[0] == 1
                        && plugin.getServerVersion()[1] >= 9) {
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
                if (itemInHand.getAmount() > 1) {
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                } else {
                    if (plugin.getServerVersion()[0] == 1
                            && plugin.getServerVersion()[1] >= 9) {
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

                // Apply (or remove) potion effects to the player.
                for (String potionEffects : consumptionControl.get().getEffects()) {
                    String inf[] = potionEffects.split(":");
                    if (inf.length == 3) {
                        Optional<PotionEffectType> type = Optional.ofNullable(PotionEffectType.getByName(inf[0].toUpperCase()));
                        if (type.isPresent()) {
                            int duration;
                            try {
                                duration = Math.min(Integer.parseInt(inf[1]), Integer.MAX_VALUE);
                            } catch (NumberFormatException e) {
                                duration = Integer.MAX_VALUE;
                            }
                            int amplifier;
                            try {
                                amplifier = Math.min(Integer.parseInt(inf[2]), 255);
                            } catch (NumberFormatException e) {
                                amplifier = 255;
                            }
                            if (!player.hasPotionEffect(type.get())) {
                                PotionEffect effect = new PotionEffect(type.get(), duration, amplifier);
                                player.addPotionEffect(effect);
                            } else {
                                player.removePotionEffect(type.get());
                                PotionEffect effect = new PotionEffect(type.get(), duration, amplifier);
                                player.addPotionEffect(effect);
                            }
                        }
                    }
                }

            }
        });
    }

}
