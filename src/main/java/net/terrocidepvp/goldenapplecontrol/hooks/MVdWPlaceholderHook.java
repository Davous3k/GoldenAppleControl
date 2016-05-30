package net.terrocidepvp.goldenapplecontrol.hooks;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import net.terrocidepvp.goldenapplecontrol.utils.TimeUtil;

public class MVdWPlaceholderHook {

    private final GoldenAppleControl plugin;

    public MVdWPlaceholderHook(final GoldenAppleControl plugin) {
        this.plugin = plugin;
    }

    public void hook() {
        PlaceholderAPI.registerPlaceholder(plugin, "goldenapplecooldown",
                new PlaceholderReplacer() {
                    @Override
                    public String onPlaceholderReplace(
                            PlaceholderReplaceEvent event) {
                        // The player if he is online
                        // NULL: If the player is not online
                        final Player player = event.getPlayer();
                        // The offline player if he is not online
                        // NULL: If the placeholder is requested without
                        // a player (like the console) DO CHECKS YOURSELF
                        final OfflinePlayer offlinePlayer = event.getPlayer();

                        if (offlinePlayer == null) {
                            return "Player needed!";
                        }

                        final double goldenAppleCd = GoldenAppleCooldowns.getHandler().getGoldenAppleCD(player.getUniqueId());
                        if (!(goldenAppleCd == 0.0D)) {
                            return TimeUtil.formatTime(formattedTime, GoldenAppleCooldowns.getHandler().getGoldenAppleCD(player.getUniqueId()));
                        }
                        return "&4Inactive";
                    }
                });
        PlaceholderAPI.registerPlaceholder(plugin, "enchantedgoldenapplecooldown",
                new PlaceholderReplacer() {
                    @Override
                    public String onPlaceholderReplace(
                            PlaceholderReplaceEvent event) {
                        // The player if he is online
                        // NULL: If the player is not online
                        final Player player = event.getPlayer();
                        // The offline player if he is not online
                        // NULL: If the placeholder is requested without
                        // a player (like the console) DO CHECKS YOURSELF
                        final OfflinePlayer offlinePlayer = event.getPlayer();

                        if (offlinePlayer == null) {
                            return "Player needed!";
                        }

                        final double enchantedGoldenAppleCd = EnchantedGoldenAppleCooldowns.getHandler().getEnchantedGoldenAppleCD(player.getUniqueId());
                        if (!(enchantedGoldenAppleCd == 0.0D)) {
                            return TimeUtil.formatTime(formattedTime, EnchantedGoldenAppleCooldowns.getHandler().getEnchantedGoldenAppleCD(player.getUniqueId()));
                        }
                        return "&4Inactive";
                    }
                });
    }
}
