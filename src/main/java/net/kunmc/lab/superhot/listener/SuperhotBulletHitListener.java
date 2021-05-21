package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.Superhot;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SuperhotBulletHitListener implements Listener {
    @EventHandler
    public void onBulletHit(ProjectileHitEvent e) {
        if (!e.getEntity().hasMetadata(Superhot.METADATAKEY)) return;
        if (!(e.getHitEntity() instanceof LivingEntity)) return;
        ((LivingEntity) e.getHitEntity()).setHealth(0.0);
    }
}
