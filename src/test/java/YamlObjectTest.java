import org.dumb.yaml.Yaml;
import org.junit.Assert;
import org.junit.Test;
import test.data.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.core.IsEqual.equalTo;
import static util.FileUtils.file;

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
        StringPrimitives test = parser.parse(file("/test.yml"), StringPrimitives.class);
        System.out.println(test);
        Assert.assertEquals(test, new StringPrimitives("val", "talk"));
    }

    @Test
    public void testInjectByFields() {
        StringPrimitivesPlain test = parser.parse(file("/test.yml"), StringPrimitivesPlain.class);
        System.out.println(test);
        StringPrimitivesPlain plain = new StringPrimitivesPlain();
        plain.setKey("val");
        plain.setTrash("talk");
        Assert.assertEquals(test, plain);
    }

    @Test
    public void testInjectByFieldsWithAnnotation() {
        StringPrimitivesAnnotation test = parser.parse(file("/test.yml"), StringPrimitivesAnnotation.class);
        System.out.println(test);
        StringPrimitivesAnnotation plain = new StringPrimitivesAnnotation();
        plain.setKeyValue("val");
        plain.setTrashValue("talk");
        Assert.assertEquals(test, plain);
    }

    @Test
    public void testParseNumericPrimitives() {
        NumericPrimitives test = parser.parse(file("/test9.yml"), NumericPrimitives.class);
        System.out.println(test);
        Assert.assertThat(test, equalTo(
                new NumericPrimitives(21341241234123412L, 12121232341231231L, -1233, -3322,
                        1231231.234412421312d, 2344542.123544322323d, -323.121f, -111.333f, false, true)));
    }

    @Test
    public void testMapInject() {
        MapInject test = parser.parse(file("/test2.yml"), MapInject.class);
        System.out.println(test);
        Assert.assertEquals(test, new MapInject(new MapInject.Key(1, 2), "talk"));
    }

    @Test
    public void testListInject() {
        IdList test = parser.parse(file("/test6.yml"), IdList.class);
        System.out.println(test);
        Assert.assertEquals(test, new IdList(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testInnerMap() {
        DBConfig test = parser.parse(file("/test4.yml"), DBConfig.class);
        System.out.println(test);
        Assert.assertEquals(test, new DBConfig(new HashMap<String, DBConfig.Database>() {{
            put("cms", new DBConfig.Database("Post gres", "52.15:cms"));
            put("sms", new DBConfig.Database("Post gres", "52.15:sms"));
        }}));
    }

    @Test
    public void testPerson() {
        Person test = parser.parse(file("/test10.yml"), Person.class);
        System.out.println(test);
        Assert.assertEquals(test, new Person("\"foo\"", "foo@mail.com", 12,
                Arrays.asList(Person.JobType.VOD, Person.JobType.TV), Person.Network.OTT));
    }

    @Test
    public void testDateIntervalTest() throws Exception {
        DateInterval test = parser.parse(file("/test11.yml"), DateInterval.class);
        System.out.println(test);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Assert.assertEquals(test, new DateInterval(sdf.parse("2010-11-18 12:30"), sdf.parse("2010-11-20 14:00")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongParametersInConstructor() {
        parser.parse(file("/test.yml"), WrongStringPrimitives.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotSetParametersInConstructor() {
        parser.parse(file("/test.yml"), NotSetStringPrimitives.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooManyConstructors() {
        parser.parse(file("/test.yml"), TooManyConstructors.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongType() {
        parser.parse(file("/test6.yml"), IdListWrongType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeFail() {
        parser.parse(file("/test2.yml"), StringPrimitives.class);
    }
}
