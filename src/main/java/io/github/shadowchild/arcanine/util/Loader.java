package io.github.shadowchild.arcanine.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.github.shadowchild.arcanine.Arcanine;
import io.github.shadowchild.arcanine.command.AbstractCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class Loader {

    public final File externalCommandsFolder;
    public final File internalCommandsFolder;

    public final List<AbstractCommand> commands = new LinkedList<>();

    public Loader(String resourcesDir) {

        // in this example resourcesDir is "assets"
        externalCommandsFolder = new File(resourcesDir, "commands");
        internalCommandsFolder = new File(Thread.currentThread().getContextClassLoader().getResource(resourcesDir + "/commands").getPath());
        System.out.println(externalCommandsFolder.getAbsolutePath());
    }

    public void reloadCommands() throws FileNotFoundException {

        File[] internal = internalCommandsFolder.listFiles((file) -> file.getName().endsWith(".json"));

        File[] external = externalCommandsFolder.listFiles((file) -> file.getName().endsWith(".json"));

        if(internal != null) {

            instanceCommandsFromArray(internal, true);
        }
        if(external != null) {

            instanceCommandsFromArray(external, false);
        }
    }

    private void instanceCommandsFromArray(File[] commands, boolean internal) throws FileNotFoundException {

        for(File f : commands) {

            JsonObject contents;

            FileReader fr = new FileReader(f);
            JsonReader jr = new JsonReader(fr);

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
