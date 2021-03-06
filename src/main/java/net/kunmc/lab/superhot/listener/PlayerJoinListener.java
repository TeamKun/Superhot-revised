package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }
        if (Utils.isCreativeOrSpectator(e.getPlayer())) {
            return;
        }

        Player p = e.getPlayer();
        manager.addToTeam(p);
        manager.updateEntity(p);
    }
}
