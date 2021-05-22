package net.kunmc.lab.superhot.task;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.state.Moving;
import net.kunmc.lab.superhot.state.Stopping;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class MainPlayerMoveObserver extends BukkitRunnable {
    private Location lastLoc;
    private final GameManager manager = GameManager.getInstance();

    @Override
    public void run() {
        Player mainPlayer = Bukkit.getPlayer(manager.getMainPlayerUUID());
        if (mainPlayer == null) return;
        Location loc = mainPlayer.getLocation();
        if (lastLoc == null) lastLoc = loc;

        double distance = loc.distance(lastLoc);
        double threshold;
        Material material = Objects.requireNonNull(mainPlayer.getActiveItem()).getType();
        if (material.equals(Material.BOW) || material.equals(Material.CROSSBOW) || material.equals(Material.TRIDENT)) {
            threshold = 0.035;
        } else {
            threshold = 0.05;
        }
        mainPlayer.sendMessage(String.valueOf(distance));
        boolean isMainPlayerMoving = distance >= threshold;
        if (manager.isMainPlayerMoving() ^ isMainPlayerMoving) {
            manager.changeState(isMainPlayerMoving ? new Moving() : new Stopping());
            manager.updateAllEntities();
        }
        manager.setMainPlayerMoving(isMainPlayerMoving);
        lastLoc = loc;
    }
}
