package io.github.shadowchild.arcanine;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import io.github.shadowchild.arcanine.util.Loader;

import java.io.FileNotFoundException;

public class Arcanine {

    private static String accessToken = null;
    private static boolean hasGui = false;

    private static String[] runArgs;

    public static final Loader loader = new Loader("assets");

    public static GatewayDiscordClient client;

    public static final String prefix = ">";

    public static void main(String[] args) {

        runArgs = args;

        for(int i = 0; i < args.length; i++) {

            String arg = args[i];
            if(arg.contains("--")) {

                String mod = arg.substring(2);
                String opt = args[i+1];
                handleArg(mod, opt);
            }
        }

        try {

            loader.reloadCommands();
        } catch (FileNotFoundException e) {

            // This should never happen
            e.printStackTrace();
            System.exit(0);
        }
//        loader.commands.forEach((cmd) -> System.out.println(cmd.getName()));
//        loader.commands.forEach((cmd) -> System.out.println(Arrays.toString(cmd.getAlias())));
        loader.commands.forEach((cmd) -> System.out.println(cmd.getUsage()));
//        loader.commands.forEach((cmd) -> System.out.println(cmd.getDescription()));
//        loader.commands.forEach((cmd) -> System.out.println(Arrays.toString(cmd.getDeep_description())));
//        loader.commands.forEach((cmd) -> System.out.println(cmd.getPermission()));
        loadClient();
    }

    private static void loadClient() {

        client = DiscordClientBuilder.create(accessToken).build().login().block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });

        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(ArcanineMessageListener::onMessage);

        client.onDisconnect().block();
    }

    private static void handleArg(String mod, String opt) {

        switch(mod) {

            case "gui": hasGui = true; break;
            case "token": accessToken = opt; break;

            default: break;
        }
    }
}
