package net.kunmc.lab.superhot.task;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.SuperhotState;
import net.kunmc.lab.superhot.state.Moving;
import net.kunmc.lab.superhot.state.Stopping;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MainPlayerMoveObserver extends BukkitRunnable {
    Location lastLoc;
    GameManager manager = GameManager.getInstance();

    @Override
    public void run() {
        Player mainPlayer = Bukkit.getPlayer(SuperhotState.mainPlayerUUID);
        if (mainPlayer == null) return;
        Location loc = mainPlayer.getLocation();
        if (lastLoc == null) lastLoc = loc;

        double distance = loc.distance(lastLoc);
        boolean isMainPlayerMoving = distance >= 0.05;
        if (SuperhotState.isMainPlayerMoving ^ isMainPlayerMoving) {
            manager.changeState(isMainPlayerMoving ? new Moving() : new Stopping());
            manager.updateAllEntities();
        }
        SuperhotState.isMainPlayerMoving = isMainPlayerMoving;
        lastLoc = loc;
    }
}
