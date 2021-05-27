package net.kunmc.lab.superhot.helper;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemHelper {
    public static ItemStack createItem(Material material, Component displayName, int amount) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        item.setItemMeta(meta);
        item.setAmount(amount);
        return item;
    }
}
