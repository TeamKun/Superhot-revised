package net.kunmc.lab.superhot;

import net.kunmc.lab.superhot.command.CommandHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Superhot extends JavaPlugin {
    private static Superhot INSTANCE;

    public static Superhot getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();
        FileConfiguration config = getConfig();
        Config.ammoAmount = config.getInt("ammoAmount");
        Config.isGlowModeEnabled = config.getBoolean("GlowMode");

        getServer().getPluginCommand("superhot").setExecutor(new CommandHandler());
        getServer().getPluginCommand("superhot").setTabCompleter(new CommandHandler());
    }

    @Override
    public void onDisable() {
        GameManager manager = GameManager.getInstance();
        manager.stop();

        FileConfiguration config = getConfig();
        config.set("ammoAmount", Config.ammoAmount);
        config.set("GlowMode", Config.isGlowModeEnabled);
        saveConfig();
    }
}

