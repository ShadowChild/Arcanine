package io.github.shadowchild.arcanine.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class FileUtil {

    public static Path getPath(URL url, String resource) throws IOException, URISyntaxException {

        URI uri = url.toURI();
        Path path;
        if(uri.getScheme().equals("jar")) {

            // Internal
            FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
            path = fs.getPath(resource);
        } else {

            // External
            path = Paths.get(uri);
        }
        return path;
    }
}
