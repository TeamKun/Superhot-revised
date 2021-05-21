package net.kunmc.lab.superhot;

import net.kunmc.lab.superhot.state.AbstractState;
import net.kunmc.lab.superhot.state.Stopping;
import net.kunmc.lab.superhot.task.MainPlayerMoveObserver;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
        SuperhotState.isMainPlayerMoving = false;
        moveObserverTask = new MainPlayerMoveObserver().runTaskTimerAsynchronously(Superhot.getInstance(), 0, 0);
    }

    public void stop() {
        SuperhotState.mainPlayerUUID = null;
        SuperhotState.isEnabled = false;
        if (moveObserverTask != null) moveObserverTask.cancel();
    }

    public void changeState(AbstractState state) {
        this.state = state;
    }

    public void updateAllEntities() {
        state.updateAllEntities();
    }

    public void updateEntity(Entity entity) {
        state.updateEntity(entity);
    }
}
