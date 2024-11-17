package backend.academy.egfedo.data;

import java.util.List;
import java.util.stream.Stream;

public record FileData(
    List<String> fileNames,
    Stream<String> fileLines
) implements AutoCloseable {

    @Override
    public void close() {
        fileLines.close();
    }
}
