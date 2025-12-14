package astra.core.api.tablist;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class TabListAPI {

    /**
     * @param header
     *            The header of the tab list.
     */
    public static void broadcastHeader(String header) throws InvocationTargetException {
        broadcastHeaderAndFooter(header, null);
    }

    /**
     * @param footer
     *            The footer of the tab list.
     */
    public static void broadcastFooter(String footer) throws InvocationTargetException {
        broadcastHeaderAndFooter(null, footer);
    }

    /**
     * @param header
     *            The header of the tab list.
     * @param footer
     *            The footer of the tab list.
     */
    public static void broadcastHeaderAndFooter(String header, String footer) throws InvocationTargetException {
        for (Player player : Bukkit.getOnlinePlayers())
            setHeaderAndFooter(player, header, footer);
    }

    /**
     * @param p
     *            The Player.
     * @param header
     *            The header.
     */
    public static void setHeader(Player p, String header) throws InvocationTargetException {
        setHeaderAndFooter(p, header, null);
    }

    /**
     * @param p
     *            The Player
     * @param footer
     *            The footer.
     */
    public static void setFooter(Player p, String footer) throws InvocationTargetException {
        setHeaderAndFooter(p, null, footer);
    }

    /**
     * @param p
     *            The Player.
     * @param rawHeader
     *            The header in raw text.
     * @param rawFooter
     *            The footer in raw text.
     */
    public static void setHeaderAndFooter(Player p, String rawHeader, String rawFooter) throws InvocationTargetException {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(rawHeader));
        packet.getChatComponents().write(1, WrappedChatComponent.fromText(rawFooter));

        ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
    }
}

