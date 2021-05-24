package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.Const;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class Stopping implements IState {
    private final EntityVelocityHolder holder = EntityVelocityHolder.getInstance();

    @Override
    public void updateEntity(Entity entity) {
        Vector velocity = entity.getVelocity();
        if (!velocity.equals(new Vector())) {
            holder.storeVelocity(entity.getUniqueId(), entity.getVelocity());
            entity.setVelocity(new Vector());
        }

        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(false);
            living.setGravity(false);
            if (living instanceof Player) {
                Player p = ((Player) living);
                p.setAllowFlight(true);
                p.setWalkSpeed(0.0F);
                p.setFlySpeed(0.0F);
            }
            return;
        }

        if (entity.hasMetadata(Const.bulletMeta)) {
            return;
        }

        if (entity instanceof Projectile) {
            Projectile projectile = ((Projectile) entity);
            projectile.setGravity(false);
            return;
        }

        if (entity instanceof Item) {
            Item item = ((Item) entity);
            item.setCanMobPickup(false);
            item.setCanPlayerPickup(false);
            return;
        }

        entity.setGravity(false);
    }
}
