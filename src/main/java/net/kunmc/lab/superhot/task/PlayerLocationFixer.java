package net.kunmc.lab.superhot.task;

import net.kunmc.lab.superhot.SuperhotState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerLocationFixer extends BukkitRunnable {
    UUID uuid;
    Location loc;

    public PlayerLocationFixer(UUID uuid, Location loc) {
        this.uuid = uuid;
        this.loc = loc;
    }

    @Override
    public void run() {
        if (SuperhotState.isMainPlayerMoving) return;
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) return;
        p.teleport(loc);
    }
}
