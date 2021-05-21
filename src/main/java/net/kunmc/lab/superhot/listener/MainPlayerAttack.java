package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.Superhot;
import net.kunmc.lab.superhot.SuperhotState;
import net.kunmc.lab.superhot.state.Attacking;
import net.kunmc.lab.superhot.state.Moving;
import net.kunmc.lab.superhot.state.Stopping;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MainPlayerAttack implements Listener {
    GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player damager = ((Player) e.getDamager());
        if (!damager.getUniqueId().equals(SuperhotState.mainPlayerUUID)) return;

        manager.changeState(new Attacking());
        manager.updateAllEntities();

        new BukkitRunnable() {
            @Override
            public void run() {
                manager.changeState(SuperhotState.isMainPlayerMoving ? new Moving() : new Stopping());
                manager.updateAllEntities();
            }
        }.runTaskLater(Superhot.getInstance(), 2);
    }
}
