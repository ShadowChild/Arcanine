package me.shadowchild.arcanine.command.template;

import me.shadowchild.arcanine.Arcanine;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.awt.*;

public abstract class AbstractCommand {

    protected String name;

    protected String[] alias;

    protected String permission;

    protected String usage;

    protected String description;

    protected String[] deep_description;

    public final Color arcanine_Red = Color.decode("#E91E63");

    public abstract void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias);
    public abstract void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender);
    public abstract void onShutdown(JDA client);

    public String getName() {

        return name;
    }

    public String[] getAlias() {

        String[] ret = new String[alias.length + 1];

        ret[0] = name;

        System.arraycopy(alias, 0, ret, 1, alias.length);

        return ret;
    }

    public String getPermission() {

        return permission;
    }

    public String getUsage(String alias) {

        String ret = usage;
        ret = ret.replaceAll("%prefix%", Arcanine.LOADER.botCfg.prefix);
        return ret.replaceAll("%cmd%", alias);
    }

    public String getUsage() {

        return getUsage(name);
    }

    public String getDescription() {

        return description;
    }

    public String[] getDeep_description() {

        return deep_description;
    }
}
