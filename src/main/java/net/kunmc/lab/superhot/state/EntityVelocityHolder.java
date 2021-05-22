package net.kunmc.lab.superhot.state;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

class EntityVelocityHolder {
    private final ConcurrentHashMap<Entity, Vector> entityVelocities = new ConcurrentHashMap<>();
    private static final EntityVelocityHolder singleton = new EntityVelocityHolder();

    private EntityVelocityHolder() {
    }

    public static EntityVelocityHolder getInstance() {
        return singleton;
    }

    public Vector getVelocity(Entity entity) {
        return entityVelocities.get(entity);
    }

    public void storeVelocity(Entity entity, Vector vec) {
        entityVelocities.put(entity, vec);
    }
}
