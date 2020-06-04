package io.github.shadowchild.arcanine.util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.github.shadowchild.arcanine.Arcanine;
import io.github.shadowchild.arcanine.command.AbstractCommand;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Loader {

    public URL externalCommandsFolder;
    public final URL internalCommandsFolder;

    public final List<AbstractCommand> commands = new LinkedList<>();

    private String resourcesDir;

    public Loader(String resourcesDir) {

        // in this example resourcesDir is "assets"
        this.resourcesDir = resourcesDir;
        try {

            externalCommandsFolder = new File(resourcesDir, "commands").toURI().toURL();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        internalCommandsFolder = Thread.currentThread().getContextClassLoader().getResource(resourcesDir + "/commands");
    }

    public void reloadCommands() throws IOException, URISyntaxException {

        commands.clear();

        List<Path> internal = filterInternalCommands(internalCommandsFolder);

//        File[] internal = internalCommandsFolder.listFiles((file) -> file.getName().endsWith(".json"));

        List<Path> external = filterInternalCommands(externalCommandsFolder);

        if(internal != null) {

            instanceCommandsFromArray(internal, true);
        }
        if(external != null) {

            instanceCommandsFromArray(external, false);
        }
    }

    private List<Path> filterInternalCommands(URL url) throws URISyntaxException, IOException {

        URI uri = url.toURI();
        Path myPath;
        if(uri.getScheme().equals("jar")) {

            FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
            myPath = fs.getPath(resourcesDir + "/commands");
        } else {

            myPath = Paths.get(uri);
        }
        List<Path> ret = Files.walk(myPath).filter(cmd -> cmd.getFileName().toString().endsWith(".json")).collect(Collectors.toList());
        if(ret == null) return Lists.newArrayList();
        else return ret;
    }

    private void instanceCommandsFromArray(List<Path> commands, boolean internal) throws IOException {

        for(Path path : commands) {

            JsonObject contents;

            BufferedReader br = Files.newBufferedReader(path);
//            FileReader fr = new FileReader(path);
            JsonReader jr = new JsonReader(br);

            JsonParser jp = new JsonParser();
            JsonElement element = jp.parse(jr);

            contents = element.getAsJsonObject();

            String clazz = contents.get("class").getAsString();
            System.out.println(clazz);
            JsonObject props = contents.getAsJsonObject("command");

            try {

                if (internal) {

                    // As it is internal, we don't need to load into classpath, as it's already there
                    this.commands.add(internalCommand(clazz, props));
                } else {

                    this.commands.add(externalCommand(clazz, props));
                }
            } catch(Exception e) {

                String name = props.get("name").getAsString();
                System.out.println("Could not load command: " + name + "\nError details:");
                e.printStackTrace();
            }
        }
    }

    private AbstractCommand externalCommand(String clazz, JsonObject props) throws ClassNotFoundException {
        return null;
    }

    private AbstractCommand internalCommand(String clazz, JsonObject props) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<? extends AbstractCommand> loaded = (Class<? extends AbstractCommand>)Arcanine.class.getClassLoader().loadClass(clazz);

        Gson gson = new Gson();

        AbstractCommand cmd = gson.fromJson(props, loaded);

        return cmd;
    }
}
