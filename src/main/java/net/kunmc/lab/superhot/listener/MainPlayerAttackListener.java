package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MainPlayerAttackListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }

        Player damager = ((Player) e.getDamager());
        if (!damager.getUniqueId().equals(manager.getMainPlayerUUID())) {
            return;
        }

        manager.advanceTime(2);
    }
}
