package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent e) {
        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }
}
