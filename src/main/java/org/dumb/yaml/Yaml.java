package org.dumb.yaml;

import org.dumb.yaml.domain.YamlList;
import org.dumb.yaml.domain.YamlMap;
import org.dumb.yaml.builder.ObjectBuilder;
import org.dumb.yaml.parser.YamlParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * Date: 11/23/13
 * Time: 12:36 PM
 * Thread safe YAML parsing/object mapping library
 *
 * @author Artem Prigoda
 */
public class Yaml {

    private YamlParser yamlParser = new YamlParser();
    private ObjectBuilder objectBuilder = new ObjectBuilder();
    private StreamAdapter streamAdapter = new StreamAdapter();

    @NotNull
    public YamlMap parse(@Nullable String yamlText) {
        return yamlParser.parse(streamAdapter.convert(yamlText), YamlMap.class);
    }

    @NotNull
    public YamlMap parse(@NotNull File file) {
        return yamlParser.parse(streamAdapter.convert(file), YamlMap.class);
    }

    @NotNull
    public YamlMap parse(@NotNull InputStream inputStream) {
        return yamlParser.parse(streamAdapter.convert(inputStream), YamlMap.class);
    }

    @NotNull
    public YamlMap parse(@NotNull BufferedReader reader) {
        return yamlParser.parse(streamAdapter.convert(reader), YamlMap.class);
    }

    @NotNull
    public YamlList parseList(@Nullable String yamlText) {
        return yamlParser.parse(streamAdapter.convert(yamlText), YamlList.class);
    }

    @NotNull
    public YamlList parseList(@NotNull File file) {
        return yamlParser.parse(streamAdapter.convert(file), YamlList.class);
    }

    @NotNull
    public YamlList parseList(@NotNull InputStream inputStream) {
        return yamlParser.parse(streamAdapter.convert(inputStream), YamlList.class);
    }

    @NotNull
    public YamlList parseList(@NotNull BufferedReader reader) {
        return yamlParser.parse(streamAdapter.convert(reader), YamlList.class);
    }

    @NotNull
    public <T> T parse(@Nullable String source, @NotNull Class<T> clazz) {
        return objectBuilder.build(parse(source), clazz);
    }

    @NotNull
    public <T> T parse(@NotNull File source, @NotNull Class<T> clazz) {
        return objectBuilder.build(parse(source), clazz);
    }

    @NotNull
    public <T> T parse(@NotNull InputStream source, @NotNull Class<T> clazz) {
        return objectBuilder.build(parse(source), clazz);
    }

    @NotNull
    public <T> T parse(@NotNull BufferedReader source, @NotNull Class<T> clazz) {
        return objectBuilder.build(parse(source), clazz);
    }

    @NotNull
    public <T> List<T> parseList(@Nullable String source, @NotNull Class<T> clazz) {
        return objectBuilder.build(parseList(source), clazz);
    }

    @NotNull
    public <T> List<T> parseList(@NotNull File source, @NotNull Class<T> clazz) {
        return objectBuilder.build(parseList(source), clazz);
    }

    @NotNull
    public <T> List<T> parseList(@NotNull InputStream source, @NotNull Class<T> clazz) {
        return objectBuilder.build(parseList(source), clazz);
    }

    @NotNull
    public <T> List<T> parseList(@NotNull BufferedReader source, @NotNull Class<T> clazz) {
        return objectBuilder.build(parseList(source), clazz);
    }

}
