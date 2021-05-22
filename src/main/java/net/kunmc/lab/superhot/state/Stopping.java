package net.kunmc.lab.superhot.state;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Stopping implements IState {
    private final EntityVelocityHolder holder = EntityVelocityHolder.getInstance();

    @Override
    public void updateEntity(Entity entity) {
        entity.setGravity(false);
        holder.storeVelocity(entity, entity.getVelocity());
        entity.setVelocity(new Vector());

        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(false);
        }

        if (entity instanceof Player) {
            Player p = ((Player) entity);
            p.setAllowFlight(true);
            p.setWalkSpeed(0.0F);
            p.setFlySpeed(0.0F);
        }

        if (entity instanceof Item) {
            Item item = ((Item) entity);
            item.setCanMobPickup(false);
            item.setCanPlayerPickup(false);
        }
    }
}
