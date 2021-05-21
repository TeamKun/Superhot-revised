package net.kunmc.lab.superhot.state;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class Attacking extends AbstractState {
    @Override
    public void updateEntity(Entity entity) {
        entity.setGravity(true);
        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
        }
    }
}
