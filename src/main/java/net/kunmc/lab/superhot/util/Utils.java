package net.kunmc.lab.superhot.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Utils {
    public static boolean isCreativeOrSpectator(Player p) {
        return p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR);
    }
}
