import org.dumb.yaml.Yaml;
import org.junit.Assert;
import org.junit.Test;
import test.data.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.IsEqual.equalTo;
import static util.FileUtils.*;

/**
 * Date: 11/20/13
 * Time: 8:08 PM
 *
 * @author Artem Prigoda
 */
public class YamlObjectTest {

    Yaml parser = new Yaml();

    @Test
    public void testParseStringPrimitives() {
        StringPrimitives test = parser.parse(contents("/test.yml"), StringPrimitives.class);
        System.out.println(test);
        Assert.assertEquals(test, new StringPrimitives("val", "talk"));
    }

    @Test
    public void testInjectByFields() {
        StringPrimitivesPlain test = parser.parse(contents("/test.yml"), StringPrimitivesPlain.class);
        System.out.println(test);
        StringPrimitivesPlain plain = new StringPrimitivesPlain();
        plain.setKey("val");
        plain.setTrash("talk");
        Assert.assertEquals(test, plain);
    }

    @Test
    public void testInjectByFieldsWithAnnotation() {
        StringPrimitivesAnnotation test = parser.parse(contents("/test.yml"), StringPrimitivesAnnotation.class);
        System.out.println(test);
        StringPrimitivesAnnotation plain = new StringPrimitivesAnnotation();
        plain.setKeyValue("val");
        plain.setTrashValue("talk");
        Assert.assertEquals(test, plain);
    }

    @Test
    public void testParseNumericPrimitives() {
        NumericPrimitives test = parser.parse(contents("/test9.yml"), NumericPrimitives.class);
        System.out.println(test);
        Assert.assertThat(test, equalTo(
                new NumericPrimitives(21341241234123412L, 12121232341231231L, -1233, -3322,
                        1231231.234412421312d, 2344542.123544322323d, -323.121f, -111.333f, false, true)));
    }

    @Test
    public void testMapInject() {
        MapInject test = parser.parse(contents("/test2.yml"), MapInject.class);
        System.out.println(test);
        Assert.assertEquals(test, new MapInject(new MapInject.Key(1, 2), "talk"));
    }

    @Test
    public void testListInject() {
        IdList test = parser.parse(contents("/test6.yml"), IdList.class);
        System.out.println(test);
        Assert.assertEquals(test, new IdList(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testInnerMap() {
        DBConfig test = parser.parse(contents("/test4.yml"), DBConfig.class);
        System.out.println(test);
        Assert.assertEquals(test, new DBConfig(new HashMap<String, DBConfig.Database>() {{
            put("cms", new DBConfig.Database("Post gres", "52.15:cms"));
            put("sms", new DBConfig.Database("Post gres", "52.15:sms"));
        }}));
    }

    @Test
    public void testNamesAnnotationOnClass() {
        DBConfigNames test = parser.parse(contents("/test4.yml"), DBConfigNames.class);
        System.out.println(test);
        Assert.assertEquals(test, new DBConfigNames(new HashMap<String, DBConfigNames.Database>() {{
            put("cms", new DBConfigNames.Database("Post gres", "52.15:cms"));
            put("sms", new DBConfigNames.Database("Post gres", "52.15:sms"));
        }}));
    }

    @Test
    public void testUnsafeInstanceOnClass() {
        DBConfigUnsafe test = parser.parse(contents("/test4.yml"), DBConfigUnsafe.class);
        System.out.println(test);
        Assert.assertEquals(test, new DBConfigUnsafe(new HashMap<String, DBConfigUnsafe.Database>() {{
            put("cms", new DBConfigUnsafe.Database("Post gres", "52.15:cms"));
            put("sms", new DBConfigUnsafe.Database("Post gres", "52.15:sms"));
        }}));
    }

    @Test
    public void testPerson() {
        Person test = parser.parse(contents("/test10.yml"), Person.class);
        System.out.println(test);
        Assert.assertEquals(test, new Person("foo", "foo@mail.com", 12,
                Arrays.asList(Person.JobType.VOD, Person.JobType.TV), Person.Network.OTT));
    }

    @Test
    public void testPersonNamesOnConstructor() {
        PersonNames test = parser.parse(contents("/test10.yml"), PersonNames.class);
        System.out.println(test);
        Assert.assertEquals(test, new PersonNames("foo", "foo@mail.com", 12,
                Arrays.asList(PersonNames.JobType.VOD, PersonNames.JobType.TV), PersonNames.Network.OTT));
    }

    @Test
    public void testMap() {
        Map<String, String> test = parser.parseMap(contents("/test.yml"), String.class);
        System.out.println(test);
        Assert.assertEquals(test, new HashMap<String, String>() {{
            put("key", "val");
            put("trash", "talk");
        }});
    }

    @Test
    public void testComplexMap() {
        Map<String, Server> servers = parser.parseMap(file("/test21.yml"), Server.class);
        System.out.println(servers);
        Assert.assertEquals(servers, new HashMap<String, Server>() {{
            put("mwjb1", new Server("1", "mwjb1.tv.megafon"));
            put("mwjb2", new Server("2", "mwjb2.tv.megafon"));
        }});
    }

    @Test
    public void testParseList() throws IOException {
        List<Server> servers = parser.parseList(contents("/test19.yml"), Server.class);
        System.out.println(servers);
        Assert.assertEquals(servers, Arrays.asList(new Server("mwjb1", "mwjb1.tv.megafon"), new Server("mwjb2", "mwjb2.tv.megafon")));
    }

    @Test
    public void testParsePrimitiveList() throws IOException {
        List<String> colors = parser.parseList(file("/test20.yml"), String.class);
        System.out.println(colors);
        Assert.assertEquals(colors, Arrays.asList("red", "orange", "black"));
    }

    @Test
    public void testDateIntervalTest() throws Exception {
        DateInterval test = parser.parse(contents("/test11.yml"), DateInterval.class);
        System.out.println(test);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Assert.assertEquals(test, new DateInterval(sdf.parse("2010-11-18 12:30"), sdf.parse("2010-11-20 14:00")));
    }

    @Test
    public void dateIntervalUnsafeTest() throws Exception {
        DateIntervalUnsafe dateInterval = parser.parse(file("/test11.yml"), DateIntervalUnsafe.class);
        System.out.println(dateInterval);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Assert.assertEquals(dateInterval, new DateIntervalUnsafe(sdf.parse("2010-11-18 12:30"), sdf.parse("2010-11-20 14:00")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongParametersInConstructor() {
        parser.parse(contents("/test.yml"), WrongStringPrimitives.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotSetParametersInConstructor() {
        parser.parse(contents("/test.yml"), NotSetStringPrimitives.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooManyConstructors() {
        parser.parse(contents("/test.yml"), TooManyConstructors.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongType() {
        parser.parse(contents("/test6.yml"), IdListWrongType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeFail() {
        parser.parse(contents("/test2.yml"), StringPrimitives.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleNames() {
        parser.parse(contents("/test6.yml"), IdListDoubleNames.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseStringPrimitivesNamesNoConstructor() {
        parser.parse(contents("/test.yml"), StringPrimitivesNames.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringPrimitivesEmptyNames() throws IOException {
        InputStream inputStream = inputStream("/test.yml");
        try {
            parser.parse(inputStream, StringPrimitivesEmptyNames.class);
        } finally {
            inputStream.close();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringPrimitivesIncorrectNames() throws IOException {
        BufferedReader reader = reader("/test.yml");
        try {
            parser.parse(reader, StringPrimitivesIncorrectNames.class);
        } finally {
            reader.close();
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void testImpossibleConversionToStraightMap() {
        parser.parseMap(file("/test10.yml"), String.class);
    }

}
