package me.shadowchild.arcanine.command;

import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import me.shadowchild.arcanine.Arcanine;

import java.util.function.Consumer;

public class CommandHelp extends AbstractCommand {

    @Override
    public void onMessage(MessageEvent event, MessageChannel channel, User sender, String alias) {

        Consumer<EmbedCreateSpec> embed = spec -> {

          spec.setColor(Color.RUBY);
          spec.setTitle("Arcanine Help!");
          spec.setUrl("https://github.com/ShadowChild/Arcanine");
          spec.setDescription("Arcanine is a bot made by ShadowChild.");

          Arcanine.LOADER.commands.getRegistry().forEach((key, cmd) -> {

                spec.addField(key, cmd.getDescription(), false);
          });

        };
        channel.createEmbed(embed).block();
    }

    @Override
    public void onReactionAdd(ReactionAddEvent event, MessageChannel channel, User sender) {

    }
}
