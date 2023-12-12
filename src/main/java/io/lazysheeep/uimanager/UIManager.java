package io.lazysheeep.uimanager;

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

public class UIManager extends JavaPlugin implements Listener
{
    private static UIManager plugin;

    @Override
    public void onEnable()
    {
        plugin = this;

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        UI ui = new UI(player);
        ui.setActionbarInfixWidth(36);
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

    static public void sendMessage(@NotNull Player player, @NotNull Message message)
    {
        UI ui = getPlayerUI(player);
        if(ui != null)
            ui.sendMessage(message);
    }

    static public void sendMessage(@NotNull Player player, @NotNull TextComponent content)
    {
        sendMessage(player, new Message(Message.Type.CHAT, content, Message.LoadMode.REPLACE, 1));
    }

    static public void broadcast(@NotNull String permission, @NotNull Message message)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            if(player.hasPermission(permission))
                sendMessage(player, message);
        }
    }

    static public void broadcast(@NotNull String permission, @NotNull TextComponent content)
    {
        broadcast(permission, new Message(Message.Type.CHAT, content, Message.LoadMode.REPLACE, 1));
    }

    static public void flush(@NotNull Player player)
    {
        flush(player, Message.Type.values());
    }

    static public void flush(@NotNull Player player, @NotNull Message.Type... types)
    {
        UI ui = getPlayerUI(player);
        if(ui != null)
            ui.flush(types);
    }
}
