package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLogout implements Listener {
    @EventHandler
    public void onLogout(PlayerQuitEvent e) {
        GameManager.getInstance().restoreEntityState(e.getPlayer());
    }
}
