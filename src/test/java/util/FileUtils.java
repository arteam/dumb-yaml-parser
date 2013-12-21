package util;

import java.io.*;

/**
 * Date: 11/23/13
 * Time: 1:30 AM
 *
 * @author Artem Prigoda
 */
public class FileUtils {

    public static String contents(String fileName) {
        String file = FileUtils.class.getResource(fileName).getFile();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            int size = (int) new File(relativeFileName(fileName)).length();
            char[] buf = new char[size];
            reader.read(buf);
            return new String(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static File file(String fileName) {
        return new File(FileUtils.class.getResource(fileName).getFile());
    }

    public static InputStream inputStream(String fileName) {
        try {
            return new FileInputStream(FileUtils.class.getResource(fileName).getFile());
        } catch (FileNotFoundException e) {
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
