package io.lazysheeep.lazuliui;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LazuliUI extends JavaPlugin implements Listener
{
    public static LazuliUI plugin;
    public Logger logger;
    private int actionbarInfixWidth;

    @Override
    public void onEnable()
    {
        plugin = this;
        logger = this.getLogger();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        logger.log(Level.INFO, "Enabled");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        UI ui = new UI(player);
        ui.setActionbarInfixWidth(actionbarInfixWidth);
        player.setMetadata("UI", new FixedMetadataValue(plugin, ui));
        Bukkit.getServer().getPluginManager().registerEvents(ui, plugin);
    }

    static @Nullable UI getPlayerUI(@NotNull Player player)
    {
        for(MetadataValue metaData : player.getMetadata("UI"))
        {
            if(metaData.getOwningPlugin() == plugin && metaData.value() instanceof UI ui)
                return ui;
        }
        return null;
    }

    /**
     * Set the width of the actionbar infix.
     * <p>
     * If the message being displayed is shorter than the set width, spaces will be filled,
     * to keep the position of prefix and suffix.
     *
     * @param width the width in characters
     */
    static public void setActionbarInfixWidth(int width)
    {
        plugin.actionbarInfixWidth = width;
    }

    /**
     * Send a message to player.
     *
     * @param player the player to send the message
     * @param message the message to send
     */
    static public void sendMessage(@NotNull Player player, @NotNull Message message)
    {
        UI ui = getPlayerUI(player);
        if(ui != null)
            ui.sendMessage(message);
    }

    /**
     * Send a series of messages to player.
     *
     * @param player the player to send the messages
     * @param messages the messages to send
     */
    static public void sendMessage(@NotNull Player player, @NotNull List<Message> messages)
    {
        for(Message message : messages)
        {
            sendMessage(player, message);
        }
    }

    /**
     * Broadcast a message to all online players.
     *
     * @param message the message to broadcast
     */
    static public void broadcast(@NotNull Message message)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            sendMessage(player, message.clone());
        }
    }

    /**
     * Broadcast a message to the players with the specified permission.
     *
     * @param permission the required permission to receive the message
     * @param message the message to broadcast
     */
    static public void broadcast(@NotNull String permission, @NotNull Message message)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            if(player.hasPermission(permission))
                sendMessage(player, message.clone());
        }
    }

    /**
     * Broadcast a series of messages to the players with the specified permission.
     *
     * @param permission the required permission to receive the message
     * @param messages the messages to broadcast
     */
    static public void broadcast(@NotNull String permission, @NotNull List<Message> messages)
    {
        for(Message message : messages)
        {
            broadcast(permission, message.clone());
        }
    }

    /**
     * Clear messages of player, both those being displayed and those in queue.
     *
     * @param player the player
     */
    static public void flush(@NotNull Player player)
    {
        flush(player, Message.Type.values());
    }

    /**
     * Clear messages of players with the specified permission, both those being displayed and those in queue.
     *
     * @param permission the specified permission
     */
    static public void flush(@NotNull String permission)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            if(player.hasPermission(permission))
                flush(player);
        }
    }

    /**
     * Clear messages of player, both those being displayed and those in queue.
     * Only the specified type of messages will be flushed.
     *
     * @param player the player
     * @param types the types of messages to flush
     */
    static public void flush(@NotNull Player player, @NotNull Message.Type... types)
    {
        UI ui = getPlayerUI(player);
        if(ui != null)
            ui.flush(types);
    }

    /**
     * Clear messages of players with the specified permission, both those being displayed and those in queue.
     * Only the specified type of messages will be flushed.
     *
     * @param permission the specified permission
     * @param types the types of messages to flush
     */
    static public void flush(@NotNull String permission, @NotNull Message.Type... types)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            if(player.hasPermission(permission))
                flush(player, types);
        }
    }
}
