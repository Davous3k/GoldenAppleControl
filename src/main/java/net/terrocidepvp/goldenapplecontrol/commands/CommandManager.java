package net.terrocidepvp.goldenapplecontrol.commands;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            if (sender instanceof Player) {
                if (sender.hasPermission("goldenapplecontrol.use")) {
                    // TODO The use command to display any active cooldowns.
                    return true;
                } else {
                    sender.sendMessage(GoldenAppleControl.getInstance().getNoPerm());
                }
            } else {
                sender.sendMessage("This command can only be run by a player!");
            }
        }
        return false;
    }
}
