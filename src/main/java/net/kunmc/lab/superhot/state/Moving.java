package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.Const;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class Moving implements IState {
    private final EntityVelocityHolder holder = EntityVelocityHolder.getInstance();

    @Override
    public void updateEntity(Entity entity) {
        Vector velocity = holder.getVelocity(entity.getUniqueId());
        if (velocity != null) {
            entity.setVelocity(velocity);
        }

        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
            living.setGravity(true);
            if (living instanceof Player) {
                Player p = ((Player) living);
                p.setAllowFlight(false);
                p.setWalkSpeed(0.2F);
                p.setFlySpeed(0.1F);
                p.setGravity(true);
            }
            return;
        }

        if (entity.hasMetadata(Const.bulletMeta)) {
            return;
        }

        if (entity instanceof Projectile) {
            Projectile projectile = ((Projectile) entity);
            projectile.setGravity(true);
            return;
        }

        if (entity instanceof Item) {
            Item item = ((Item) entity);
            item.setCanMobPickup(true);
            item.setCanPlayerPickup(true);
            item.setPickupDelay(8);
            return;
        }
        
        entity.setGravity(true);
    }
}
