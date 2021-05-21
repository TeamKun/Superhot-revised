package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.Superhot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntitySpawnListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                manager.updateEntity(e.getEntity());
            }
        }.runTaskLater(Superhot.getInstance(), 2);
    }
}
