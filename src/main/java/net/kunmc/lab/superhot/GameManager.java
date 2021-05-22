package net.kunmc.lab.superhot;

import net.kunmc.lab.superhot.event.StateChangeEvent;
import net.kunmc.lab.superhot.listener.*;
import net.kunmc.lab.superhot.state.IState;
import net.kunmc.lab.superhot.state.Stopping;
import net.kunmc.lab.superhot.task.MainPlayerMoveObserver;
import net.kunmc.lab.superhot.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class GameManager {
    private UUID mainPlayerUUID;
    private boolean isSuperhotEnabled;
    private boolean isMainPlayerMoving;
    private IState state = new Stopping();
    private static final GameManager singleton = new GameManager();

    private GameManager() {
    }

    public static GameManager getInstance() {
        return singleton;
    }

    public void start(Player target) {
        mainPlayerUUID = target.getUniqueId();
        isSuperhotEnabled = true;
        isMainPlayerMoving = true;

        JavaPlugin plugin = Superhot.getInstance();
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new EntitySpawnListener(), plugin);
        pluginManager.registerEvents(new MainPlayerAttackListener(), plugin);
        pluginManager.registerEvents(new PlayerJoinListener(), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(), plugin);
        pluginManager.registerEvents(new StateChangeListener(), plugin);
        pluginManager.registerEvents(new ProjectileHitListener(), plugin);
        pluginManager.registerEvents(new SuperhotBulletHitListener(), plugin);
        pluginManager.registerEvents(new SuperhotGunUsedListener(), plugin);

        new MainPlayerMoveObserver()
                .runTaskTimerAsynchronously(Superhot.getInstance(), 0, 0);
    }

    public void stop() {
        mainPlayerUUID = null;
        isSuperhotEnabled = false;
        isMainPlayerMoving = false;

        JavaPlugin plugin = Superhot.getInstance();
        EntitySpawnEvent.getHandlerList().unregister(plugin);
        EntityDamageByEntityEvent.getHandlerList().unregister(plugin);
        PlayerJoinEvent.getHandlerList().unregister(plugin);
        PlayerQuitEvent.getHandlerList().unregister(plugin);
        StateChangeEvent.getHandlerList().unregister(plugin);
        ProjectileHitEvent.getHandlerList().unregister(plugin);
        PlayerInteractEvent.getHandlerList().unregister(plugin);
        PlayerRespawnEvent.getHandlerList().unregister(plugin);
        PlayerGameModeChangeEvent.getHandlerList().unregister(plugin);

        Bukkit.getScheduler().cancelTasks(Superhot.getInstance());
        Bukkit.selectEntities(Bukkit.getConsoleSender(), "@e").forEach(this::restoreEntityState);
    }

    public void changeState(IState state) {
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
            p.setAllowFlight(p.getGameMode().equals(GameMode.SPECTATOR) || p.getGameMode().equals(GameMode.CREATIVE));
            p.setWalkSpeed(0.2F);
            p.setFlySpeed(0.1F);
        }
    }

    public void updateAllEntities() {
        Player p = Bukkit.getPlayer(mainPlayerUUID);
        if (p == null) return;
        List<Entity> entityList = Bukkit.selectEntities(p, "@e");
        entityList.parallelStream().forEach(x -> {
            if (x.equals(p)) return;
            if (x instanceof Player)
                if (Utils.isCreativeOrAdventure(((Player) x))) return;

            updateEntity(x);
        });
    }

    public void updateEntity(Entity entity) {
        state.updateEntity(entity);
    }

    public boolean isMainPlayerMoving() {
        return isMainPlayerMoving;
    }

    public void setMainPlayerMoving(boolean isMainPlayerMoving) {
        this.isMainPlayerMoving = isMainPlayerMoving;
    }

    public boolean isSuperhotEnabled() {
        return isSuperhotEnabled;
    }

    public UUID getMainPlayerUUID() {
        return mainPlayerUUID;
    }

    public void setMainPlayerUUID(UUID uuid) {
        mainPlayerUUID = uuid;
    }
}
