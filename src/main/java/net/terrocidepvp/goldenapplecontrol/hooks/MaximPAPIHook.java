package net.terrocidepvp.goldenapplecontrol.hooks;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import net.terrocidepvp.goldenapplecontrol.handlers.Item;
import net.terrocidepvp.goldenapplecontrol.utils.CooldownUtil;
import net.terrocidepvp.goldenapplecontrol.utils.TimeUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

public class MaximPAPIHook {

    public static void hook(final GoldenAppleControl plugin) {
        for (String placeholder : plugin.getPlaceholders()) {
            PlaceholderAPI.registerPlaceholder(plugin, "goldenapplecontrol_" + placeholder, event -> {
                final Player player = event.getPlayer();
                final OfflinePlayer offlinePlayer = event.getPlayer();
                if (offlinePlayer == null) {
                    return "Player needed!";
                }

                for (Item item : plugin.getItemManager().getItems()) {
                    Optional<String> itemPlaceholder = Optional.ofNullable(item.getPlaceholder());
                    if (itemPlaceholder.isPresent()
                            && itemPlaceholder.get().equals(placeholder)) {
                        boolean formattedTime = item.getCoolDown().isUseFormattedTime();
                        double duration = CooldownUtil.getCooldown(item.getCoolDown().getCooldowns(), player.getUniqueId());
                        if (duration != 0) {
                            return TimeUtil.formatTime(formattedTime, duration);
                        } else {
                            return "&4Inactive";
                        }
                    }
                }
                return "&4Unavailable";
            });
        }
        plugin.getLogger().info("Registered placeholders for Maxim's PAPI.");
    }

}
