package net.terrocidepvp.goldenapplecontrol;

import net.terrocidepvp.goldenapplecontrol.commands.CommandManager;
import net.terrocidepvp.goldenapplecontrol.hooks.MVdWPlaceholderHook;
import net.terrocidepvp.goldenapplecontrol.listeners.EnchantedGoldenAppleConsumeListener;
import net.terrocidepvp.goldenapplecontrol.listeners.GoldenAppleConsumeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldenAppleControl extends JavaPlugin {

    private static GoldenAppleControl instance;
    public static GoldenAppleControl getInstance() {
        return instance;
    }

    public double serverVersion;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        // Create config if not already existing.
        saveDefaultConfig();
        reloadConfig();

        // Check if config is broken.
        if (!getConfig().isSet("config-version")) {
            getLogger().severe("The config.yml file is broken!");
            getLogger().severe("The plugin failed to detect a 'config-version'.");
            getLogger().severe("The plugin will not load until you generate a new, working config OR if you fix the config.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Outdated config check.
        final int configVersion = 7;
        /*
         Updated for: Flexible perms system introduction
         Release: v1.6
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

        // Get Minecraft server version.
        serverVersion = getMCVersion();
        getLogger().info("Running Bukkit version " + serverVersion);

        // Load listeners.
        new GoldenAppleConsumeListener(this);
        new EnchantedGoldenAppleConsumeListener(this);

        // Check if the MVdWPlaceholderAPI plugin is present.
        if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")
                && getConfig().getBoolean("hooks.mvdw-placeholderapi")) {
            getLogger().info("Attempting to apply custom placeholders...");
            new MVdWPlaceholderHook(this).hook();
        }

        // Register command executors.
        commandManager = new CommandManager();
        getCommand("gapple").setExecutor(commandManager);
        getCommand("goldenapplecontrol").setExecutor(commandManager);
        getCommand("gac").setExecutor(commandManager);
        getCommand("goldenapple").setExecutor(commandManager);
        getCommand("gapplecooldown").setExecutor(commandManager);
        getCommand("gcd").setExecutor(commandManager);
    }

    @Override
    public void onDisable() {
        commandManager = null;
        // TODO Nullify the objects you're gonna add in real soon.
    }

    private double getMCVersion() {
        // Get version from Bukkit.
        String version = new String(Bukkit.getVersion());
        final int pos = version.indexOf("(MC: ");
        // Clean it up to get the numbers.
        version = version.substring(pos + 5).replace(")", "");
        // Parse as a double.
        final String[] splitVersion = version.split("\\.");
        return Double.parseDouble(splitVersion[0] + "." + splitVersion[1]);
    }
}
