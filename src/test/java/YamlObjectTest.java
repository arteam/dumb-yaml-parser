import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Date: 11/20/13
 * Time: 8:08 PM
 *
 * @author Artem Prigoda
 */
public class YamlObjectTest {

    ObjectParser parser = new ObjectParser();

    @Test
    public void testParseStringPrimitives()  {
        StringPrimitives test = parser.parse(file("/test.yml"), StringPrimitives.class);
        System.out.println(test);
        Assert.assertEquals(test, new StringPrimitives("val", "talk"));
    }

    @Test
    public void testInjectByFields()  {
        StringPrimitivesPlain test = parser.parse(file("/test.yml"), StringPrimitivesPlain.class);
        System.out.println(test);
        StringPrimitivesPlain plain = new StringPrimitivesPlain();
        plain.setKey("val");
        plain.setTrash("talk");
        Assert.assertEquals(test, plain);
    }

    @Test
    public void testInjectByFieldsWithAnnotation()  {
        StringPrimitivesAnnotation test = parser.parse(file("/test.yml"), StringPrimitivesAnnotation.class);
        System.out.println(test);
        StringPrimitivesAnnotation plain = new StringPrimitivesAnnotation();
        plain.setKeyValue("val");
        plain.setTrashValue("talk");
        Assert.assertEquals(test, plain);
    }

    @Test
    public void testParseNumericPrimitives()  {
        NumericPrimitives test = parser.parse(file("/test9.yml"), NumericPrimitives.class);
        System.out.println(test);
        Assert.assertThat(test, equalTo(
                new NumericPrimitives(21341241234123412L, 12121232341231231L, -1233, -3322,
                        1231231.234412421312d, 2344542.123544322323d, -323.121f, -111.333f, false, true)));
    }

    @Test
    public void testMapInject()  {
        MapInject test = parser.parse(file("/test2.yml"), MapInject.class);
        System.out.println(test);
        Assert.assertEquals(test, new MapInject(new MapInject.Key(1, 2), "talk"));
    }

    private static String file(String fileName) {
        String file = YamlObjectTest.class.getResource(fileName).getFile();
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
