import domain.YamlMap;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 11/23/13
 * Time: 6:10 PM
 * Adapter to convert different input data formats to a list of lines
 *
 * @author Artem Prigoda
 */
class StreamAdapter {

    public List<String> convert(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            return convert(reader);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable read yaml file: " + file, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new IllegalArgumentException("Unable read yaml file: " + file, e);
                }
            }
        }
    }

    public List<String> convert(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return convert(reader);
    }

    public List<String> convert(BufferedReader reader) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable read yaml file", e);
        }
        return lines;
    }

    public List<String> convert(String yamlText) {
        return Arrays.asList(yamlText.split("\n"));
    }
}
