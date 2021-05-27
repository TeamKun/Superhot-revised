package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockChangeListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent e) {
        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onNotePlay(NotePlayEvent e) {
        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent e) {
        if (!manager.isStateMoving()) {
            e.setCancelled(true);
        }
    }
}
