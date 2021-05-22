package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.Superhot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Attacking implements IState {
    @Override
    public void updateEntity(Entity entity) {
        entity.setGravity(true);
        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
        }

        if (entity instanceof Player) {
            Player p = ((Player) entity);
            p.setAllowFlight(false);
            p.setWalkSpeed(0.2F);
            p.setFlySpeed(0.1F);
        }

        if (entity.hasMetadata(Superhot.METADATAKEY)) {
            entity.setGravity(false);
        }
    }
}
