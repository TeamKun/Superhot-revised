package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileLaunchEventListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        manager.advanceTime(2);
    }
}
