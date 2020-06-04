package io.github.shadowchild.arcanine;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import io.github.shadowchild.arcanine.command.AbstractCommand;

public class ArcanineMessageListener {

    public static void onMessage(MessageCreateEvent e) {

        Message m = e.getMessage();
        String content = m.getContent();
        if(content.startsWith(Arcanine.LOADER.prefix)) {

            // get rid of the prefix
            content = content.substring(1);
            String[] args = content.split(" ");
            for (String s : args) {
                System.out.println(s);
            }
            String alias = args[0];
            for (AbstractCommand cmd : Arcanine.LOADER.commands) {

                String[] aliases = cmd.getAlias();
                for (String s : aliases) {

                    if (alias.equalsIgnoreCase(s)) {

                        cmd.onMessage(e, m.getChannel().block(), m.getAuthor().get(), s);
                    }
                }
            }
        }
    }
}
