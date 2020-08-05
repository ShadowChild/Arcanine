package me.shadowchild.arcanine.command.thread;

import me.shadowchild.arcanine.Arcanine;
import me.shadowchild.arcanine.command.template.AbstractCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.Map;

public class CommandExecutionThread implements Runnable {

    private final String cmdName;
    private GenericMessageEvent event;

    public CommandExecutionThread(String cmdName, GenericMessageEvent event) {

        this.cmdName = cmdName;
        this.event = event;
    }

    @Override
    public void run() {

        if(event instanceof MessageReceivedEvent) {

            Message m = ((MessageReceivedEvent) event).getMessage();

            // Clone to prevent ConcurrentModificationException
            Map<String, AbstractCommand> reg = Arcanine.LOADER.commands.clone();

            reg.values().forEach((cmd) -> {

                for(String s : cmd.getAlias()) {

                    if(cmdName.equalsIgnoreCase(s)) {

                        cmd.onMessage((MessageReceivedEvent) event, m.getChannel(), m.getAuthor(), s);
                    }
                }
            });
        } else if(event instanceof MessageReactionAddEvent) {


        }
    }
}
