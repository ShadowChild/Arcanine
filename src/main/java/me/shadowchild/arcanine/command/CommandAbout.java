package me.shadowchild.arcanine.command;

import me.shadowchild.arcanine.command.template.AbstractCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.time.Instant;

public class CommandAbout extends AbstractCommand {


    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("About Arcanine", "https://arcanine.shadowchild.me");
        builder.setImage("http://arcanine.shadowchild.me/pokemon-arcanine.jpg");
        builder.setThumbnail("https://arcanine.shadowchild.me/Dali.jpg");
        builder.setColor(arcanine_Red);
        builder.setDescription("Info and Stats about Arcanine");
        builder.setAuthor("ShadowChild", "https://shadowchild.me");
        {
            builder.setTimestamp(Instant.now());
            builder.addField("Version", "SNAPSHOT", true);
            builder.addField("Java Version", System.getProperty("java.version"), true);
        }
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
