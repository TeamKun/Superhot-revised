package net.kunmc.lab.superhot.state;

import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class Moving extends AbstractState {
    private final EntityVelocityHolder holder = EntityVelocityHolder.getInstance();

    @Override
    public void updateEntity(Entity entity) {
        entity.setGravity(true);
        Vector velocity = holder.getVelocity(entity.getUniqueId());
        if (velocity != null) entity.setVelocity(velocity);

        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
        }

        if (entity instanceof Player) {
            Player p = ((Player) entity);
            p.setWalkSpeed(0.2F);
            p.setFlySpeed(0.1F);
        }

        if (entity instanceof Projectile) {
            Projectile projectile = ((Projectile) entity);
        }

        if (entity instanceof Item) {
            Item item = ((Item) entity);
        }
    }
}
