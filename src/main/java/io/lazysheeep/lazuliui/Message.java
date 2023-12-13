package io.lazysheeep.lazuliui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class Message
{
    public enum Type { CHAT, ACTIONBAR_PREFIX, ACTIONBAR_INFIX, ACTIONBAR_SUFFIX }
    public enum LoadMode { IMMEDIATE, WAIT, REPLACE }
    Type type;
    TextComponent content;
    Sound tone;
    LoadMode loadMode;
    int lifeTime;
    boolean sent = false;

    public Message(@NotNull Type type, @NotNull TextComponent content, @NotNull LoadMode loadMode, int lifeTime)
    {
        this.type = type;
        this.content = content;
        this.tone = null;
        this.loadMode = loadMode;
        this.lifeTime = lifeTime;
    }

    public Message(@NotNull Type type, @NotNull String content, @NotNull LoadMode loadMode, int lifeTime)
    {
        this.type = type;
        this.content = Component.text(content);
        this.tone = null;
        this.loadMode = loadMode;
        this.lifeTime = lifeTime;
    }

    public Message(@NotNull Type type, @NotNull TextComponent content, Sound tone, @NotNull LoadMode loadMode, int lifeTime)
    {
        this.type = type;
        this.content = content;
        this.tone = tone;
        this.loadMode = loadMode;
        this.lifeTime = lifeTime;
    }
}
