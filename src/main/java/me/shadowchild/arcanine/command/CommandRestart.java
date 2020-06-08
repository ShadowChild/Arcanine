package me.shadowchild.arcanine.command;

import me.shadowchild.arcanine.Arcanine;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandRestart extends AbstractCommand {

    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

        channel.sendMessage("I am returning to my pokéball").queue();

        if(alias.equalsIgnoreCase("shutdown") || alias.equalsIgnoreCase("s")) {

            System.exit(0);
        } else {

            try {
                File jar = new File(Arcanine.LOADER.JAR_FILE.toURI());
                String workingDir = jar.getParentFile().getCanonicalPath();
                String jarName = jar.getName();

                ArrayList<String> commands = new ArrayList<>();
                commands.add("java");
                commands.add("-jar");
                commands.add(new File(workingDir, "libs/ProcStart.jar").getCanonicalPath());
                commands.add("-loc");
                commands.add(workingDir);
                commands.add("-jar");
                commands.add(jarName.replace(".jar", ""));
                commands.add("-runDir");
                commands.add(workingDir);
                commands.add("-args");
                commands.addAll(Arrays.asList(Arcanine.runArgs));

                ProcessBuilder builder = new ProcessBuilder(commands).redirectErrorStream(true);
                Process proc = builder.start();

                BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                br.lines().forEach(System.out::println);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    @Override
    public void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender) {

    }
}