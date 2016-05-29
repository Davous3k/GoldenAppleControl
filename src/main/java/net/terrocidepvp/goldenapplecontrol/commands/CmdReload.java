package net.terrocidepvp.goldenapplecontrol.commands;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.terrocidepvp.goldenapplecontrol.utils.ColorCodeUtil;

public class CmdReload {
    public static boolean onReload(final CommandSender sender) {
        final String prefix = ColorCodeUtil.translateAlternateColorCodes('&',
                GoldenAppleControl.getInstance().getConfig().getString("plugin-messages.prefix"));
        sender.sendMessage(prefix + ChatColor.GRAY + "Attempting to reload config...");
        GoldenAppleControl.getInstance().reloadConfig();
        return true;
    }
}
