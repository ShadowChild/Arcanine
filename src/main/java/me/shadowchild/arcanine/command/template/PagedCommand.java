package me.shadowchild.arcanine.command.template;

import com.jagrosh.jdautilities.menu.Paginator;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public abstract class PagedCommand extends AbstractCommand {

    protected Paginator.Builder builder;

    public PagedCommand() {

        this.builder = getBuilder();
    }

    protected abstract Paginator.Builder getBuilder();

    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

    }

    @Override
    public void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender) {

    }

    @Override
    public void onShutdown(JDA client) {

    }


}
