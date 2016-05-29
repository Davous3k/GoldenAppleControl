package net.terrocidepvp.goldenapplecontrol.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.terrocidepvp.goldenapplecontrol.Main;
import net.terrocidepvp.goldenapplecontrol.utils.ColorCodeUtil;

// CommandExecutor ensures that commands carry over from the main class.
public class CommandManager implements CommandExecutor {
    // What to do when a player types a command.
    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        // Initialise the prefix.
        final String prefix = ColorCodeUtil.translateAlternateColorCodes('&',
                Main.getInstance().getConfig().getString("plugin-messages.prefix"));
        // Initialise the no permission message.
        final String noPerms = ColorCodeUtil.translateAlternateColorCodes('&',
                Main.getInstance().getConfig().getString("plugin-messages.no-permission"));
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
}