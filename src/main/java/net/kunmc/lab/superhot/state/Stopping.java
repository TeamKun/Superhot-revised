package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.Const;
import net.kunmc.lab.superhot.Superhot;
import net.kunmc.lab.superhot.helper.CameraHelper;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
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

        if (entity.hasMetadata(Const.bulletMeta) ||
                entity.hasMetadata(Const.spectatingMeta) ||
                entity.hasMetadata(Const.cameraEntityMeta)) {
            return;
        }

        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(false);
            living.setGravity(false);
            if (living instanceof Player) {
                Player p = ((Player) living);
                p.setAllowFlight(true);
                p.setFlying(true);
                p.setWalkSpeed(0.0F);
                p.setFlySpeed(0.0F);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Entity camera = CameraHelper.spawnCameraEntity(p.getLocation());
                        CameraHelper.makePlayerSpectateEntity(p, camera);
                    }
                }.runTask(Superhot.getInstance());
            }
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
