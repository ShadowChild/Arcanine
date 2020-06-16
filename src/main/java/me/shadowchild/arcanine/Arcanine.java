package me.shadowchild.arcanine;

import me.shadowchild.arcanine.util.Loader;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;

public class Arcanine {

    public static String[] runArgs;

    public static final Loader LOADER = new Loader("assets");
    public static Logger LOGGER;

    public static void main(String[] args) {

        runArgs = args;

        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        LogManager.setFactory(new Log4jContextFactory());
        LOGGER = LogManager.getLogger(Arcanine.class);
        try {

            LOGGER.info("Arcanine is loading it's configs");
            LOADER.loadConfig();
        } catch (Exception e) {

            // Should never happen
            e.printStackTrace();
            System.exit(0);
        }

        LOGGER.info("Configs successfully loaded");

//        if(LOADER.botCfg.gui) {
//
//            SwingUtilities.invokeLater(new ArcanineGui());
//        }

        try {

            LOGGER.info("Attempting to load commands");
            LOADER.reloadCommands();
        } catch (IOException | URISyntaxException e) {

            // This should never happen
            e.printStackTrace();
            System.exit(0);
        }
        LOGGER.info("Arcanine has finished loading commands");
        LOGGER.info("Attempting to start discord client");
        try {

            loadClient();
        } catch (Exception e) {

            e.printStackTrace();
            System.exit(0);
        }
        LOGGER.info("Client Loaded");
    }

    private static void loadClient() throws LoginException, InterruptedException {

        JDABuilder builder = JDABuilder.createDefault(LOADER.botCfg.token);
        builder.setActivity(Activity.watching("his little Growlithe"));
        builder.addEventListeners(new ArcanineMessageListener());
        LOADER.client = builder.build();
        LOADER.client.awaitReady();
    }

    public static void safeShutdown() {

        LOGGER.debug("Shutting Down!");
        LOADER.commands.getRegistry().values().forEach((cmd) -> {

            LOGGER.debug("Shutting down command: " + cmd.getName());
            cmd.onShutdown(LOADER.client);
        });
        LOGGER.debug("Shutting down client.");
        LOADER.client.shutdown();
    }
}
