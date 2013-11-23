import domain.YamlList;
import domain.YamlMap;
import domain.YamlObject;
import domain.YamlPrimitive;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static util.FileUtils.file;
import static util.FileUtils.relativeFileName;

/**
 * Date: 11/20/13
 * Time: 8:08 PM
 *
 * @author Artem Prigoda
 */
public class YamlParserTest {

    Yaml parser = new Yaml();

    @Test
    public void testParseBaseYaml() {
        YamlMap yamlMap = parser.parse(new File(relativeFileName("/test.yml")));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlPrimitive("val"));
            put("trash", new YamlPrimitive("talk"));
        }})));
    }

    @Test
    public void testParseYamlWithInnerMap() {
        YamlMap yamlMap = parser.parse(file("/test2.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlMap(new HashMap<String, YamlObject>() {{
                put("test", new YamlPrimitive("1"));
                put("pen", new YamlPrimitive("2"));
            }}));
            put("trash", new YamlPrimitive("talk"));
        }})));
    }

    @Test
    public void testReturnToUpperLevel() {
        YamlMap yamlMap = parser.parse(file("/test3.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("ter", new YamlMap(new HashMap<String, YamlObject>() {{
                put("ber", new YamlPrimitive("1"));
                put("gfer", new YamlMap(new HashMap<String, YamlObject>() {{
                    put("tger", new YamlPrimitive("2"));
                }}));
            }}));
            put("fer", new YamlPrimitive("12"));
        }})));
    }

    @Test
    public void testCheckInnerLevelLast() throws Exception {
        FileInputStream inputStream = new FileInputStream(relativeFileName("/test4.yml"));
        YamlMap yamlMap = parser.parse(inputStream);
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("data_bases", new YamlMap(new HashMap<String, YamlObject>() {{
                put("cms", new YamlMap(new HashMap<String, YamlObject>() {{
                    put("db_driver", new YamlPrimitive("Post gres"));
                    put("url", new YamlPrimitive("52.15:cms"));
                }}));
                put("sms", new YamlMap(new HashMap<String, YamlObject>() {{
                    put("db_driver", new YamlPrimitive("Post gres"));
                    put("url", new YamlPrimitive("52.15:sms"));
                }}));
            }}));
        }})));
        inputStream.close();
    }

    @Test
    public void testDifferentTextFormat() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(relativeFileName("/test5.yml"))));
        YamlMap yamlMap = parser.parse(reader);
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("name", new YamlPrimitive("\"foo\""));
            put("email", new YamlPrimitive("foo@mail.com"));
            put("age", new YamlPrimitive("12"));
            put("birth", new YamlPrimitive("Jun 01, 1985"));
        }})));
        reader.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYaml() {
        parser.parse(file("/invalid.yml"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYaml2() {
        parser.parse(file("/invalid2.yml"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYaml3() {
        parser.parse(file("/invalid3.yml"));
    }

    @Test
    public void testList() {
        YamlMap yamlMap = parser.parse(file("/test6.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("ids", new YamlList(new ArrayList<YamlObject>() {{
                add(new YamlPrimitive("1"));
                add(new YamlPrimitive("2"));
                add(new YamlPrimitive("3"));
            }}));
        }})));
    }

    @Test
    public void testInnerMap() {
        YamlMap yamlMap = parser.parse(file("/test7.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("args", new YamlMap(new HashMap<String, YamlObject>() {{
                put("name", new YamlPrimitive("art"));
                put("pass", new YamlPrimitive("wolf"));
            }}));
        }})));
    }

    @Test
    public void testComments() {
        YamlMap yamlMap = parser.parse(file("/test8.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap, equalTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlMap(new HashMap<String, YamlObject>() {{
                put("root", new YamlPrimitive("test"));
                put("end", new YamlPrimitive("val"));
            }}));
            put("man", new YamlList(new ArrayList<YamlObject>() {{
                add(new YamlPrimitive("page23"));
                add(new YamlPrimitive("page34"));
            }}));
        }})));
    }

}
