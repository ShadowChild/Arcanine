package io.github.shadowchild.arcanine.command;

import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import io.github.shadowchild.arcanine.Arcanine;

public class CommandRefresh extends AbstractCommand {

    @Override
    public void onMessage(MessageEvent event, MessageChannel channel, User sender, String alias) {

//        channel.createMessage("I am now refreshing my commands!");

        Thread thread = new Thread(() -> {

            try {

                Arcanine.LOADER.reloadCommands();
//            channel.createMessage("Commands refreshed successfully");
            } catch (Exception e) {

                e.printStackTrace();
//            channel.createMessage("Commands could not be refreshed, I'm shutting down!");
                System.exit(0);
            }
        });


    }

    @Override
    public void onReactionAdd(ReactionAddEvent event, MessageChannel channel, User sender) {

    }
}
