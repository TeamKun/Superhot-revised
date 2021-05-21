package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.Superhot;
import net.kunmc.lab.superhot.state.Attacking;
import net.kunmc.lab.superhot.state.Moving;
import net.kunmc.lab.superhot.state.Stopping;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProjectileHitListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        manager.changeState(new Attacking());
        manager.updateAllEntities();

        new BukkitRunnable() {
            @Override
            public void run() {
                manager.changeState(manager.isMainPlayerMoving() ? new Moving() : new Stopping());
                manager.updateAllEntities();
            }
        }.runTaskLater(Superhot.getInstance(), 2);
    }
}
