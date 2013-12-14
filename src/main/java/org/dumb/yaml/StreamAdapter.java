package org.dumb.yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @NotNull
    public List<String> convert(@NotNull File file) {
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

    @NotNull
    public List<String> convert(@NotNull InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return convert(reader);
    }

    @NotNull
    public List<String> convert(@NotNull BufferedReader reader) {
        List<String> lines = new ArrayList<String>();
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

    @NotNull
    public List<String> convert(@Nullable String yamlText) {
        if (yamlText == null) return new ArrayList<String>();
        return Arrays.asList(yamlText.split("\n"));
    }
}
