package me.shadowchild.arcanine;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ArcanineMessageListener extends ListenerAdapter {

    @Override
    public void onReady(@Nonnull ReadyEvent event) {

        int count = event.getGuildTotalCount();
        Arcanine.LOGGER.info("Arcanine loaded in {} servers", count);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

        Message message = event.getMessage();
        String content = message.getContentRaw();
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

                        cmd.onMessage(event, message.getChannel(), message.getAuthor(), s);
                    }
                }
            });
        }
    }
}
