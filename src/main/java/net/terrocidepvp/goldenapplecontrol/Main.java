package net.terrocidepvp.goldenapplecontrol;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.terrocidepvp.goldenapplecontrol.commands.CommandManager;
import net.terrocidepvp.goldenapplecontrol.hooks.MVdWPlaceholderHook;
import net.terrocidepvp.goldenapplecontrol.listeners.EnchantedGoldenAppleConsumeListener;
import net.terrocidepvp.goldenapplecontrol.listeners.GoldenAppleConsumeListener;

public class Main extends JavaPlugin implements Listener {

    private static final Main instance = new Main();

    public static String version;
    public static double versionAsDouble;

    public static Main getInstance() {
        return instance;
    }

    public static String getMCVersion() {
        String version = new String(Bukkit.getVersion());
        final int pos = version.indexOf("(MC: ");
        version = version.substring(pos + 5).replace(")", "");
        return version;
    }

    private Main() {
    }

    @Override
    public void onDisable() {
        getLogger().info("All cooldowns are stored in a hashmap - it resets once the plugin is unloaded.");
        getLogger().info("Disabled!");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
        reloadConfig();
        // Check if the MVdWPlaceholderAPI plugin is present
        if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")
                && getConfig().getBoolean("hooks.mvdw-placeholderapi")) {
            getLogger().info("Attempting to apply custom placeholders...");
            MVdWPlaceholderHook.hook(this);
        }
        getCommand("gapple").setExecutor(new CommandManager());
        getCommand("goldenapplecontrol").setExecutor(new CommandManager());
        getCommand("gac").setExecutor(new CommandManager());
        getCommand("goldenapple").setExecutor(new CommandManager());
        getCommand("gapplecooldown").setExecutor(new CommandManager());
        getCommand("gcd").setExecutor(new CommandManager());
        getLogger().info("Checking if the config is broken...");
        if (!getConfig().isSet("config-version")) {
            getLogger().severe("The config.yml file is broken!");
            getLogger().severe("The plugin failed to detect a 'config-version'.");
            getLogger().severe(
                    "The plugin will not load until you generate a new, working config OR if you fix the config.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("The config is not broken.");
        getLogger().info("Checking if the config is outdated...");
        final int configVersion = 7;
        // Updated for: Flexible perms system introduction
        // Release: v1.6
        if (getConfig().getInt("config-version") != configVersion) {
            getLogger().severe("Your config is outdated!");
            getLogger()
                    .severe("The plugin will not load unless you change the config version to " + configVersion + ".");
            getLogger().severe(
                    "This means that you will need to reset your config, as there may have been major changes to the plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            getLogger().info("The config was not detected as outdated.");
        }
        version = getMCVersion();
        final String[] splitVersion = version.split("\\.");
        versionAsDouble = Double.parseDouble(splitVersion[0] + "." + splitVersion[1]);
        getLogger().info("Running Bukkit version " + version);
        getLogger().info("Loading item consume listener...");
        new GoldenAppleConsumeListener(this);
        new EnchantedGoldenAppleConsumeListener(this);
        getLogger().info("Enabled!");
    }
}