package me.shadowchild.arcanine;

import me.shadowchild.arcanine.command.thread.CommandExecutionThread;
import me.shadowchild.cybernize.net.Hastebin;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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
            String alias = args[0];

            Thread commandThread = new Thread(new CommandExecutionThread(alias, event), "Arcanine Command Execution Thread");
            commandThread.start();
        }
    }

    @Override
    public void onException(@Nonnull ExceptionEvent event) {

        StringWriter sw = new StringWriter();
        event.getCause().printStackTrace(new PrintWriter(sw));
        String result = sw.toString();

        try {

            String hasteResult = Hastebin.post(result, true);
            Arcanine.LOGGER.error(hasteResult);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
