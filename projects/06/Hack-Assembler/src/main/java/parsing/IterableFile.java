package parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;

public class IterableFile implements Iterable<String> {

    private final Path filePath;

    public IterableFile(Path filePath) {
        checkArgument(Files.exists(filePath), "The file %s does not exist.", filePath.toAbsolutePath());
        this.filePath = filePath;
    }

    private BufferedReader initReader() throws IOException {
        return Files.newBufferedReader(filePath);
    }

    @Override
    public Iterator<String> iterator() {
        try {
            return new FileIterator(initReader());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("The file " + filePath.getFileName() +
                    " could not be opened for reading.");
        }

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public String next() {
                return null;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        Iterator<String> iterator = iterator();
        iterator.forEachRemaining(action);

    }

    private static class FileIterator implements Iterator<String> {

        private final BufferedReader reader;

        public FileIterator(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public boolean hasNext() {
            try {
                return reader.ready();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public String next() {
            try {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
