/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Finder {
    private final String fname;
    public Finder(String fname) {
        this.fname = fname;
    }

    public int getIfCount() throws IOException {
        int ifCount = 0;
        boolean insideComment1 = false;
        boolean insideComment2 = false;
        try(BufferedReader br = new BufferedReader(new FileReader(fname))){
            String line;
            while ((line = br.readLine()) != null){
                line = line.trim();

                if (line.contains("/*")){
                    insideComment1 = true;
                }
                if (line.contains("*/")){
                    insideComment1 = false;
                }
                if (line.contains("//")){
                    line = line.split("//")[0].trim();
                }
                insideComment2 = !insideComment2 && line.matches(".*\".*\\bif\\b.*\".*");

                if (insideComment1 || insideComment2){
                    continue;
                }
                String[] statements = line.split(";");
                    for (String statement : statements){
                        if (containsFunctionalIf(statement) && !insideComment1){
                            if (count(statement, "if") > 1){
                                ifCount += count(statement, "if") - 1;
                            }

                            ifCount++;
                        }
                    }
            }
        }
        return ifCount;
    }
    private boolean containsFunctionalIf (String line){
        return line.matches(".*\\bif\\b.*[^\\}]$");

    }
    public int getStringCount(String target) throws IOException {
        int stringCount = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(fname))) {
            String line;
            while ((line = br.readLine()) != null){
                stringCount += count(line, target);
            }
        }
        return stringCount;
    }

    private int count(String line, String target){
        int count = 0;
        int index = 0;
        while ((index = line.indexOf(target, index)) != -1){
            count++;
            index += target.length();
        }
        return count;
    }
}
