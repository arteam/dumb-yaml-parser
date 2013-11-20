import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Date: 11/20/13
 * Time: 8:08 PM
 *
 * @author Artem Prigoda
 */
public class YamlParserTest {

    YamlParser parser = new YamlParser();

    @Test
    public void testParseBaseYaml()  {
        YamlMap yamlMap = parser.parse(file("/test.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlPrimitive("val"));
            put("trash", new YamlPrimitive("talk"));
        }})));
    }

    @Test
    public void testParseYamlWithInnerMap()  {
        YamlMap yamlMap = parser.parse(file("/test2.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlMap(new HashMap<String, YamlObject>(){{
                put("test", new YamlPrimitive("1"));
                put("pen", new YamlPrimitive("2"));
            }}));
            put("trash", new YamlPrimitive("talk"));
        }})));
    }

    @Test
    public void testReturnToUpperLevel()  {
        YamlMap yamlMap = parser.parse(file("/test3.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("ter", new YamlMap(new HashMap<String, YamlObject>(){{
                put("ber", new YamlPrimitive("1"));
                put("gfer", new YamlMap(new HashMap<String, YamlObject>(){{
                    put("tger", new YamlPrimitive("2"));
                }}));
            }}));
            put("fer", new YamlPrimitive("12"));
        }})));
    }

    @Test
    public void testCheckInnerLevelLast()  {
        YamlMap yamlMap = parser.parse(file("/test4.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("data_bases", new YamlMap(new HashMap<String, YamlObject>(){{
                put("cms", new YamlMap(new HashMap<String, YamlObject>(){{
                    put("db_driver", new YamlPrimitive("Post gres"));
                    put("url", new YamlPrimitive("52.15:cms"));
                }}));
                put("sms", new YamlMap(new HashMap<String, YamlObject>(){{
                    put("db_driver", new YamlPrimitive("Post gres"));
                    put("url", new YamlPrimitive("52.15:sms"));
                }}));
            }}));
        }})));
    }

    @Test
    public void testDifferentTextFormat()  {
        YamlMap yamlMap = parser.parse(file("/test5.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("name", new YamlPrimitive("\"foo\""));
            put("email", new YamlPrimitive("foo@mail.com"));
            put("age", new YamlPrimitive("12"));
            put("birth", new YamlPrimitive("Jun 01, 1985"));
        }})));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYaml()  {
         parser.parse(file("/invalid.yml"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYaml2()  {
        parser.parse(file("/invalid2.yml"));
    }

    private static String file(String fileName) {
        String file = YamlParserTest.class.getResource(fileName).getFile();
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
