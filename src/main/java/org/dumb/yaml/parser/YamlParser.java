package org.dumb.yaml.parser;

import org.dumb.yaml.domain.YamlList;
import org.dumb.yaml.domain.YamlMap;
import org.dumb.yaml.domain.YamlObject;
import org.dumb.yaml.domain.YamlPrimitive;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 11/19/13
 * Time: 9:56 PM
 * YAML strings parser
 *
 * @author Artem Prigoda
 */
public class YamlParser {

    private static final Pattern PATTERN = Pattern.compile("^(\\s*)(\\S+)\\s*:+\\s*([^#]*).*$");
    private static final Pattern COMMENT = Pattern.compile("\\d*#.*");

    /**
     * Parse YAML string to object tree representation
     */
    public YamlMap parse(List<String> lines) {
        if (lines.size() == 0)
            throw new IllegalArgumentException("No data to parse");

        Map<String, YamlObject> map = new HashMap<>();
        int pos = 0;
        while (pos < lines.size()) {
            ParserNewStep rv = analyze(lines, pos, -1, map);
            if (!rv.ifContinue) throw new IllegalStateException("Delimiter problems");
            pos = rv.pos;
        }

        return new YamlMap(map);
    }

    /**
     * Analyze current line and decide what org.dumb.yaml.parser should do
     *
     * @param lines          All lines
     * @param pos            current line number
     * @param rootDelimiters level of previous branch
     * @param map            previous branch map
     * @return new org.dumb.yaml.parser step
     */
    private ParserNewStep analyze(List<String> lines, int pos, int rootDelimiters, Map<String, YamlObject> map) {
        if (pos > lines.size() - 1)
            return new ParserNewStep(false, pos);

        String line = lines.get(pos);
        Matcher matcher = PATTERN.matcher(line);
        if (!matcher.find()) {
            if (line.trim().isEmpty() || COMMENT.matcher(line).find()) {
                return new ParserNewStep(true, pos + 1);
            }
            throw new IllegalArgumentException("Bad line: " + line);
        }
        int amountDelimiters = matcher.group(1).length();
        String key = matcher.group(2);
        String value = matcher.group(3).trim();
        if (amountDelimiters > rootDelimiters) {
            if (!value.isEmpty()) {
                if (map.containsKey(key)) {
                    throw new IllegalArgumentException("Map " + map + " already contains key " + key);
                }
                map.put(key, parseStringValue(value));
                return new ParserNewStep(true, pos + 1);
            } else {
                Map<String, YamlObject> childMap = new HashMap<>();
                ParserNewStep newStep;
                int nextPos = pos + 1;
                // Iterate while org.dumb.yaml.parser not jumped to upper level
                do {
                    newStep = analyze(lines, nextPos, amountDelimiters, childMap);
                    nextPos = newStep.pos;
                } while (newStep.ifContinue);
                if (childMap.isEmpty()) {
                    throw new IllegalArgumentException("Key " + key + " hasn't got values");
                }
                map.put(key, new YamlMap(childMap));
                return new ParserNewStep(true, nextPos);
            }
        } else {
            // This means child level has ended and we jump to upper level
            return new ParserNewStep(false, pos);
        }
    }

    /**
     * Parse string value (usually primitive but could be list and map as well)
     */
    private YamlObject parseStringValue(String value) {
        // Apparently regexp validation should be here
        if (value.startsWith("[") && value.endsWith("]")) {
            String[] split = value.substring(1, value.length() - 1).split(",");
            List<YamlObject> list = new ArrayList<>();
            for (String s : split) {
                list.add(new YamlPrimitive(s.trim()));
            }
            return new YamlList(list);
        } else if (value.startsWith("{") && value.endsWith("}")) {
            String[] split = value.substring(1, value.length() - 1).split(",");
            Map<String, YamlObject> childMap = new HashMap<>();
            for (String s : split) {
                String[] keyValue = s.split(":");
                String childKey = keyValue[0].trim();
                String childValue = keyValue[1].trim();
                childMap.put(childKey, new YamlPrimitive(childValue));
            }
            return new YamlMap(childMap);
        } else {
            return new YamlPrimitive(value);
        }
    }
}
