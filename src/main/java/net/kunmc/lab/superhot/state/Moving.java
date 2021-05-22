package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.Superhot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class Moving extends AbstractState {
    private final EntityVelocityHolder holder = EntityVelocityHolder.getInstance();

    @Override
    public void updateEntity(Entity entity) {
        entity.setGravity(true);
        Vector velocity = holder.getVelocity(entity);
        if (velocity != null) entity.setVelocity(velocity);

        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
        }

        if (entity instanceof Player) {
            Player p = ((Player) entity);
            p.setAllowFlight(false);
            p.setWalkSpeed(0.2F);
            p.setFlySpeed(0.1F);
        }

        if (entity.hasMetadata(Superhot.METADATAKEY)) {
            entity.setGravity(false);
        }

        //bulletがたまに途中で止まるので暫定的処置
        if (entity instanceof Snowball && entity.getVelocity().distance(new Vector()) == 0.0) {
            entity.remove();
        }
    }
}
