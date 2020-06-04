package io.github.shadowchild.arcanine;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import io.github.shadowchild.arcanine.util.Resources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Arcanine {

    public static String[] runArgs;

    public static final Resources resources = new Resources();

    public static void main(String[] args) {

        runArgs = args;

        if(args.length == 0) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Arcanine Setup was not loaded correctly, please answer the following questions.");
            System.out.println("Would you Arcanine to have a gui? true/false: ");

            resources.hasGui = scanner.nextBoolean();

            // Workaround for nextBoolean not consuming the \n
            scanner.nextLine();

            System.out.println("Please input your Token from Discord Developer Portal: ");
            resources.accessToken = scanner.nextLine();

            scanner.close();
        } else {

            for (String arg : args) {

                System.out.println(arg);
            }

            for (int i = 0; i < args.length; i++) {

                String arg = args[i];
                if (arg.contains("--")) {

                    String mod = arg.substring(2);
                    String opt = args[i + 1];
                    handleArg(mod, opt);
                }
            }
        }

        try {

            resources.loader.reloadCommands();
        } catch (IOException | URISyntaxException e) {

            // This should never happen
            e.printStackTrace();
            System.exit(0);
        }
//        loader.commands.forEach((cmd) -> System.out.println(cmd.getName()));
//        loader.commands.forEach((cmd) -> System.out.println(Arrays.toString(cmd.getAlias())));
        resources.loader.commands.forEach((cmd) -> System.out.println(cmd.getUsage()));
//        loader.commands.forEach((cmd) -> System.out.println(cmd.getDescription()));
//        loader.commands.forEach((cmd) -> System.out.println(Arrays.toString(cmd.getDeep_description())));
//        loader.commands.forEach((cmd) -> System.out.println(cmd.getPermission()));
        loadClient();
    }

    private static void loadClient() {

        resources.client = DiscordClientBuilder.create(resources.accessToken).build().login().block();

        resources.client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });

        resources.client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(ArcanineMessageListener::onMessage);

        resources.client.onDisconnect().block();
    }

    private static void handleArg(String mod, String opt) {

        switch(mod) {

            case "gui": resources.hasGui = true; break;
            case "token": resources.accessToken = opt.replace(" ", ""); break;

            default: break;
        }
    }
}
