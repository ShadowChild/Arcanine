package io.github.shadowchild.arcanine.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import discord4j.core.GatewayDiscordClient;
import io.github.shadowchild.arcanine.Arcanine;
import io.github.shadowchild.arcanine.command.AbstractCommand;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Loader {

    public final URL JAR_FILE = Arcanine.class.getProtectionDomain().getCodeSource().getLocation();

    public GatewayDiscordClient client;

    public final String prefix = ">";

    public String accessToken = null;
    public boolean hasGui = false;

    public final URL internalCommandsFolder;

    public final List<AbstractCommand> commands = new LinkedList<>();

    private final String resourcesDir;

    public Loader(String resourcesDir) {

        // in this example resourcesDir is "assets"
        this.resourcesDir = resourcesDir;
        internalCommandsFolder = ClassLoadUtil.getSafeCL().getResource(resourcesDir + "/commands");
    }

    public void reloadCommands() throws IOException, URISyntaxException {

        commands.clear();

        List<Path> internal = filterCommandsFromPath(internalCommandsFolder);

        instanceCommandsFromArray(internal);
    }

    private List<Path> filterCommandsFromPath(URL url) throws URISyntaxException, IOException {

        Path myPath = FileUtil.getPath(url, resourcesDir + "/commands");
        return Files.walk(myPath).filter(cmd -> cmd.getFileName().toString().endsWith(".json")).collect(Collectors.toList());
    }

    private void instanceCommandsFromArray(List<Path> commands) throws IOException {

        for(Path path : commands) {

            JsonObject contents;

            contents = JsonUtil.getObjectFromPath(path);

            String clazz = JsonUtil.getString(contents, "class");
            JsonObject props = JsonUtil.getObject(contents, "command");

            try {

                this.commands.add(internalCommand(clazz, props));
            } catch(Exception e) {

                String name = JsonUtil.getString(props, "name");
                System.out.println("Could not load command: " + name + "\nError details:");
                e.printStackTrace();
            }
        }
    }

    private AbstractCommand internalCommand(String clazz, JsonObject props) throws ClassNotFoundException {

        Class<? extends AbstractCommand> loaded = ClassLoadUtil.loadClass(clazz);

        Gson gson = new Gson();

        AbstractCommand cmd = gson.fromJson(props, loaded);

        return cmd;
    }

    public static class BotConfig {

        public static boolean gui;
        public static String token;
    }

    public static class LoggerConfig {


    }

    public static class GuiConfig {


    }
}
