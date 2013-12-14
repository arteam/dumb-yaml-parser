package org.dumb.yaml.domain;

import java.util.Map;

/**
 * Date: 11/19/13
 * Time: 9:51 PM
 *
 * @author Artem Prigoda
 */
public class YamlMap implements YamlObject {

    private Map<String, YamlObject> map;

    public YamlMap(Map<String, YamlObject> map) {
        this.map = map;
    }

    public Map<String, YamlObject> getMap() {
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof YamlMap) {
          YamlMap that = (YamlMap) o;
            return map == that.map || (map != null && map.equals(that.map));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return map != null ? map.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "YamlMap{map=" + map + "}";
    }
}
