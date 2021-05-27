package net.kunmc.lab.superhot.task;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerLocationFixer extends BukkitRunnable {
    private final UUID uuid;
    private final Location loc;
    private final GameManager manager = GameManager.getInstance();

    public PlayerLocationFixer(UUID uuid, Location loc) {
        this.uuid = uuid;
        this.loc = loc;
    }

    @Override
    public void run() {
        if (manager.isStateMoving()) {
            return;
        }

        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            p.teleportAsync(loc);
        }
    }
}
