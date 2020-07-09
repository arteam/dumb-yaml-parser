package org.dumb.yaml.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Date: 11/19/13
 * Time: 9:51 PM
 *
 * @author Artem Prigoda
 */
public class YamlMap implements YamlObject {

    @NotNull
    private final Map<String, YamlObject> map;

    public YamlMap(@NotNull Map<String, YamlObject> map) {
        this.map = map;
    }

    @NotNull
    public Map<String, YamlObject> getMap() {
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof YamlMap) {
            YamlMap that = (YamlMap) o;
            return map.equals(that.map);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return "YamlMap{map=" + map + "}";
    }
}
