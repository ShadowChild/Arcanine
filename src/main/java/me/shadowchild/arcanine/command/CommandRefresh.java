package me.shadowchild.arcanine.command;

import me.shadowchild.arcanine.Arcanine;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class CommandRefresh extends AbstractCommand {

    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

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
    public void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender) {

    }
}
