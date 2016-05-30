package net.terrocidepvp.goldenapplecontrol;

import net.terrocidepvp.goldenapplecontrol.commands.CommandManager;
import net.terrocidepvp.goldenapplecontrol.handlers.ItemManager;
import net.terrocidepvp.goldenapplecontrol.hooks.MVdWPlaceholderHook;
import net.terrocidepvp.goldenapplecontrol.listeners.ConsumeListener;
import net.terrocidepvp.goldenapplecontrol.listeners.EnchantedGoldenAppleConsumeListener;
import net.terrocidepvp.goldenapplecontrol.listeners.GoldenAppleConsumeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldenAppleControl extends JavaPlugin {

    private static GoldenAppleControl instance;
    public static GoldenAppleControl getInstance() {
        return instance;
    }

    private double serverVersion;
    private ItemManager itemManager;
    private CommandManager commandManager;

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

        int configVersion = 8;
        /*
         Updated for: Full recode
         Release: v1.8.1
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

        serverVersion = getMCVersion();
        getLogger().info("Running Bukkit version " + serverVersion);

        new GoldenAppleConsumeListener(this);
        new EnchantedGoldenAppleConsumeListener(this);
        new ConsumeListener(this);

        if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")
                && getConfig().getBoolean("hooks.mvdw-placeholderapi")) {
            getLogger().info("Attempting to apply custom placeholders...");
            new MVdWPlaceholderHook(this).hook();
        }

        commandManager = new CommandManager();
        getCommand("gapple").setExecutor(commandManager);
        getCommand("goldenapplecontrol").setExecutor(commandManager);
        getCommand("gac").setExecutor(commandManager);
        getCommand("goldenapple").setExecutor(commandManager);
        getCommand("gapplecooldown").setExecutor(commandManager);
        getCommand("gcd").setExecutor(commandManager);

        itemManager = ItemManager.createItemManager(this);
    }

    private double getMCVersion() {
        String version = new String(Bukkit.getVersion());
        int pos = version.indexOf("(MC: ");
        version = version.substring(pos + 5).replace(")", "");
        String[] splitVersion = version.split("\\.");
        return Double.parseDouble(splitVersion[0] + "." + splitVersion[1]);
    }

    public double getServerVersion() {
        return serverVersion;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
