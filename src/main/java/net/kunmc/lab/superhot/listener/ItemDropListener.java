package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.Superhot;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ItemDropListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (!e.getPlayer().getUniqueId().equals(manager.getMainPlayerUUID())) {
            e.setCancelled(true);
            return;
        }

        Item item = e.getItemDrop();
        item.setGravity(false);
        item.setVelocity(e.getPlayer().getLocation().getDirection().multiply(new Vector(0.7, 1, 0.7)));

        Location droppedLoc = item.getLocation();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (item.isOnGround() || item.getLocation().distance(droppedLoc) > 10.0) {
                    item.setGravity(true);
                    this.cancel();
                }

                item.getNearbyEntities(0.5, 0.6, 0.5).forEach(x -> {
                    if (x.getUniqueId().equals(item.getThrower())) {
                        return;
                    }

                    if (x instanceof LivingEntity) {
                        LivingEntity living = ((LivingEntity) x);
                        if (!living.isDead()) {
                            ((LivingEntity) x).damage(5);
                            item.setGravity(true);
                            this.cancel();
                        }
                    }
                });
            }
        }.runTaskTimer(Superhot.getInstance(), 0, 4);

        manager.advanceTime(2);
    }
}
