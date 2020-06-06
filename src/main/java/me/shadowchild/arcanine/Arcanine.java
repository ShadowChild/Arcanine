package me.shadowchild.arcanine;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import me.shadowchild.arcanine.util.Loader;

import java.io.IOException;
import java.net.URISyntaxException;

public class Arcanine {

    public static String[] runArgs;

    public static final Loader LOADER = new Loader("assets");

    public static void main(String[] args) {

        runArgs = args;

        try {

            LOADER.loadConfig();
        } catch (Exception e) {

            // Should never happen
            e.printStackTrace();
            System.exit(0);
        }

        try {

            LOADER.reloadCommands();
        } catch (IOException | URISyntaxException e) {

            // This should never happen
            e.printStackTrace();
            System.exit(0);
        }
        loadClient();
    }

    private static void loadClient() {

        LOADER.client = DiscordClientBuilder.create(LOADER.botCfg.token).build().login().block();

        LOADER.client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });

        LOADER.client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(ArcanineMessageListener::onMessage);

        LOADER.client.onDisconnect().block();
    }
}
