package net.terrocidepvp.goldenapplecontrol.commands;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.terrocidepvp.goldenapplecontrol.utils.ColorCodeUtil;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

        final String prefix = ColorCodeUtil.translateAlternateColorCodes('&', GoldenAppleControl.getInstance().getConfig().getString("plugin-messages.prefix"));
        final String noPerms = ColorCodeUtil.translateAlternateColorCodes('&', GoldenAppleControl.getInstance().getConfig().getString("plugin-messages.no-permission"));
        // Checks for the right commands in case something happens.
        if (!cmd.getName().equalsIgnoreCase("gapple") && !cmd.getName().equalsIgnoreCase("goldenapplecontrol")
                && !cmd.getName().equalsIgnoreCase("gac") && !cmd.getName().equalsIgnoreCase("goldenapple")
                && !cmd.getName().equalsIgnoreCase("gapplecontrol") && !cmd.getName().equalsIgnoreCase("gcd"))
            return false;
        // Check for an args length that is greater than or equal to 1.
        if (args.length >= 1) {
            // Ensure the first letter is extracted.
            final String firstLetterFromArg = args[0].substring(0, 1);
            // Make sure the argument starts with the letter "r".
            if (firstLetterFromArg.equalsIgnoreCase("r")) {
                // Check for a permission node.
                if (sender.hasPermission("goldenapplecontrol.reload")) {
                    // Call CmdReload.
                    CmdReload.onReload(sender);
                } else {
                    // Output the no permission message otherwise.
                    sender.sendMessage(prefix + noPerms);
                }
            }
        }
        // Carry out the rest of the task if there are no arguments.
        if (args.length == 0) {
            if (sender instanceof Player) {
                // Check for a permission node.
                if (sender.hasPermission("goldenapplecontrol.use")) {
                    // Call CmdUse.
                    CmdUse.onUse(sender);
                } else {
                    // Output the no permission message otherwise.
                    sender.sendMessage(prefix + noPerms);
                }
            } else {
                sender.sendMessage("This command can only be run by a player!");
            }
        }
        return false;
    }

    public static boolean onReload(final CommandSender sender) {
        final String prefix = ColorCodeUtil.translateAlternateColorCodes('&',
                GoldenAppleControl.getInstance().getConfig().getString("plugin-messages.prefix"));
        sender.sendMessage(prefix + ChatColor.GRAY + "Attempting to reload config...");
        GoldenAppleControl.getInstance().reloadConfig();
        return true;
    }
}
