package me.shadowchild.arcanine.command;

import me.shadowchild.arcanine.Arcanine;
import me.shadowchild.arcanine.util.GuildUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class CommandRefresh extends AbstractCommand {

    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

        if(GuildUtils.isUserAdmin(event.getGuild(), sender)) {

            channel.sendMessage("I am now refreshing my commands!").queue();

            Thread thread = new Thread(() -> {

                try {

                    Arcanine.LOADER.reloadCommands();
                    channel.sendMessage("Commands refreshed successfully").queue();
                } catch (Exception e) {

                    e.printStackTrace();
                    channel.sendMessage("Commands could not be refreshed, I'm shutting down!").queue();
                    Arcanine.safeShutdown();
                }
            });
            thread.start();
        }
    }

    @Override
    public void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender) {

    }

    @Override
    public void onShutdown(JDA client) {

    }
}
