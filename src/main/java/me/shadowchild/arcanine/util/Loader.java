package me.shadowchild.arcanine.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import me.shadowchild.arcanine.Arcanine;
import me.shadowchild.arcanine.command.template.AbstractCommand;
import me.shadowchild.cybernize.registry.NamedRegistry;
import me.shadowchild.cybernize.util.ClassLoadUtil;
import me.shadowchild.cybernize.util.FileUtil;
import me.shadowchild.cybernize.util.JsonUtil;
import net.dv8tion.jda.api.JDA;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Loader {

//    public final URL JAR_FILE = Arcanine.class.getProtectionDomain().getCodeSource().getLocation();

    public BotConfig botCfg;
    public LoggerConfig loggerCfg;
    public GuiConfig guiConfig;

    public JDA client;
//    public GatewayDiscordClient client;

    private URL intCmdFldr, externCmdFldr, configUrl;

    public final NamedRegistry<AbstractCommand> commands = new NamedRegistry<>();

    private final String resourcesDir;

    public Loader(String resourcesDir) {

        // in this example resourcesDir is "assets"
        this.resourcesDir = resourcesDir;
        intCmdFldr = ClassLoadUtil.getSafeCL().getResource(resourcesDir + "/commands");
        configUrl = ClassLoadUtil.getSafeCL().getResource(resourcesDir + "/configs/Config.json");
    }

    public void reloadCommands() throws IOException, URISyntaxException {

        commands.clearRegistry();

        List<Path> internal = filterCommandsFromPath(intCmdFldr);

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

                AbstractCommand cmd = internalCommand(clazz, props);
                Arcanine.LOGGER.info("Loaded command: " + cmd.getName());
                this.commands.register(cmd.getName().toLowerCase(), cmd);
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

    public void loadConfig() throws IOException, URISyntaxException {

        System.out.println(configUrl.toURI());
        File file  = ArcanineFileUtils.urlToFile(configUrl);
//        Path path = FileUtil.getPath(configUrl, resourcesDir + "/configs/Config.json");

        JsonReader reader = new JsonReader(new FileReader(file));

        JsonObject contents = JsonParser.parseReader(reader).getAsJsonObject();

        Gson gson = new Gson();

        JsonObject bot = JsonUtil.getObject(contents, "bot");
        JsonObject logger = JsonUtil.getObject(contents, "logger");
        JsonObject gui = JsonUtil.getObject(contents, "gui");

        botCfg = gson.fromJson(bot, BotConfig.class);
        loggerCfg = gson.fromJson(logger, LoggerConfig.class);
        guiConfig = gson.fromJson(gui, GuiConfig.class);
    }

    public class BotConfig {

        public boolean gui;
        public String token;
        public String prefix;
        public String tzdb_token;
    }

    public class LoggerConfig {


    }

    public class GuiConfig {


    }
}
