package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (e.getPlayer().getUniqueId().equals(manager.getMainPlayerUUID())) return;
        manager.restoreEntityState(e.getPlayer());
    }
}
