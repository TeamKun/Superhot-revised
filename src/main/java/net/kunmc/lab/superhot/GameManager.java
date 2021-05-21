package net.kunmc.lab.superhot;

import net.kunmc.lab.superhot.state.AbstractState;
import net.kunmc.lab.superhot.state.Moving;
import net.kunmc.lab.superhot.task.MainPlayerMoveObserver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class GameManager {
    private static final GameManager singleton = new GameManager();
    private BukkitTask moveObserverTask;
    private AbstractState state = new Moving();

    private GameManager() {
    }

    public static GameManager getInstance() {
        return singleton;
    }

    public void start(Player target) {
        SuperhotState.mainPlayerUUID = target.getUniqueId();
        SuperhotState.isEnabled = true;
        SuperhotState.isMainPlayerMoving = true;
        moveObserverTask = new MainPlayerMoveObserver().runTaskTimerAsynchronously(Superhot.getInstance(), 0, 0);
    }

    public void stop() {
        SuperhotState.mainPlayerUUID = null;
        SuperhotState.isEnabled = false;
        if (moveObserverTask != null) moveObserverTask.cancel();
        Bukkit.selectEntities(Bukkit.getConsoleSender(), "@e").forEach(this::restoreEntityState);
    }

    public void changeState(AbstractState state) {
        this.state = state;
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
