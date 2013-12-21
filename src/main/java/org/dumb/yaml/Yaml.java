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
import java.util.Map;

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
    public YamlMap parse(@Nullable String source) {
        return yamlParser.parse(streamAdapter.convert(source), YamlMap.class);
    }

    @NotNull
    public YamlMap parse(@NotNull File source) {
        return yamlParser.parse(streamAdapter.convert(source), YamlMap.class);
    }

    @NotNull
    public YamlMap parse(@NotNull InputStream source) {
        return yamlParser.parse(streamAdapter.convert(source), YamlMap.class);
    }

    @NotNull
    public YamlMap parse(@NotNull BufferedReader source) {
        return yamlParser.parse(streamAdapter.convert(source), YamlMap.class);
    }

    @NotNull
    public YamlList parseList(@NotNull String source) {
        return yamlParser.parse(streamAdapter.convert(source), YamlList.class);
    }

    @NotNull
    public YamlList parseList(@NotNull File source) {
        return yamlParser.parse(streamAdapter.convert(source), YamlList.class);
    }

    @NotNull
    public YamlList parseList(@NotNull InputStream source) {
        return yamlParser.parse(streamAdapter.convert(source), YamlList.class);
    }

    @NotNull
    public YamlList parseList(@NotNull BufferedReader source) {
        return yamlParser.parse(streamAdapter.convert(source), YamlList.class);
    }

    @NotNull
    public <T> T parse(@NotNull String source, @NotNull Class<T> clazz) {
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
    public <T> Map<String, T> parseMap(@NotNull String source, @NotNull Class<T> clazz) {
        return objectBuilder.buildMap(parse(source), clazz);
    }

    @NotNull
    public <T> Map<String, T> parseMap(@NotNull File source, @NotNull Class<T> clazz) {
        return objectBuilder.buildMap(parse(source), clazz);
    }

    @NotNull
    public <T> Map<String, T> parseMap(@NotNull InputStream source, @NotNull Class<T> clazz) {
        return objectBuilder.buildMap(parse(source), clazz);
    }

    @NotNull
    public <T> Map<String, T> parseMap(@NotNull BufferedReader source, @NotNull Class<T> clazz) {
        return objectBuilder.buildMap(parse(source), clazz);
    }

    @NotNull
    public <T> List<T> parseList(@NotNull String source, @NotNull Class<T> clazz) {
        return objectBuilder.buildList(parseList(source), clazz);
    }

    @NotNull
    public <T> List<T> parseList(@NotNull File source, @NotNull Class<T> clazz) {
        return objectBuilder.buildList(parseList(source), clazz);
    }

    @NotNull
    public <T> List<T> parseList(@NotNull InputStream source, @NotNull Class<T> clazz) {
        return objectBuilder.buildList(parseList(source), clazz);
    }

    @NotNull
    public <T> List<T> parseList(@NotNull BufferedReader source, @NotNull Class<T> clazz) {
        return objectBuilder.buildList(parseList(source), clazz);
    }


}
