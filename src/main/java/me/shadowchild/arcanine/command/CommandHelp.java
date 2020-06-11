package me.shadowchild.arcanine.command;

import me.shadowchild.arcanine.Arcanine;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class CommandHelp extends AbstractCommand {


    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#E91E63"));
        builder.setTitle("Arcanine Help!", "https://github.com/ShadowChild/Arcanine");
        builder.setDescription("Arcanine is a bot made by ShadowChild.");

        Arcanine.LOADER.commands.getRegistry().forEach((key, cmd) -> {

            builder.addField(StringUtils.capitalize(key), cmd.getDescription(), false);
        });

        MessageEmbed embed = builder.build();

        channel.sendMessage(embed).queue();
    }

    @Override
    public void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender) {

    }

    @Override
    public void onShutdown(JDA client) {

    }
}
