package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractState {
    protected final GameManager manager = GameManager.getInstance();

    public void updateAllEntities() {
        Player p = Bukkit.getPlayer(manager.getMainPlayerUUID());
        if (p == null) return;
        List<Entity> entityList = Bukkit.selectEntities(p, "@e");
        entityList.parallelStream().forEach(x -> {
            if (x.equals(p)) return;
            updateEntity(x);
        });
    }

    abstract public void updateEntity(Entity entity);
}
