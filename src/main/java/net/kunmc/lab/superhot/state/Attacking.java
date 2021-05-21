package net.kunmc.lab.superhot.state;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

public class Attacking extends AbstractState {
    @Override
    public void updateEntity(Entity entity) {
        entity.setGravity(true);
        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
        }

        if (entity instanceof Projectile) {
            Projectile projectile = ((Projectile) entity);
        }

        if (entity instanceof Item) {
            Item item = ((Item) entity);
        }
    }
}
