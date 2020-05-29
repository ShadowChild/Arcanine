package io.github.shadowchild;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;

public class Arcanine {

    private static String accessToken = null;
    private static boolean hasGui = false;

    private static String[] runArgs;

    private static GatewayDiscordClient client;

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

        loadClient();
    }

    private static void loadClient() {

        client = DiscordClientBuilder.create(accessToken).build().login().block();
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
