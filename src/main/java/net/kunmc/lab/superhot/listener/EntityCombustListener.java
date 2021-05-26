package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;

public class EntityCombustListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCombust(EntityCombustEvent e) {
        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCombustByEntity(EntityCombustByEntityEvent e) {
        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCombustByBlock(EntityCombustByBlockEvent e) {
        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }
}
