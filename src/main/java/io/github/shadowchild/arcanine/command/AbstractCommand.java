package io.github.shadowchild.arcanine.command;

import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public abstract class AbstractCommand {

    protected String name;

    protected String[] alias;

    protected String permission;

    protected String usage;

    protected String description;

    protected String[] deep_description;

    public abstract void onMessage(MessageEvent event, MessageChannel channel, User sender, String alias);
    public abstract void onReactionAdd(ReactionAddEvent event, MessageChannel channel, User sender);

    public String getName() {

        return name;
    }

    public String[] getAlias() {

        String[] ret = new String[alias.length + 1];

        ret[0] = name;

        for (int i = 0; i < alias.length; i++) {

            ret[i+1] = alias[i];
        }

        return ret;
    }

    public String getPermission() {

        return permission;
    }

    public String getUsage(String alias) {

        return usage.replaceAll("%cmd%", alias);
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
