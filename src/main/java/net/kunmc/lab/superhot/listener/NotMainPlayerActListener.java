package net.kunmc.lab.superhot.listener;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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

        if (!manager.isStateMoving()) {
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

        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onReadyArrow(PlayerReadyArrowEvent e) {
        if (e.getPlayer().getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }

        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (e.getPlayer().getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }

        if (Utils.isCreativeOrSpectator(e.getPlayer())) {
            return;
        }

        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (e.getPlayer().getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }

        if (Utils.isCreativeOrSpectator(e.getPlayer())) {
            return;
        }

        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageBlock(BlockDamageEvent e) {
        if (e.getPlayer().getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }

        if (Utils.isCreativeOrSpectator(e.getPlayer())) {
            return;
        }

        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }
}
