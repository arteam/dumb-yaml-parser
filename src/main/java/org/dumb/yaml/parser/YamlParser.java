package org.dumb.yaml.parser;

import org.dumb.yaml.domain.YamlList;
import org.dumb.yaml.domain.YamlMap;
import org.dumb.yaml.domain.YamlObject;
import org.dumb.yaml.domain.YamlPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 11/19/13
 * Time: 9:56 PM
 * YAML text parser
 *
 * @author Artem Prigoda
 */
public class YamlParser {

    private static final Pattern PATTERN = Pattern.compile("^(\\s*)(\\S+)\\s*:+\\s*(.*)\\s*$");
    private static final Pattern LIST_KEY_VALUE = Pattern.compile("^\\s*([^{]\\S+)\\s*:\\s*(.*)\\s*$");
    private static final Pattern LIST = Pattern.compile("^(\\s*)-\\s*(.*)\\s*$");
    private static final Pattern COMMENT = Pattern.compile("\\d*#.*");

    /**
     * Parse YAML string to an object tree representation
     */
    @NotNull
    public YamlMap parse(@NotNull List<String> lines) {
        if (lines.size() == 0)
            throw new IllegalArgumentException("No data to parse");

        Map<String, YamlObject> map = new HashMap<String, YamlObject>();
        List<YamlObject> list = new ArrayList<YamlObject>();
        int pos = 0;
        while (pos < lines.size()) {
            ParserNewStep rv = analyze(lines, pos, -1, map, list);
            if (!rv.ifContinue) throw new IllegalStateException("Delimiter problems");
            pos = rv.pos;
        }

        return new YamlMap(map);
    }

    /**
     * Analyze a current line and decide what parser should to do next
     *
     * @param lines          all lines
     * @param pos            current line number
     * @param rootDelimiters level of previous branch
     * @param map            previous branch map
     * @return new parser step
     */
    @NotNull
    private ParserNewStep analyze(@NotNull List<String> lines, int pos, int rootDelimiters,
                                  @NotNull Map<String, YamlObject> map, @NotNull List<YamlObject> list) {
        if (pos > lines.size() - 1)
            return new ParserNewStep(false, pos);

        String line = lines.get(pos);
        Matcher matcher = PATTERN.matcher(line);
        if (!matcher.find()) {
            Matcher lineMatcher = LIST.matcher(line);
            if (lineMatcher.find()) {
                if (lineMatcher.group(1).length() <= rootDelimiters) {
                    return new ParserNewStep(false, pos);
                }
                return startAnalyzeList(lines, pos, rootDelimiters, list);
            }
            if (line.trim().isEmpty() || COMMENT.matcher(line).find()) {
                return new ParserNewStep(true, pos + 1);
            }
            throw new IllegalArgumentException("Bad line: " + line);
        }
        int amountDelimiters = matcher.group(1).length();
        String key = matcher.group(2);
        String value = deleteComment(matcher.group(3));
        if (amountDelimiters > rootDelimiters) {
            if (!value.isEmpty()) {
                if (map.containsKey(key)) {
                    throw new IllegalArgumentException("Map " + map + " already contains key " + key);
                }
                map.put(key, parseStringValue(value));
                return new ParserNewStep(true, pos + 1);
            } else {
                return analyzeMap(lines, pos, amountDelimiters, key, map);
            }
        } else {
            // This means a child level has ended and we jump to an upper level
            return new ParserNewStep(false, pos);
        }
    }

    /**
     * Analyze map with key
     */
    @NotNull
    private ParserNewStep analyzeMap(@NotNull List<String> lines, int pos,
                                     int amountDelimiters, @NotNull String key,
                                     @NotNull Map<String, YamlObject> rootMap) {
        Map<String, YamlObject> childMap = new LinkedHashMap<String, YamlObject>();
        List<YamlObject> childList = new ArrayList<YamlObject>();
        ParserNewStep newStep;
        int nextPos = pos + 1;
        // Iterate while parser not jumped to upper level
        do {
            newStep = analyze(lines, nextPos, amountDelimiters, childMap, childList);
            nextPos = newStep.pos;
        } while (newStep.ifContinue);
        if (childMap.isEmpty() && childList.isEmpty()) {
            throw new IllegalArgumentException("Key " + key + " hasn't got values");
        }
        if (!childMap.isEmpty() && !childList.isEmpty()) {
            throw new IllegalStateException("Key " + key + " can't have map and list values");
        }
        rootMap.put(key, !childMap.isEmpty() ? new YamlMap(childMap) : new YamlList(childList));
        return new ParserNewStep(true, nextPos);
    }

    @NotNull
    private ParserNewStep analyzeList(@NotNull List<String> lines, int pos, int rootDelimiters,
                                      @NotNull List<YamlObject> list) {
        if (pos > lines.size() - 1)
            return new ParserNewStep(false, pos);

        String line = lines.get(pos);
        Matcher lineMatcher = LIST.matcher(line);
        if (!lineMatcher.find()) {
            return new ParserNewStep(false, pos);
        }
        int amountDelimiters = lineMatcher.group(1).length();
        String lv = deleteComment(lineMatcher.group(2));
        if (amountDelimiters > rootDelimiters) {
            Matcher matcher = LIST_KEY_VALUE.matcher(lv);
            if (!matcher.find()) {
                // If value is primitive
                list.add(parseStringValue(lv));
                return new ParserNewStep(true, pos + 1);
            }
            // Otherwise key value list
            String key = matcher.group(1);
            String value = matcher.group(2);

            ParserNewStep newStep;
            int nextPos = pos + 1;
            Map<String, YamlObject> map = new LinkedHashMap<String, YamlObject>();
            if (!value.isEmpty()) {
                // If plain key/value list
                map.put(key, parseStringValue(value));
                do {
                    newStep = analyze(lines, nextPos, amountDelimiters, map, new ArrayList<YamlObject>());
                    nextPos = newStep.pos;
                } while (newStep.ifContinue);
            } else {
                // If map key
                newStep = analyzeMap(lines, pos, amountDelimiters, key, map);
            }
            list.add(new YamlMap(map));
            return newStep;
        } else {
            return new ParserNewStep(false, pos);
        }
    }

    @NotNull
    private ParserNewStep startAnalyzeList(@NotNull List<String> lines, int pos, int rootDelimiters,
                                           @NotNull List<YamlObject> list) {
        ParserNewStep newStep;
        int nextPos = pos;
        do {
            newStep = analyzeList(lines, nextPos, rootDelimiters, list);
            nextPos = newStep.pos;
        } while (newStep.ifContinue);
        return new ParserNewStep(true, nextPos);
    }

    /**
     * Parse string value (usually primitive but could be list and map as well)
     */
    @NotNull
    private YamlObject parseStringValue(@NotNull String value) {
        if (value.startsWith("[")) {
            if (!value.endsWith("]")) {
                throw new IllegalArgumentException(value + " should have closed bracket");
            }
            String[] split = value.substring(1, value.length() - 1).split(",");
            List<YamlObject> list = new ArrayList<YamlObject>();
            for (String s : split) {
                list.add(new YamlPrimitive(s.trim()));
            }
            return new YamlList(list);
        } else if (value.startsWith("{")) {
            if (!value.endsWith("}")) {
                throw new IllegalArgumentException(value + " should have closed bracket");
            }
            if (!value.contains(":")) {
                throw new IllegalArgumentException(value + " should have key and value");
            }
            String[] split = value.substring(1, value.length() - 1).split(",");
            Map<String, YamlObject> childMap = new HashMap<String, YamlObject>();
            for (String s : split) {
                String[] keyValue = s.split(":");
                String childKey = keyValue[0].trim();
                String childValue = keyValue[1].trim();
                childMap.put(childKey, new YamlPrimitive(childValue));
            }
            return new YamlMap(childMap);
        } else {
            if (value.length() >= 2 && (value.charAt(0) == '\'' && value.charAt(value.length() - 1) == '\'')
                    || (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"')) {
                value = value.substring(1, value.length() - 1);
            }
            return new YamlPrimitive(value);
        }
    }

    @NotNull
    private String deleteComment(@NotNull String value) {
        // If comments are not escaping
        if (value.length() >= 2 && !(value.charAt(0) == '\'' && value.charAt(value.length() - 1) == '\'')
                && !(value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"')) {
            if (value.startsWith("#")) return "";
            int i = value.indexOf(" #");
            if (i != -1) {
                value = value.substring(0, i).trim();
            }
        }
        return value;
    }
}
