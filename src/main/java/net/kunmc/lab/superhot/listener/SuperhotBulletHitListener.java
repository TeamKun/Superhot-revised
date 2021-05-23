package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.Superhot;
import net.kunmc.lab.superhot.state.Attacking;
import net.kunmc.lab.superhot.state.Moving;
import net.kunmc.lab.superhot.state.Stopping;
import net.kunmc.lab.superhot.util.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SuperhotBulletHitListener implements Listener {
    GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onBulletHit(ProjectileHitEvent e) {
        if (!e.getEntity().hasMetadata(Superhot.METADATAKEY)) {
            return;
        }

        Entity hitEntity = e.getHitEntity();
        if (!(hitEntity instanceof LivingEntity)) {
            return;
        }

        if (hitEntity instanceof Player && Utils.isCreativeOrAdventure(((Player) e.getHitEntity()))) {
            return;
        }

        ((LivingEntity) hitEntity).setHealth(0.0);

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
