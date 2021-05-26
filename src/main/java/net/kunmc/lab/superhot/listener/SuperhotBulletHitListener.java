package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.Const;
import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.util.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SuperhotBulletHitListener implements Listener {
    private final GameManager manager = GameManager.getInstance();

    @EventHandler
    public void onBulletHit(ProjectileHitEvent e) {
        if (!e.getEntity().hasMetadata(Const.bulletMeta)) {
            return;
        }

        Entity hitEntity = e.getHitEntity();
        if (!(hitEntity instanceof LivingEntity)) {
            return;
        }

        if (hitEntity instanceof Player && Utils.isCreativeOrSpectator(((Player) e.getHitEntity()))) {
            return;
        }

        ((LivingEntity) hitEntity).setHealth(0.0);

        manager.advanceTime(2);
    }
}
