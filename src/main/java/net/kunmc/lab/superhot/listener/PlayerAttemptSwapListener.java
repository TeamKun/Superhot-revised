package net.kunmc.lab.superhot.listener;

import net.kunmc.lab.superhot.GameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerAttemptSwapListener implements Listener {
    private final GameManager manager = GameManager.getInstance();
    private final Set<Material> transparentBlockSet;

    public PlayerAttemptSwapListener() {
        transparentBlockSet = new HashSet<>();
        transparentBlockSet.addAll(Arrays.asList(Material.AIR, Material.CAVE_AIR));
        transparentBlockSet.addAll(Arrays.stream(Material.values()).filter(x -> x.toString().contains("GLASS")).collect(Collectors.toList()));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().getType().equals(Material.NETHERITE_SCRAP)) return;

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!manager.getMainPlayerUUID().equals(uuid)) {
            return;
        }

        List<Block> blockList = p.getLineOfSight(transparentBlockSet, 32).stream()
                .filter(x -> x.getLocation().distance(p.getLocation()) > 4.0)
                .sorted(Comparator.comparing(b -> b.getLocation().distance(p.getLocation())))
                .collect(Collectors.toList());
        for (Block block : blockList) {
            Player target = block.getLocation().getNearbyPlayers(0.3).stream()
                    .filter(player -> !player.equals(p))
                    .findFirst()
                    .orElse(null);
            if (target != null) {
                Location mainPlayerLoc = p.getLocation();
                Inventory mainPlayerInv = p.getInventory();
                Location targetPlayerLoc = target.getLocation();
                Inventory targetPlayerInv = target.getInventory();

                manager.advanceTime(1);
                p.teleportAsync(targetPlayerLoc);
                target.teleportAsync(mainPlayerLoc);

                ItemStack[] mainPlayerContents = mainPlayerInv.getContents();
                ItemStack[] targetPlayerContents = targetPlayerInv.getContents();
                mainPlayerInv.setContents(targetPlayerContents);
                targetPlayerInv.setContents(mainPlayerContents);
                p.updateInventory();
                target.updateInventory();
                break;
            }
        }
    }
}
