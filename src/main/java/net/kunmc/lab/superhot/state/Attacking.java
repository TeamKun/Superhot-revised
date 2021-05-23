package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.Superhot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Attacking implements IState {
    EntityVelocityHolder holder = EntityVelocityHolder.getInstance();

    @Override
    public void updateEntity(Entity entity) {
        entity.setGravity(true);
        Vector velocity = holder.getVelocity(entity.getUniqueId());
        if (velocity != null) {
            entity.setVelocity(velocity);
        }

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

        if (entity instanceof Item) {
            Item item = ((Item) entity);
            item.setCanMobPickup(true);
            item.setCanPlayerPickup(true);
            item.setPickupDelay(8);
        }

        if (entity.hasMetadata(Superhot.METADATAKEY)) {
            entity.setGravity(false);
        }
    }
}
