package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.NotePlayEvent;

public class BlockChangeListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent e) {
        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onNotePlay(NotePlayEvent e) {
        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent e) {
        if (!manager.isMovingState()) {
            e.setCancelled(true);
        }
    }
}
