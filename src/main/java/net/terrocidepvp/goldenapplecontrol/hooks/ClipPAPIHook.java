package net.terrocidepvp.goldenapplecontrol.hooks;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import net.terrocidepvp.goldenapplecontrol.GoldenAppleControl;
import net.terrocidepvp.goldenapplecontrol.handlers.Item;
import net.terrocidepvp.goldenapplecontrol.utils.CooldownUtil;
import net.terrocidepvp.goldenapplecontrol.utils.TimeUtil;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ClipPAPIHook extends EZPlaceholderHook {

    private final GoldenAppleControl plugin;

    public ClipPAPIHook(final GoldenAppleControl plugin) {
        super(plugin, "goldenapplecontrol");
        this.plugin = plugin;
        plugin.getLogger().info("Hooked into Clip's PAPI.");
    }

    @Override
    public String onPlaceholderRequest(final Player p, final String identifier) {
        for (String placeholder : plugin.getPlaceholders()) {
            System.out.println(identifier + " " + placeholder);
            if (!placeholder.equals(identifier)) continue;
            for (Item item : plugin.getItemManager().getItems()) {
                Optional<String> itemPlaceholder = Optional.ofNullable(item.getPlaceholder());
                if (itemPlaceholder.isPresent()
                        && itemPlaceholder.get().equals(placeholder)) {
                    boolean formattedTime = item.getCoolDown().isUseFormattedTime();
                    double duration = CooldownUtil.getCooldown(item.getCoolDown().getCooldowns(), p.getUniqueId());
                    if (duration != 0) {
                        return TimeUtil.formatTime(formattedTime, duration);
                    } else {
                        return "&4Inactive";
                    }
                }
            }
        }
        return null;
    }
}
