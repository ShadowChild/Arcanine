package me.shadowchild.arcanine;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;

public class ArcanineMessageListener {

    public static void onMessage(MessageCreateEvent e) {

        Message m = e.getMessage();
        String content = m.getContent();
        if(content.startsWith(Arcanine.LOADER.botCfg.prefix)) {

            // get rid of the prefix
            content = content.substring(1);
            String[] args = content.split(" ");
            for (String s : args) {
                System.out.println(s);
            }
            String alias = args[0];

            Arcanine.LOADER.commands.getRegistry().values().forEach((cmd) -> {

                for(String s : cmd.getAlias()) {

                    if(alias.equalsIgnoreCase(s)) {

                        cmd.onMessage(e, m.getChannel().block(), m.getAuthor().get(), s);
                    }
                }
            });
        }
    }
}
