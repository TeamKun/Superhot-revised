package net.kunmc.lab.superhot.packet;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public abstract class PacketClient {
    protected ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    abstract public void send(Player p);

    protected void sendPacket(Player p, PacketContainer packetContainer) {
        try {
            protocolManager.sendServerPacket(p, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
