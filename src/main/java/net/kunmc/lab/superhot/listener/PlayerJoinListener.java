package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        manager.updateEntity(e.getPlayer());
    }
}
