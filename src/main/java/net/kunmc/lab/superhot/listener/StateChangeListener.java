package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.Superhot;
import net.kunmc.lab.superhot.event.StateChangeEvent;
import net.kunmc.lab.superhot.state.Attacking;
import net.kunmc.lab.superhot.state.Moving;
import net.kunmc.lab.superhot.state.Stopping;
import net.kunmc.lab.superhot.task.PlayerLocationFixer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StateChangeListener implements Listener {
    private final Map<UUID, BukkitTask> playerLocationFixTasks = new HashMap<>();
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onStateChange(StateChangeEvent e) {
        if (e.getStateClass().equals(Moving.class) || e.getStateClass().equals(Attacking.class)) {
            playerLocationFixTasks.values().forEach(BukkitTask::cancel);
        }

        if (e.getStateClass().equals(Stopping.class)) {
            Bukkit.getOnlinePlayers().stream().forEach(p -> {
                UUID uuid = p.getUniqueId();
                if (uuid.equals(manager.getMainPlayerUUID())) return;
                BukkitTask task = new PlayerLocationFixer(uuid, p.getLocation()).runTaskTimer(Superhot.getInstance(), 0, 0);
                playerLocationFixTasks.put(uuid, task);
            });
        }
    }
}
