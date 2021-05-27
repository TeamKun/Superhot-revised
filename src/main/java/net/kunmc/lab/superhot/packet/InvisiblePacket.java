package net.kunmc.lab.superhot.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class InvisiblePacket extends PacketClient {
    private final Entity entity;
    private final boolean isInvisible;

    public InvisiblePacket(Entity target, boolean isInvisible) {
        this.entity = target;
        this.isInvisible = isInvisible;
    }

    @Override
    public void send(Player p) {
        sendPacket(p, createPacket());
    }

    private PacketContainer createPacket() {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packetContainer.getIntegers().write(0, entity.getEntityId());
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setEntity(entity);
        watcher.setObject(0, serializer, (byte) (isInvisible ? 0x20 : 0x00));
        packetContainer.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        return packetContainer;
    }
}
