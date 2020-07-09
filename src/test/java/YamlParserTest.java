import org.dumb.yaml.Yaml;
import org.dumb.yaml.domain.YamlList;
import org.dumb.yaml.domain.YamlMap;
import org.dumb.yaml.domain.YamlObject;
import org.dumb.yaml.domain.YamlPrimitive;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static util.FileUtils.contents;
import static util.FileUtils.inputStream;
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
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlPrimitive("val"));
            put("trash", new YamlPrimitive("talk"));
        }}));
    }

    @Test
    public void testParseYamlWithInnerMap() {
        YamlMap yamlMap = parser.parse(contents("/test2.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlMap(new HashMap<String, YamlObject>() {{
                put("test", new YamlPrimitive("1"));
                put("pen", new YamlPrimitive("2"));
            }}));
            put("trash", new YamlPrimitive("talk"));
        }}));
    }

    @Test
    public void testReturnToUpperLevel() {
        YamlMap yamlMap = parser.parse(contents("/test3.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("ter", new YamlMap(new HashMap<String, YamlObject>() {{
                put("ber", new YamlPrimitive("1"));
                put("gfer", new YamlMap(new HashMap<String, YamlObject>() {{
                    put("tger", new YamlPrimitive("2"));
                }}));
            }}));
            put("fer", new YamlPrimitive("12"));
        }}));
    }

    @Test
    public void testCheckInnerLevelLast() throws Exception {
        InputStream inputStream = inputStream("/test4.yml");
        YamlMap yamlMap = parser.parse(inputStream);
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
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
        }}));
        inputStream.close();
    }

    @Test
    public void testDifferentTextFormat() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream("/test5.yml")));
        YamlMap yamlMap = parser.parse(reader);
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("name", new YamlPrimitive("foo"));
            put("email", new YamlPrimitive("foo@mail.com"));
            put("age", new YamlPrimitive("12"));
            put("birth", new YamlPrimitive("Jun 01, 1985"));
        }}));
        reader.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYaml() {
        parser.parse(contents("/invalid.yml"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYaml2() {
        parser.parse(contents("/invalid2.yml"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYaml3() {
        parser.parse(contents("/invalid3.yml"));
    }

    @Test
    public void testList() {
        YamlMap yamlMap = parser.parse(contents("/test6.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("ids", new YamlList(new ArrayList<YamlObject>() {{
                add(new YamlPrimitive("1"));
                add(new YamlPrimitive("2"));
                add(new YamlPrimitive("3"));
            }}));
        }}));
    }

    @Test
    public void testInnerMap() {
        YamlMap yamlMap = parser.parse(contents("/test7.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("args", new YamlMap(new HashMap<String, YamlObject>() {{
                put("name", new YamlPrimitive("art"));
                put("pass", new YamlPrimitive("wolf"));
            }}));
        }}));
    }

    @Test
    public void testComments() {
        YamlMap yamlMap = parser.parse(contents("/test8.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlMap(new HashMap<String, YamlObject>() {{
                put("root", new YamlPrimitive("test"));
                put("end", new YamlPrimitive("val"));
            }}));
            put("man", new YamlList(new ArrayList<YamlObject>() {{
                add(new YamlPrimitive("page23"));
                add(new YamlPrimitive("page34"));
            }}));
        }}));
    }

    @Test
    public void testPlainList() {
        YamlMap yamlMap = parser.parse(contents("/test12.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("colors", new YamlList(new ArrayList<YamlObject>() {{
                add(new YamlPrimitive("orange"));
                add(new YamlPrimitive("red"));
                add(new YamlPrimitive("black"));
            }}));
        }}));
    }

    @Test
    public void testComplexList() {
        YamlMap yamlMap = parser.parse(contents("/test13.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("servers", new YamlList(new ArrayList<YamlObject>() {{
                add(new YamlMap(new HashMap<String, YamlObject>() {{
                    put("key", new YamlPrimitive("1"));
                    put("url", new YamlPrimitive("mwjb1.tv.megafon"));
                }}));
                add(new YamlMap(new HashMap<String, YamlObject>() {{
                    put("key", new YamlPrimitive("2"));
                    put("url", new YamlPrimitive("mwjb2.tv.megafon"));
                }}));
            }}));
        }}));
    }

    @Test
    public void testSuperComplexList() {
        YamlMap yamlMap = parser.parse(contents("/test14.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlList(new ArrayList<YamlObject>() {{
                add(new YamlMap(new HashMap<String, YamlObject>() {{
                    put("just", new YamlPrimitive("write some"));
                }}));
                add(new YamlMap(new HashMap<String, YamlObject>() {{
                    put("yaml", new YamlList(new ArrayList<YamlObject>() {{
                        add(new YamlList(new ArrayList<YamlObject>() {{
                            add(new YamlPrimitive("here"));
                            add(new YamlPrimitive("and"));
                        }}));
                        add(new YamlMap(new HashMap<String, YamlObject>() {{
                            put("it", new YamlPrimitive("updates"));
                            put("in", new YamlPrimitive("real-time"));
                        }}));
                    }}));
                }}));
            }}));
        }}));
    }

    @Test
    public void testRootList() {
        YamlList yamlList = parser.parseList(contents("/test19.yml"));
        System.out.println(yamlList);
        assertThat(yamlList).isEqualTo(new YamlList(new ArrayList<YamlObject>() {{
            add(new YamlMap(new HashMap<String, YamlObject>() {{
                put("id", new YamlPrimitive("mwjb1"));
                put("url", new YamlPrimitive("mwjb1.tv.megafon"));
            }}));
            add(new YamlMap(new HashMap<String, YamlObject>() {{
                put("id", new YamlPrimitive("mwjb2"));
                put("url", new YamlPrimitive("mwjb2.tv.megafon"));
            }}));
        }}));
    }

    @Test
    public void testRootPrimitiveList() {
        YamlList yamlList = parser.parseList(contents("/test20.yml"));
        System.out.println(yamlList);
        assertThat(yamlList).isEqualTo(new YamlList(new ArrayList<YamlObject>() {{
            add(new YamlPrimitive("red"));
            add(new YamlPrimitive("orange"));
            add(new YamlPrimitive("black"));
        }}));
    }

    @Test(expected = IllegalStateException.class)
    public void testListAndMap() {
        parser.parse(contents("/test15.yml"));
    }

    @Test
    public void testBrackets() {
        YamlMap yamlMap = parser.parse(contents("/test16.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlList(new ArrayList<YamlObject>() {{
                add(new YamlMap(new HashMap<String, YamlObject>() {{
                    put("it", new YamlPrimitive("updates}"));
                }}));
                add(new YamlMap(new HashMap<String, YamlObject>() {{
                    put("it", new YamlList(new ArrayList<YamlObject>() {{
                        add(new YamlPrimitive("updates"));
                    }}));
                }}));
                add(new YamlMap(new HashMap<String, YamlObject>() {{
                    put("it", new YamlPrimitive("updates]"));
                }}));
                add(new YamlMap(new HashMap<String, YamlObject>() {{
                    put("it", new YamlMap(new HashMap<String, YamlObject>() {{
                        put("up", new YamlPrimitive("dates"));
                    }}));
                }}));
            }}));
        }}));
    }

    @Test
    public void testListAfterList() {
        YamlMap yamlMap = parser.parse(contents("/test17.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlList(new ArrayList<YamlObject>() {{
                Map<String, YamlObject> map = new HashMap<String, YamlObject>();
                map.put("it", new YamlList(new ArrayList<YamlObject>() {{
                    add(new YamlPrimitive("1"));
                    add(new YamlPrimitive("2"));
                    add(new YamlPrimitive("3"));
                }}));
                add(new YamlMap(map));
                add(new YamlPrimitive("4"));
                add(new YamlPrimitive("5"));
            }}));
        }}));
    }

    @Test
    public void testStringEscaping() {
        YamlMap yamlMap = parser.parse(contents("/test18.yml"));
        System.out.println(yamlMap);
        assertThat(yamlMap).isEqualTo(new YamlMap(new HashMap<String, YamlObject>() {{
            put("key", new YamlPrimitive(" val 1 "));
            put("val", new YamlPrimitive("val:2"));
            put("empty", new YamlPrimitive(""));
            put("comment", new YamlPrimitive("1 # comment"));
            put("comment2", new YamlPrimitive("1# comment"));
            put("comment3", new YamlPrimitive("1"));
        }}));
    }
}
