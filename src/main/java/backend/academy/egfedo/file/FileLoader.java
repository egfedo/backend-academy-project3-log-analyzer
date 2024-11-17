package backend.academy.egfedo.file;

import backend.academy.egfedo.data.FileData;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class FileLoader implements AutoCloseable {

    private FileSystemLoader fileSystemLoader;
    private HTTPLoader httpLoader;

    @Override
    public void close() {
        if (Objects.nonNull(httpLoader)) {
            httpLoader.close();
        }
    }

    public FileData getFileLines(String path) throws URISyntaxException, IOException, InterruptedException {
        boolean isUrl = path.startsWith("http://") || path.startsWith("https://");
        if (isUrl) {
            httpLoader = new HTTPLoader();
            return new FileData(List.of(path), httpLoader.getLines(new URI(path)));
        } else {
            fileSystemLoader = new FileSystemLoader();
            return fileSystemLoader.getLines(path);
        }
    }

}
