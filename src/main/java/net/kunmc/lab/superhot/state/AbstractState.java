package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.SuperhotState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractState {
    public void updateAllEntities() {
        Player p = Bukkit.getPlayer(SuperhotState.mainPlayerUUID);
        if (p == null) return;
        List<Entity> entityList = Bukkit.selectEntities(p, "@e");
        entityList.parallelStream().forEach(x -> {
            if (x.equals(p)) return;
            updateEntity(x);
        });
    }

    abstract public void updateEntity(Entity entity);
}
