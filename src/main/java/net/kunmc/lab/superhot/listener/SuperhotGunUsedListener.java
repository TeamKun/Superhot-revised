package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.Const;
import net.kunmc.lab.superhot.Superhot;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SuperhotGunUsedListener implements Listener {
    @EventHandler
    public void onUseSuperhotGun(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action action = e.getAction();
        if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_AIR)) {
            return;
        }

        ItemStack item = e.getItem();
        if (item == null) return;
        if (!item.getType().equals(Const.gunMaterial) || !Objects.equals(e.getItem().getItemMeta().displayName(), Const.gunName)) {
            return;
        }

        Inventory inventory = p.getInventory();
        Map<Integer, ItemStack> ammoMap = inventory.all(Const.ammoMaterial).entrySet().stream()
                .filter(x -> Objects.equals(x.getValue().getItemMeta().displayName(), Const.ammoName))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (ammoMap.isEmpty()) {
            return;
        }
        ItemStack ammo = ammoMap.values().stream().findFirst().get();
        ammo.setAmount(ammo.getAmount() - 1);

        Snowball bullet = p.launchProjectile(Snowball.class);
        bullet.setMetadata(Const.bulletMeta, new FixedMetadataValue(Superhot.getInstance(), null));
        bullet.setGravity(false);

        //50m飛んだら弾を削除する
        Location launchedLoc = p.getLocation();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (bullet.getLocation().distance(launchedLoc) > 50.0) {
                    bullet.remove();
                }
            }
        }.runTaskTimer(Superhot.getInstance(), 0, 20);
    }
}
