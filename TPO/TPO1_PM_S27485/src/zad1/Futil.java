package zad1;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    public static void processDir(String dirName, String resultFileName) {
        Path dir = Paths.get(dirName);
        Path resultFile = Paths.get(resultFileName);

        try (FileChannel outputChannel = FileChannel.open(resultFile, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        try (FileChannel inputChannel = FileChannel.open(file, StandardOpenOption.READ)) {
                            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                            inputChannel.read(buffer);
                            buffer.flip();
                            CharBuffer decoded = Charset.forName("cp1250").decode(buffer);
                            outputChannel.write(StandardCharsets.UTF_8.encode(decoded));
                        }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
