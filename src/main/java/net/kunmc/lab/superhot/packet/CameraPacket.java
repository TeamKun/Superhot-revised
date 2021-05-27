package net.kunmc.lab.superhot.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CameraPacket extends PacketClient {
    private final Entity entity;

    public CameraPacket(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void send(Player p) {
        sendPacket(p, createPacket());
    }

    private PacketContainer createPacket() {
        PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.CAMERA);
        packetContainer.getIntegers().write(0, entity.getEntityId());
        return packetContainer;
    }
}
