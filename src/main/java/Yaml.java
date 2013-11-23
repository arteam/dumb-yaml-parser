import domain.YamlMap;
import builder.ObjectBuilder;
import parser.YamlParser;

/**
 * Date: 11/23/13
 * Time: 12:36 PM
 *
 * @author Artem Prigoda
 */
public class Yaml {

    private YamlParser yamlParser = new YamlParser();
    private ObjectBuilder objectBuilder = new ObjectBuilder();

    public YamlMap parse(String source) {
        return yamlParser.parse(source);
    }

    public <T> T parse(String source, Class<T> clazz) {
        YamlMap yamlMap = yamlParser.parse(source);
        return objectBuilder.build(yamlMap, clazz);
    }
}
