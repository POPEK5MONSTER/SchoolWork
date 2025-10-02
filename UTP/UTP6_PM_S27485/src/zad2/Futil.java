package zad2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {


    public static void processDir(String dirName, String resultFileName) {
        Path startDir = Paths.get(dirName);
        Path resultFilePatch = Paths.get(resultFileName);
        try (BufferedWriter bw = Files.newBufferedWriter(resultFilePatch, StandardCharsets.UTF_8)){
            Files.walkFileTree(startDir, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".txt")) {
                        readAndWrite(file, bw);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            } catch (IOException e){
                e.printStackTrace();
            }
    }
    private static void readAndWrite(Path filePatch, BufferedWriter bw){
        try(BufferedReader br = Files.newBufferedReader(filePatch, Charset.forName("Cp1250"))){
            String line;
            while ((line = br.readLine()) != null){
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

