package backend.academy.egfedo.file;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FileSystemLoaderTest {

    @Test
    void fsLoader_loadsTheFile() throws IOException {
        FileSystemLoader fsLoader = new FileSystemLoader();

        List<String> expected = List.of(
          "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
          "93.180.71.3 - - [17/May/2015:08:05:23 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
          "80.91.33.133 - - [17/May/2015:08:05:24 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.17)\""
        );

        try (var files = fsLoader.getLines("./src/test/resources/log.txt")) {
            var list = files.fileLines().toList();
            assertThat(list).isEqualTo(expected);
        }
    }

    @Test
    void fsLoader_globWorks() throws IOException {
        FileSystemLoader fsLoader = new FileSystemLoader();

        List<String> expected = List.of(
            "./src/test/resources/log.txt",
            "./src/test/resources/log2.txt"
        );

        try (var stream = fsLoader.getLines("./src/test/resources/*.txt")) {
            var list = stream.fileNames();
            assertThat(list).containsAll(expected);
        }
    }
}
