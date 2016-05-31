package net.terrocidepvp.goldenapplecontrol.commands;

import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import net.terrocidepvp.goldenapplecontrol.hooks.ClipPAPIHook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    private ClipPAPIHook clipPAPIHook = GoldenAppleControl.getInstance().getClipPAPIHook();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            if (sender instanceof Player) {
                if (sender.hasPermission("goldenapplecontrol.use")) {
                    if (clipPAPIHook != null) {
                        clipPAPIHook.replacePlaceholders((Player) sender, GoldenAppleControl.getInstance().getRemainingTime()).forEach(sender::sendMessage);
                    } else {
                        sender.sendMessage("Clip's PlaceholderAPI is required for this command to work!");
                    }
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
