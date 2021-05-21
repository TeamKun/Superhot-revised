package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.Const;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class Stopping extends AbstractState {
    private final EntityVelocityHolder holder = EntityVelocityHolder.getInstance();

    @Override
    public void updateEntity(Entity entity) {
        entity.setGravity(false);
        holder.storeVelocity(entity.getUniqueId(), entity.getVelocity());
        entity.setVelocity(new Vector());
        
        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(false);
            AttributeInstance attr = living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            boolean hasDeceleration = attr.getModifiers().contains(Const.DECELERATION);
            if (!hasDeceleration) attr.addModifier(Const.DECELERATION);
        }
    }
}
