package util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Date: 11/23/13
 * Time: 1:30 AM
 *
 * @author Artem Prigoda
 */
public class FileUtils {

    public static String contents(String fileName) {
        try (InputStream is = FileUtils.class.getResource(fileName).openStream();
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            char[] buf = new char[4096];
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                int read = reader.read(buf);
                if (read == -1) {
                    break;
                }
                stringBuilder.append(buf, 0, read);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static File file(String fileName) {
        return new File(FileUtils.class.getResource(fileName).getFile());
    }

    public static InputStream inputStream(String fileName) {
        try {
            return FileUtils.class.getResource(fileName).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedReader reader(String fileName) {
        try {
            return new BufferedReader(new FileReader(FileUtils.class.getResource(fileName).getFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String relativeFileName(String fileName) {
        return FileUtils.class.getResource(fileName).getFile();
    }
}
