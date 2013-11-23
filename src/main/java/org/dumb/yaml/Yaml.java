package org.dumb.yaml;

import org.dumb.yaml.domain.YamlMap;
import org.dumb.yaml.builder.ObjectBuilder;
import org.dumb.yaml.parser.YamlParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;

/**
 * Date: 11/23/13
 * Time: 12:36 PM
 * YAML parse library
 *
 * @author Artem Prigoda
 */
public class Yaml {

    private YamlParser yamlParser = new YamlParser();
    private ObjectBuilder objectBuilder = new ObjectBuilder();
    private StreamAdapter streamAdapter = new StreamAdapter();

    public YamlMap parse(String yamlText) {
        return yamlParser.parse(streamAdapter.convert(yamlText));
    }

    public YamlMap parse(File file) {
        return yamlParser.parse(streamAdapter.convert(file));
    }

    public YamlMap parse(InputStream inputStream) {
        return yamlParser.parse(streamAdapter.convert(inputStream));
    }

    public YamlMap parse(BufferedReader reader) {
        return yamlParser.parse(streamAdapter.convert(reader));
    }

    public <T> T parse(String source, Class<T> clazz) {
        return objectBuilder.build(parse(source), clazz);
    }

    public <T> T parse(File source, Class<T> clazz) {
        return objectBuilder.build(parse(source), clazz);
    }

    public <T> T parse(InputStream source, Class<T> clazz) {
        return objectBuilder.build(parse(source), clazz);
    }

    public <T> T parse(BufferedReader source, Class<T> clazz) {
        return objectBuilder.build(parse(source), clazz);
    }

}
