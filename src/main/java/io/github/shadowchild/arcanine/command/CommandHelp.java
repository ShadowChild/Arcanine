package io.github.shadowchild.arcanine.command;

import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public class CommandHelp extends AbstractCommand {

    @Override
    public void onMessage(MessageEvent event, MessageChannel channel, User sender) {

        channel.createMessage("This is the help menu").block();
    }

    @Override
    public void onReactionAdd(MessageEvent event, MessageChannel channel, User sender) {

    }

    @Override
    public String getUsage(String alias) {

        return usage.replaceAll("%cmd%", alias);
    }
}
