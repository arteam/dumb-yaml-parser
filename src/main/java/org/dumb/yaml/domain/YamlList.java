package org.dumb.yaml.domain;

import java.util.List;

/**
 * Date: 11/19/13
 * Time: 9:51 PM
 *
 * @author Artem Prigoda
 */
public class YamlList implements YamlObject {

    private List<YamlObject> list;

    public YamlList(List<YamlObject> list) {
        this.list = list;
    }

    public List<YamlObject> getList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof YamlList) {
            YamlList that = (YamlList) o;
            return list == that.list || (list != null && list.equals(that.list));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return list != null ? list.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "YamlList{list=" + list + "}";
    }
}
