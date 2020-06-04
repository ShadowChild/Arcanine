package io.github.shadowchild.arcanine.command;

import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import io.github.shadowchild.arcanine.Arcanine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandRestart extends AbstractCommand {

    @Override
    public void onMessage(MessageEvent event, MessageChannel channel, User sender, String alias) {

        channel.createMessage("I am returning to my pokéball").block();

        if(alias.equalsIgnoreCase("shutdown") || alias.equalsIgnoreCase("s")) {

            System.exit(0);
        } else {

            try {
                File jar = new File(Arcanine.LOADER.JAR_FILE.toURI());
                String workingDir = jar.getParentFile().getCanonicalPath();
                String jarName = jar.getName();

                ArrayList<String> commands = new ArrayList();
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
    public void onReactionAdd(ReactionAddEvent event, MessageChannel channel, User sender) {

    }
}
