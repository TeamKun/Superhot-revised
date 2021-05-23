package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.Superhot;
import net.kunmc.lab.superhot.util.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SuperhotBulletHitListener implements Listener {
    @EventHandler
    public void onBulletHit(ProjectileHitEvent e) {
        if (!e.getEntity().hasMetadata(Superhot.METADATAKEY)) {
            return;
        }

        Entity hitEntity = e.getHitEntity();
        if (!(hitEntity instanceof LivingEntity)) {
            return;
        }
        
        if (hitEntity instanceof Player && Utils.isCreativeOrAdventure(((Player) e.getHitEntity()))) {
            return;
        }

        ((LivingEntity) hitEntity).setHealth(0.0);
    }
}
