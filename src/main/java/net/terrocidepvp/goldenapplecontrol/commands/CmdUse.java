package net.terrocidepvp.goldenapplecontrol.commands;

import java.util.List;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.terrocidepvp.goldenapplecontrol.handlers.EnchantedGoldenAppleCooldowns;
import net.terrocidepvp.goldenapplecontrol.handlers.GoldenAppleCooldowns;
import net.terrocidepvp.goldenapplecontrol.utils.ColorCodeUtil;
import net.terrocidepvp.goldenapplecontrol.utils.TimeUtil;

public class CmdUse {
    // Use command sender info in this method.
    public static boolean onUse(final CommandSender sender) {
        // Set up the prefix.
        final String prefix = ColorCodeUtil.translateAlternateColorCodes('&',
                GoldenAppleControl.getInstance().getConfig().getString("plugin-messages.prefix"));
        // Set up a List<String> for the RemainingTime message in the
        // configuration file.
        final List<String> remainingTime = GoldenAppleControl.getInstance().getConfig()
                .getStringList("plugin-messages.remaining-time");
        // Initialise the golden apple cooldown.
        final double goldenAppleCd = GoldenAppleCooldowns.getHandler()
                .getGoldenAppleCD(((Player) sender).getUniqueId());
        // Initialise the enchanted golden apple cooldown.
        final double enchantedGoldenAppleCd = EnchantedGoldenAppleCooldowns.getHandler()
                .getEnchantedGoldenAppleCD(((Player) sender).getUniqueId());
        // Check if the cooldowns haven't expired.
        if (goldenAppleCd == 0 & enchantedGoldenAppleCd == 0) {
            sender.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&',
                    GoldenAppleControl.getInstance().getConfig().getString("plugin-messages.no-active-cooldowns")));
            return true;
        }

        // Convert the cooldowns to their string equivalent.
        String goldenAppleCdString = TimeUtil
                .formatTime(formattedTime, GoldenAppleCooldowns.getHandler().getGoldenAppleCD(((Player) sender).getUniqueId()));
        String enchantedGoldenAppleCdString = TimeUtil.formatTime(
                formattedTime, EnchantedGoldenAppleCooldowns.getHandler().getEnchantedGoldenAppleCD(((Player) sender).getUniqueId()));
        // Replace the golden apple cooldown with the word "Inactive" if it's
        // 0.0 seconds.
        if (goldenAppleCd == 0.0D) {
            goldenAppleCdString = "&4Inactive";
        }
        // Replace the enchanted golden apple cooldown with the word "Inactive"
        // if it's 0.0 seconds.
        if (enchantedGoldenAppleCd == 0.0D) {
            enchantedGoldenAppleCdString = "&4Inactive";
        }

        // Go through the List<String> and replace it accordingly.
        for (final String str : remainingTime) {
            sender.sendMessage(prefix + ColorCodeUtil.translateAlternateColorCodes('&',
                    str.replaceAll("%GOLDENAPPLE%", goldenAppleCdString).replaceAll("%ENCHANTEDGOLDENAPPLE%",
                            enchantedGoldenAppleCdString)));
        }
        return true;
    }
}
