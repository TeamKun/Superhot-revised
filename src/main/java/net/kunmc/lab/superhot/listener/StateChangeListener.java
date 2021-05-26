package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.Superhot;
import net.kunmc.lab.superhot.event.StateChangeEvent;
import net.kunmc.lab.superhot.state.Stopping;
import net.kunmc.lab.superhot.task.PlayerLocationFixer;
import net.kunmc.lab.superhot.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StateChangeListener implements Listener {
    private final Map<UUID, BukkitTask> playerLocationFixTasks = new HashMap<>();
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onStateChange(StateChangeEvent e) {
        playerLocationFixTasks.values().forEach(BukkitTask::cancel);
        if (e.getStateClass().equals(Stopping.class)) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                UUID uuid = p.getUniqueId();
                if (uuid.equals(manager.getMainPlayerUUID())) return;
                if (Utils.isCreativeOrSpectator(p)) return;

                BukkitTask task = new PlayerLocationFixer(uuid, p.getLocation()).runTaskTimer(Superhot.getInstance(), 0, 0);
                playerLocationFixTasks.put(uuid, task);
            });
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (Utils.isCreativeOrSpectator(p)) {
            return;
        }

        UUID uuid = p.getUniqueId();
        if (uuid.equals(manager.getMainPlayerUUID())) {
            return;
        }

        BukkitTask oldTask = playerLocationFixTasks.get(uuid);
        if (oldTask != null) {
            oldTask.cancel();
        }

        if (manager.isMovingState()) {
            return;
        }

        Location respawnLoc = p.getBedSpawnLocation();
        if (respawnLoc == null) {
            respawnLoc = p.getCompassTarget();
        }

        BukkitTask task = new PlayerLocationFixer(uuid, respawnLoc).runTaskTimer(Superhot.getInstance(), 0, 0);
        playerLocationFixTasks.put(uuid, task);

        if (!manager.isMovingState()) {
            p.setAllowFlight(true);
            p.setFlying(true);
        }
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent e) {
        GameMode mode = e.getNewGameMode();
        if (mode.equals(GameMode.CREATIVE) || mode.equals(GameMode.SPECTATOR)) {
            BukkitTask task = playerLocationFixTasks.get(e.getPlayer().getUniqueId());
            if (task != null) {
                task.cancel();
            }
            manager.restoreEntityState(e.getPlayer());
        } else {
            UUID uuid = e.getPlayer().getUniqueId();
            BukkitTask task = new PlayerLocationFixer(uuid, e.getPlayer().getLocation()).runTaskTimer(Superhot.getInstance(), 0, 0);
            playerLocationFixTasks.put(uuid, task);
        }
    }
}
