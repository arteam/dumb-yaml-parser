package org.dumb.yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date: 11/23/13
 * Time: 6:10 PM
 * Adapter to convert different input data formats to a list of lines
 *
 * @author Artem Prigoda
 */
class StreamAdapter {

    @NotNull
    List<String> convert(@NotNull File file) {
        try (FileInputStream is = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            return convert(reader);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable read yaml file: " + file, e);
        }
    }

    @NotNull
    List<String> convert(@NotNull InputStream stream) {
        try (InputStreamReader isr = new InputStreamReader(stream);
             BufferedReader reader = new BufferedReader(isr)) {
            return convert(reader);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to process I/O stream", e);
        }
    }

    @NotNull
    List<String> convert(@NotNull BufferedReader reader) {
        return reader.lines().collect(Collectors.toList());
    }

    @NotNull
    List<String> convert(@Nullable String yamlText) {
        if (yamlText == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(yamlText.split("\n"));
    }
}
