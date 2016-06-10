package net.terrocidepvp.goldenapplecontrol;

import net.terrocidepvp.goldenapplecontrol.commands.CommandManager;
import net.terrocidepvp.goldenapplecontrol.handlers.ItemManager;
import net.terrocidepvp.goldenapplecontrol.hooks.ClipPAPIHook;
import net.terrocidepvp.goldenapplecontrol.hooks.MaximPAPIHook;
import net.terrocidepvp.goldenapplecontrol.listeners.ConsumeListener;
import net.terrocidepvp.goldenapplecontrol.utils.ColorCodeUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GoldenAppleControl extends JavaPlugin {

    private static GoldenAppleControl instance;
    public static GoldenAppleControl getInstance() {
        return instance;
    }

    private double serverVersion;
    private ItemManager itemManager;
    private ClipPAPIHook clipPAPIHook;

    private String noPerm;
    private String inactiveMsg;
    private List<String> remainingTime;
    private List<String> blockedWorlds;
    private List<String> placeholders = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        if (!getConfig().isSet("config-version")) {
            getLogger().severe("The config.yml file is broken!");
            getLogger().severe("The plugin failed to detect a 'config-version'.");
            getLogger().severe("The plugin will not load until you generate a new, working config OR if you fix the config.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        int configVersion = 9;
        /*
         Updated for: World bypass and Inactive string for placeholders
         Release: v2.0.3
        */
        if (getConfig().getInt("config-version") != configVersion) {
            getLogger().severe("Your config is outdated!");
            getLogger()
                    .severe("The plugin will not load unless you change the config version to " + configVersion + ".");
            getLogger().severe(
                    "This means that you will need to reset your config, as there may have been major changes to the plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Loading values from config...");
        noPerm = ColorCodeUtil.translate(getConfig().getString("plugin-messages.no-permission"));
        inactiveMsg = getConfig().getString("plugin-messages.inactive-cooldown");
        remainingTime = ColorCodeUtil.translate(getConfig().getStringList("plugin-messages.remaining-time"));
        blockedWorlds = getConfig().getStringList("ignore-cooldowns-in-these-worlds");
        itemManager = ItemManager.createItemManager(this);

        serverVersion = getMCVersion();
        getLogger().info("Running server version " + serverVersion);

        if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            getLogger().info("Attempting to register placeholders for Maxim's PAPI...");
            MaximPAPIHook.hook(this);
        }
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Attempting to hook into Clip's PAPI...");
            clipPAPIHook = new ClipPAPIHook(this);
            clipPAPIHook.hook();
        }

        CommandManager commandManager = new CommandManager();
        getCommand("gapple").setExecutor(commandManager);
        getCommand("goldenapplecontrol").setExecutor(commandManager);
        getCommand("gac").setExecutor(commandManager);
        getCommand("goldenapple").setExecutor(commandManager);
        getCommand("gapplecooldown").setExecutor(commandManager);
        getCommand("gcd").setExecutor(commandManager);

        new ConsumeListener(this);
    }

    private double getMCVersion() {
        String version = Bukkit.getVersion();
        int pos = version.indexOf("(MC: ");
        String newVersion = version.substring(pos + 5).replace(")", "");
        String[] splitVersion = newVersion.split("\\.");
        return Double.parseDouble(splitVersion[0] + "." + splitVersion[1]);
    }

    public double getServerVersion() {
        return serverVersion;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public String getNoPerm() {
        return noPerm;
    }

    public List<String> getRemainingTime() {
        return remainingTime;
    }

    public List<String> getPlaceholders() {
        return placeholders;
    }

    public ClipPAPIHook getClipPAPIHook() {
        return clipPAPIHook;
    }

    public String getInactiveMsg() {
        return inactiveMsg;
    }

    public List<String> getBlockedWorlds() {
        return blockedWorlds;
    }
}
