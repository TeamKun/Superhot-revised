package net.kunmc.lab.superhot.state;

import net.kunmc.lab.superhot.Const;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

public class Attacking extends AbstractState {
    @Override
    public void updateEntity(Entity entity) {
        entity.setGravity(true);
        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
            AttributeInstance attr = living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if (attr != null) attr.removeModifier(Const.DECELERATION);
        }

        if (entity instanceof Projectile) {
            Projectile projectile = ((Projectile) entity);
        }

        if (entity instanceof Item) {
            Item item = ((Item) entity);
        }
    }
}
