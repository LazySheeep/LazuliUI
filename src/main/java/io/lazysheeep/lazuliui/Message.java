package io.lazysheeep.lazuliui;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;


public class Message
{
    /**
     * The type, or the display position, of the message.<br>
     * The actionbar is divided into three parts: prefix, infix and suffix. Each part works individually
     */
    public enum Type { CHAT, ACTIONBAR_PREFIX, ACTIONBAR_INFIX, ACTIONBAR_SUFFIX }

    /**
     * The behavior to take when try to send a message but the corresponding part is occupied by other message
     * <p>
     * {@link LoadMode#IMMEDIATE } - Only try to immediately send the message.<br>
     * When the type of the message is {@link Type#CHAT}, will not check whether the CHAT part is occupied, just simply send the message.<br>
     * When the type of the message is {@link Type#ACTIONBAR_PREFIX}, {@link Type#ACTIONBAR_INFIX} or {@link Type#ACTIONBAR_SUFFIX},
     * if the corresponding part is occupied by other message, the message will be discarded and not send.
     * <p>
     * {@link LoadMode#WAIT } - Wait in queue until the corresponding part is empty.
     * <p>
     * {@link LoadMode#REPLACE } - Force to display, and the message currently being displayed will be discarded.
     */
    public enum LoadMode { IMMEDIATE, WAIT, REPLACE }

    Type type;
    TextComponent content;
    Sound tone;
    LoadMode loadMode;
    int lifeTime;
    boolean sent = false;

    /**
     * Build a message
     *
     * @param type {@link Type}
     * @param content the content of the message
     * @param loadMode {@link LoadMode}
     * @param lifeTime the duration(in ticks) this message will be displayed and occupy the corresponding part. Use -1 for forever.
     */
    public Message(@NotNull Type type, @NotNull TextComponent content, @NotNull LoadMode loadMode, int lifeTime)
    {
        this.type = type;
        this.content = content;
        this.tone = null;
        this.loadMode = loadMode;
        this.lifeTime = lifeTime;
    }

    /**
     * Build a message
     *
     * @param type {@link Type}
     * @param content the content of the message
     * @param tone the sound to play when the message is displayed
     * @param loadMode {@link LoadMode}
     * @param lifeTime the duration(in ticks) this message will be displayed and occupy the corresponding part. Use -1 for forever.
     */
    public Message(@NotNull Type type, @NotNull TextComponent content, Sound tone, @NotNull LoadMode loadMode, int lifeTime)
    {
        this.type = type;
        this.content = content;
        this.tone = tone;
        this.loadMode = loadMode;
        this.lifeTime = lifeTime;
    }

    /**
     * Build a message<br>
     * Will build a message with {@link Type#CHAT} as the type and {@link LoadMode#IMMEDIATE} as the loadMode.
     *
     * @param content the content of the message
     */
    public Message(@NotNull TextComponent content)
    {
        this.type = Type.CHAT;
        this.content = content;
        this.tone = null;
        this.loadMode = LoadMode.IMMEDIATE;
        this.lifeTime = 1;
    }
}
