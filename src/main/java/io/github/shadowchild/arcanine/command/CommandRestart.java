package io.github.shadowchild.arcanine.command;

import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public class CommandRestart extends AbstractCommand {

    @Override
    public void onMessage(MessageEvent event, MessageChannel channel, User sender) {

        System.exit(0);
    }

    @Override
    public void onReactionAdd(MessageEvent event, MessageChannel channel, User sender) {

    }
}
