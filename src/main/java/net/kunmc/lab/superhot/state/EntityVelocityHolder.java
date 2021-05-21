package net.kunmc.lab.superhot.state;

import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class EntityVelocityHolder {
    private final ConcurrentHashMap<UUID, Vector> entityVelocities = new ConcurrentHashMap<>();
    private static final EntityVelocityHolder singleton = new EntityVelocityHolder();

    private EntityVelocityHolder() {
    }

    public static EntityVelocityHolder getInstance() {
        return singleton;
    }

    public Vector getVelocity(UUID uuid) {
        return entityVelocities.get(uuid);
    }

    public void storeVelocity(UUID uuid, Vector vec) {
        entityVelocities.put(uuid, vec);
    }
}
