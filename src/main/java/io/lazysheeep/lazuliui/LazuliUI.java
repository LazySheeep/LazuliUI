package io.lazysheeep.lazuliui;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
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
    private static LazuliUI instance;

    static final int actionbarInfixWidth = 32;

    @Override
    public void onEnable()
    {
        if(instance == null)
        {
            instance = this;
        }
        else
        {
            getLogger().log(Level.SEVERE, "Multiple instances of LazuliUI detected, disabling current one...");
            return;
        }
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getLogger().log(Level.INFO, "Enabled");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        UI ui = new UI(player);
        player.setMetadata("UI", new FixedMetadataValue(instance, ui));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onServerTickStart(@NotNull ServerTickStartEvent event)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            UI ui = getPlayerUI(player);
            if(ui != null)
                ui.tick();
        }
    }

    private static @Nullable UI getPlayerUI(@NotNull Player player)
    {
        for(MetadataValue metaData : player.getMetadata("UI"))
        {
            if(metaData.getOwningPlugin() == instance && metaData.value() instanceof UI ui)
                return ui;
        }
        return null;
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
