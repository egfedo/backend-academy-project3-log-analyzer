package backend.academy.egfedo.file;

import backend.academy.egfedo.data.FileData;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static java.nio.file.FileVisitResult.CONTINUE;

public class FileSystemLoader {

    public FileData getLines(String template) throws IOException {

        Path start;
        if (template.startsWith("/")) {
            Path templatePath = Path.of(template);
            Path root = Path.of("/");
            int i = 1;
            for (; i <= templatePath.getNameCount(); i++) {
                var subpath = root.resolve(templatePath.subpath(0, i));
                if (!Files.exists(subpath)) {
                    break;
                }
            }
            start =  root.resolve(templatePath.subpath(0, i - 1));
        } else {
            start = Path.of(".");
        }

        Finder finder = new Finder(template);

        Files.walkFileTree(start, finder);
        Stream<Path> stream = finder.getFiles().stream();

        var fileNames = stream.filter(Files::isRegularFile)
            .map(Path::toString)
            .toList();

        stream.close();

        stream = fileNames.stream().map(Path::of);

        return new FileData(
            fileNames,
            stream.flatMap(file -> {
                try {
                    return Files.lines(file);
                } catch (IOException e) {
                    throw new RuntimeException("Ошибка чтения файла " + file, e);
                }
            }));
    }

    private static class Finder
        extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;

        private final List<Path> files = new ArrayList<>();

        public List<Path> getFiles() {
            return Collections.unmodifiableList(files);
        }

        Finder(String pattern) {
            matcher = FileSystems.getDefault()
                .getPathMatcher("glob:" + pattern);
        }

        void find(Path file) {
            if (matcher.matches(file)) {
                files.add(file);
            }
        }

        @Override
        public FileVisitResult visitFile(Path file,
            BasicFileAttributes attrs) {
            find(file);
            return CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir,
            BasicFileAttributes attrs) {
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file,
            IOException exc) {
            System.err.println(exc.getMessage());
            return CONTINUE;
        }
    }
}
