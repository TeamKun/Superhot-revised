package net.kunmc.lab.superhot.listener;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import net.kunmc.lab.superhot.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class NotMainPlayerActListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onLaunchProjectile(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = ((Player) e.getEntity());
        if (p.getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }

        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChangeBlock(EntityChangeBlockEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = ((Player) e.getEntity());
        if (p.getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }

        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onReadyArrow(PlayerReadyArrowEvent e) {
        if (e.getPlayer().getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }

        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (e.getPlayer().getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }

        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }
}
