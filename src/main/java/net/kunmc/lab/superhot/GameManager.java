package net.kunmc.lab.superhot;

import net.kunmc.lab.superhot.event.StateChangeEvent;
import net.kunmc.lab.superhot.listener.*;
import net.kunmc.lab.superhot.state.AbstractState;
import net.kunmc.lab.superhot.state.Stopping;
import net.kunmc.lab.superhot.task.MainPlayerMoveObserver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class GameManager {
    private static final GameManager singleton = new GameManager();
    private BukkitTask moveObserverTask;
    private AbstractState state = new Stopping();

    private GameManager() {
    }

    public static GameManager getInstance() {
        return singleton;
    }

    public void start(Player target) {
        SuperhotState.mainPlayerUUID = target.getUniqueId();
        SuperhotState.isEnabled = true;
        SuperhotState.isMainPlayerMoving = true;

        JavaPlugin plugin = Superhot.getInstance();
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new EntitySpawn(), plugin);
        pluginManager.registerEvents(new MainPlayerAttack(), plugin);
        pluginManager.registerEvents(new PlayerJoin(), plugin);
        pluginManager.registerEvents(new PlayerLogout(), plugin);
        pluginManager.registerEvents(new StateChange(), plugin);
        pluginManager.registerEvents(new ProjectileHit(), plugin);
        pluginManager.registerEvents(new SuperhotBulletHit(), plugin);
        pluginManager.registerEvents(new UseSuperhotGun(), plugin);

        moveObserverTask = new MainPlayerMoveObserver().runTaskTimerAsynchronously(Superhot.getInstance(), 0, 0);
    }

    public void stop() {
        SuperhotState.mainPlayerUUID = null;
        SuperhotState.isEnabled = false;
        SuperhotState.isMainPlayerMoving = false;

        JavaPlugin plugin = Superhot.getInstance();
        EntitySpawnEvent.getHandlerList().unregister(plugin);
        EntityDamageByEntityEvent.getHandlerList().unregister(plugin);
        PlayerJoinEvent.getHandlerList().unregister(plugin);
        PlayerQuitEvent.getHandlerList().unregister(plugin);
        StateChangeEvent.getHandlerList().unregister(plugin);
        ProjectileHitEvent.getHandlerList().unregister(plugin);
        PlayerInteractEvent.getHandlerList().unregister(plugin);

        Bukkit.getScheduler().cancelTasks(Superhot.getInstance());
        Bukkit.selectEntities(Bukkit.getConsoleSender(), "@e").forEach(this::restoreEntityState);
    }

    public void changeState(AbstractState state) {
        this.state = state;
        Bukkit.getScheduler().runTask(Superhot.getInstance(), () ->
                Bukkit.getPluginManager().callEvent(new StateChangeEvent(state.getClass())));
    }

    public void restoreEntityState(Entity entity) {
        entity.setGravity(true);
        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
        }

        if (entity instanceof Player) {
            Player p = ((Player) entity);
            p.setWalkSpeed(0.2F);
            p.setFlySpeed(0.1F);
        }
    }

    public void updateAllEntities() {
        state.updateAllEntities();
    }

    public void updateEntity(Entity entity) {
        state.updateEntity(entity);
    }
}
