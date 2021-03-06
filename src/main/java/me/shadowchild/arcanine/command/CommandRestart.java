package me.shadowchild.arcanine.command;

import me.shadowchild.arcanine.Arcanine;
import me.shadowchild.arcanine.command.template.AbstractCommand;
import me.shadowchild.arcanine.util.GuildUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class CommandRestart extends AbstractCommand {

    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

        if(GuildUtils.isUserAdmin(event.getGuild(), sender)) {

            // Fixed the gosh dang bug where é was showing as Ã©
            channel.sendMessage("I am returning to my pok" + "\u00e9" + "ball! See you soon!").queue();

            if (alias.equalsIgnoreCase("shutdown") || alias.equalsIgnoreCase("s")) {

                Arcanine.safeShutdown();
            } else {

//                try {
//
//                    File jar = new File(Arcanine.LOADER.JAR_FILE.toURI());
//                    String workingDir = jar.getParentFile().getCanonicalPath();
//                    String jarName = jar.getName();
//
//                    ArrayList<String> commands = new ArrayList<>();
//                    commands.add("java");
//                    commands.add("-jar");
//                    commands.add(new File(workingDir, "libs/ProcStart.jar").getCanonicalPath());
//                    commands.add("-loc");
//                    commands.add(workingDir);
//                    commands.add("-jar");
//                    commands.add(jarName.replace(".jar", ""));
//                    commands.add("-runDir");
//                    commands.add(workingDir);
//                    commands.add("-args");
//                    commands.addAll(Arrays.asList(Arcanine.runArgs));
//
//                    ProcessBuilder builder = new ProcessBuilder(commands).redirectErrorStream(true);
//                    Process proc = builder.start();
//
//                    BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                    br.lines().forEach(System.out::println);
//                    Arcanine.safeShutdown();
//                } catch (URISyntaxException | IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    @Override
    public void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender) {

    }

    @Override
    public void onShutdown(JDA client) {

    }
}
