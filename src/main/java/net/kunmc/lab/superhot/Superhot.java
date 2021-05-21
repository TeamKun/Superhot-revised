package net.kunmc.lab.superhot;

import net.kunmc.lab.superhot.command.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Superhot extends JavaPlugin {
    private static Superhot INSTANCE;
    public static final String METADATAKEY = "SuperhotMeta";

    public static Superhot getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        getServer().getPluginCommand("superhot").setExecutor(new CommandHandler());
        getServer().getPluginCommand("superhot").setTabCompleter(new CommandHandler());
    }

    @Override
    public void onDisable() {
        GameManager manager = GameManager.getInstance();
        List<Entity> entityList = Bukkit.selectEntities(Bukkit.getConsoleSender(), "@e");
        entityList.forEach(manager::restoreEntityState);
    }
}

