package net.terrocidepvp.goldenapplecontrol.hooks;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import net.terrocidepvp.goldenapplecontrol.Main;
import net.terrocidepvp.goldenapplecontrol.handlers.EnchantedGoldenAppleCooldowns;
import net.terrocidepvp.goldenapplecontrol.handlers.GoldenAppleCooldowns;
import net.terrocidepvp.goldenapplecontrol.utils.TimeUtil;

public class MVdWPlaceholderHook
{
    public static void hook(Main plugin)
    {
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
                            final String goldenAppleCdString = TimeUtil.formatTime(GoldenAppleCooldowns.getHandler().getGoldenAppleCD(player.getUniqueId()));
                            return goldenAppleCdString;
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
                            final String enchantedGoldenAppleCdString = TimeUtil.formatTime(EnchantedGoldenAppleCooldowns.getHandler().getEnchantedGoldenAppleCD(player.getUniqueId()));
                            return enchantedGoldenAppleCdString;
                        }
                        return "&4Inactive";
                    }
                });
    }
}
