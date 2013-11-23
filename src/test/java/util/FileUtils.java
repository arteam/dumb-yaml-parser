package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Date: 11/23/13
 * Time: 1:30 AM
 *
 * @author Artem Prigoda
 */
public class FileUtils {

    public static String file(String fileName) {
        String file = FileUtils.class.getResource(fileName).getFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int size = (int) Files.size(Paths.get(file));
            char[] buf = new char[size];
            reader.read(buf);
            return new String(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}