package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.Superhot;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class UseSuperhotGun implements Listener {
    @EventHandler
    public void onUseSuperhotGun(PlayerInteractEvent e) {
        Action action = e.getAction();
        Material material = e.getMaterial();
        if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_AIR)) return;
        if (!material.equals(Material.NETHERITE_SCRAP)) return;

        Player p = e.getPlayer();
        Entity bullet = p.launchProjectile(Snowball.class);
        bullet.setMetadata(Superhot.METADATAKEY, new FixedMetadataValue(Superhot.getInstance(), null));
        bullet.setGravity(false);
    }
}
