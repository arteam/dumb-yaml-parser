import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Date: 11/20/13
 * Time: 8:08 PM
 *
 * @author Artem Prigoda
 */
public class YamlObjectTest {

    ObjectParser parser = new ObjectParser();

    @Test
    public void testParseBaseYaml()  {
        TestObject test = parser.parse(file("/test.yml"), TestObject.class);
        System.out.println(test);
        Assert.assertEquals(test, new TestObject("val", "talk"));
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
