package me.shadowchild.arcanine.command;

import me.shadowchild.arcanine.Arcanine;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.Arrays;

public class CommandHelp extends AbstractCommand {


    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {
        
        Message m = event.getMessage();
        String[] raw = m.getContentRaw().split(" ");

        MessageEmbed embed = null;

        if(raw.length > 1) {

            if(raw.length > 2) {

                channel.sendMessage("The help message was used incorrectly.\nPlease follow the usage: \n" + getUsage(alias)).queue();
            } else {

                embed = getDeepInfoEmbed(raw[1]);
            }
        } else {

            embed = getHelpEmbed();
        }

        if(embed != null) channel.sendMessage(embed).queue();
    }

    @Override
    public void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender) {

    }

    @Override
    public void onShutdown(JDA client) {

    }

    private MessageEmbed getHelpEmbed() {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#E91E63"));
        builder.setTitle("Arcanine Help!", "https://github.com/ShadowChild/Arcanine");
        builder.setDescription("Arcanine is a bot made by ShadowChild.");

        Arcanine.LOADER.commands.getRegistry().forEach((key, cmd) -> {

            builder.addField(StringUtils.capitalize(key), cmd.getDescription(), false);
        });

        return builder.build();
    }

    private MessageEmbed getDeepInfoEmbed(String helpAlias) {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#E91E63"));
        builder.setTitle("Arcanine Help!", "https://github.com/ShadowChild/Arcanine");
        builder.setDescription("Arcanine is a bot made by ShadowChild.");

        Arcanine.LOADER.commands.getRegistry().values().forEach((cmd) -> {

            Arrays.asList(cmd.getAlias()).forEach((s) -> {

                if(s.equalsIgnoreCase(helpAlias)) {

                    StringBuilder sBuilder = new StringBuilder();
                    Arrays.asList(cmd.getDeep_description()).forEach((d) -> {

                        sBuilder.append(d);
                        sBuilder.append(" ");
                    });
                    builder.addField(helpAlias, sBuilder.toString(), true);
                }
            });
        });
        return builder.build();
    }
}
