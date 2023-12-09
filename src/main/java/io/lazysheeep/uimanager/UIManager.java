package io.lazysheeep.uimanager;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class UIManager extends JavaPlugin implements Listener
{
    private static UIManager plugin;

    @Override
    public void onEnable()
    {
        plugin = this;

        this.getServer().getPluginManager().registerEvents(plugin, this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        UI ui = new UI(player);
        ui.setActionbarInfixWidth(32);
        player.setMetadata("UI", new FixedMetadataValue(plugin, ui));
        plugin.getServer().getPluginManager().registerEvents(ui, plugin);
    }

    public UI getPlayerUI(Player player)
    {
        for(MetadataValue metaData : player.getMetadata("UI"))
        {
            if(metaData.getOwningPlugin() == plugin && metaData.value() instanceof UI ui)
                return ui;
        }
        return null;
    }

    public void sendMessage(Player player, Message message)
    {
        getPlayerUI(player).sendMessage(message);
    }

    public void sendMessage(Player player, Message.Type type, TextComponent content, Message.LoadMode loadMode, int lifeTime)
    {
        getPlayerUI(player).sendMessage(new Message(type, content, loadMode, lifeTime));
    }

    public void broadcastMessage(String permission, Message message)
    {
        // TODO
    }

    public void flush(Player player, Message.Type... types)
    {
        getPlayerUI(player).flush(types);
    }

}
