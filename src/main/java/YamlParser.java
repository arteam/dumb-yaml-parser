import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 11/19/13
 * Time: 9:56 PM
 *
 * @author Artem Prigoda
 */
public class YamlParser {

    private static final Pattern PATTERN = Pattern.compile("(\\s*)(\\S+)\\s*:+\\s*(.*)");

    public YamlMap parse(String yaml) {
        List<String> lines = Arrays.asList(yaml.split("\n"));
        if (lines.size() == 0)
            throw new IllegalArgumentException("Bad param: " + yaml);

        Map<String, YamlObject> map = new HashMap<>();
        int pos = 0;
        while (pos < lines.size()) {
            ParserNewStep rv = analyze(lines, pos, -1, map);
            if (!rv.ifContinue) throw new IllegalStateException("Delimeter problems");
            pos = rv.pos;
        }

        return new YamlMap(map);
    }

    private ParserNewStep analyze(List<String> lines, int pos, int rootDelimiters, Map<String, YamlObject> map) {
        if (pos > lines.size() - 1) return new ParserNewStep(false, pos);
        String line = lines.get(pos);
        Matcher matcher = PATTERN.matcher(line);
        if (!matcher.find())
            // Don't analyze lists yet
            throw new IllegalArgumentException("Bad line: " + line);
        int amountDelimiters = matcher.group(1).length();
        String key = matcher.group(2);
        String value = matcher.group(3);
        if (amountDelimiters > rootDelimiters) {
            if (!value.isEmpty()) {
                map.put(key, new YamlPrimitive(value));
                return new ParserNewStep(true, pos + 1);
            } else {
                Map<String, YamlObject> childMap = new HashMap<>();
                // While not ended iterate
                ParserNewStep newStep;
                int nextPos = pos + 1;
                do {
                    newStep = analyze(lines, nextPos, amountDelimiters, childMap);
                    nextPos = newStep.pos;
                } while (newStep.ifContinue);
                if (childMap.isEmpty())
                    throw new IllegalArgumentException("Key " + key + " hasn't got values");
                map.put(key, new YamlMap(childMap));
                return new ParserNewStep(true, nextPos);
            }
        }
        return new ParserNewStep(false, pos);
    }
}
