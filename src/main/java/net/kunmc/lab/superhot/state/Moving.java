package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.Const;
import net.kunmc.lab.superhot.helper.CameraHelper;
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

        if (entity.hasMetadata(Const.bulletMeta)) {
            return;
        }

        if (entity.hasMetadata(Const.cameraEntityMeta)) {
            entity.remove();
            return;
        }

        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
            living.setGravity(true);
            if (living instanceof Player) {
                Player p = ((Player) living);
                p.setAllowFlight(false);
                p.setFlying(false);
                p.setWalkSpeed(0.2F);
                p.setFlySpeed(0.1F);
                p.setGravity(true);
                CameraHelper.makePlayerNotSpectateEntity(p);
            }
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
            item.setPickupDelay(16);
            return;
        }

        entity.setGravity(true);
    }
}
