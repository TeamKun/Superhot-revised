package net.kunmc.lab.superhot.helper;

import net.kunmc.lab.superhot.Const;
import net.kunmc.lab.superhot.Superhot;
import net.kunmc.lab.superhot.packet.CameraPacket;
import net.kunmc.lab.superhot.packet.InvisiblePacket;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class CameraHelper {
    public static Entity spawnCameraEntity(Location loc) {
        Zombie husk = ((Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE));
        husk.setInvisible(true);
        husk.setAdult();
        husk.setCollidable(false);
        husk.setAI(false);
        husk.setGravity(false);
        husk.setInvulnerable(true);
        husk.setVelocity(new Vector());
        husk.setSilent(true);
        husk.setCanPickupItems(false);
        husk.setShouldBurnInDay(false);
        husk.setMetadata(Const.cameraEntityMeta, new FixedMetadataValue(Superhot.getInstance(), null));
        return husk;
    }

    public static void makePlayerSpectateEntity(Player p, Entity entity) {
        new CameraPacket(entity).send(p);
        new InvisiblePacket(p, true).send(p);
        p.setMetadata(Const.spectatingMeta, new FixedMetadataValue(Superhot.getInstance(), null));
    }

    public static void makePlayerNotSpectateEntity(Player p) {
        new CameraPacket(p).send(p);
        new InvisiblePacket(p, false).send(p);
        p.removeMetadata(Const.spectatingMeta, Superhot.getInstance());
    }
}
