package io.lazysheeep.lazuliui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;

class UI
{
    private final Player player;
    private final LinkedList<Message> messageList = new LinkedList<>();
    private final Message[] messagesOnStage = new Message[Message.Type.values().length];

    public UI(@NotNull Player player)
    {
        this.player = player;
    }

    public void sendMessage(@NotNull Message message)
    {
        if(message.type == Message.Type.CHAT && message.loadMode == Message.LoadMode.IMMEDIATE)
        {
            this.player.sendMessage(message.content);
            if(message.tone != null) this.player.playSound(this.player, message.tone, SoundCategory.MASTER, 1.0f, 1.0f);
        }
        else
        {
            this.messageList.offerLast(message);
        }
    }

    public void flush(Message.Type... types)
    {
        for(Message.Type type : types)
        {
            messagesOnStage[type.ordinal()] = null;
            messageList.removeIf(message -> message.type == type);
        }
    }

    public void tick()
    {
        // load message from messageList
        for(Iterator<Message> it = this.messageList.iterator(); it.hasNext(); )
        {
            Message message = it.next();
            // load
            if(this.messagesOnStage[message.type.ordinal()] == null || message.loadMode == Message.LoadMode.REPLACE)
            {
                this.messagesOnStage[message.type.ordinal()] = message;
                it.remove();
            }
            // clear IMMEDIATE messages that can't be load
            else if(message.loadMode == Message.LoadMode.IMMEDIATE)
            {
                it.remove();
            }
        }

        // actual send messages
        this.draw();

        // update or unload the messagesOnStage
        for(int i = 0; i < messagesOnStage.length; i ++)
        {
            if(messagesOnStage[i] != null)
            {
                // set sent to true
                messagesOnStage[i].sent = true;
                // update lifeTime
                if(messagesOnStage[i].lifeTime > 0)
                    messagesOnStage[i].lifeTime --;
                // unload if lifeTime is 0
                if(messagesOnStage[i].lifeTime == 0)
                    messagesOnStage[i] = null;
            }
        }
    }

    private void draw()
    {
        // chat
        Message chatMessage = this.messagesOnStage[Message.Type.CHAT.ordinal()];
        if(chatMessage != null && !chatMessage.sent)
        {
            this.player.sendMessage(chatMessage.content);
            if(chatMessage.tone != null) this.player.playSound(this.player, chatMessage.tone, SoundCategory.MASTER, 1.0f, 1.0f);
        }

        // actionbar
        Message actionbarPrefix = this.messagesOnStage[Message.Type.ACTIONBAR_PREFIX.ordinal()];
        Message actionbarInfix = this.messagesOnStage[Message.Type.ACTIONBAR_INFIX.ordinal()];
        Message actionbarSuffix = this.messagesOnStage[Message.Type.ACTIONBAR_SUFFIX.ordinal()];
        int prefixWidth = actionbarPrefix == null ? 0 : Util.getTextComponentWidth(actionbarPrefix.content);
        int infixWidth = actionbarInfix == null ? 0 : Util.getTextComponentWidth(actionbarInfix.content);
        int suffixWidth = actionbarSuffix == null ? 0 : Util.getTextComponentWidth(actionbarSuffix.content);
        int prefixPaddingLength = Math.max(prefixWidth, suffixWidth) - prefixWidth;
        int infixPaddingLength = Math.max(LazuliUI.actionbarInfixWidth - infixWidth, 0);
        int suffixPaddingLength = Math.max(prefixWidth, suffixWidth) - suffixWidth;
        TextComponent actionbarComponent = Component.text("");
        // prefix
        if(prefixPaddingLength > 0)
            actionbarComponent = actionbarComponent.append(Component.text(" ".repeat(prefixPaddingLength)));
        if(prefixWidth > 0)
            actionbarComponent = actionbarComponent.append(actionbarPrefix.content);
        // infix
        if(infixPaddingLength > 0)
            actionbarComponent = actionbarComponent.append(Component.text(" ".repeat(infixPaddingLength/2)));
        if(infixWidth > 0)
            actionbarComponent = actionbarComponent.append(actionbarInfix.content);
        if(infixPaddingLength > 0)
            actionbarComponent = actionbarComponent.append(Component.text(" ".repeat(infixPaddingLength-infixPaddingLength/2)));
        // suffix
        if(suffixWidth > 0)
            actionbarComponent = actionbarComponent.append(actionbarSuffix.content);
        if(suffixPaddingLength > 0)
            actionbarComponent = actionbarComponent.append(Component.text(" ".repeat(suffixPaddingLength)));

        player.sendActionBar(actionbarComponent);
        if(actionbarPrefix != null && !actionbarPrefix.sent && actionbarPrefix.tone != null) this.player.playSound(this.player, actionbarPrefix.tone, SoundCategory.MASTER, 1.0f, 1.0f);
        if(actionbarInfix != null && !actionbarInfix.sent && actionbarInfix.tone != null) this.player.playSound(this.player, actionbarInfix.tone, SoundCategory.MASTER, 1.0f, 1.0f);
        if(actionbarSuffix != null && !actionbarSuffix.sent && actionbarSuffix.tone != null) this.player.playSound(this.player, actionbarSuffix.tone, SoundCategory.MASTER, 1.0f, 1.0f);
    }
}
