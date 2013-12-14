package org.dumb.yaml.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Date: 11/19/13
 * Time: 9:51 PM
 *
 * @author Artem Prigoda
 */
public class YamlList implements YamlObject {

    @NotNull
    private List<YamlObject> list;

    public YamlList(@NotNull List<YamlObject> list) {
        this.list = list;
    }

    @NotNull
    public List<YamlObject> getList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof YamlList) {
            YamlList that = (YamlList) o;
            return list.equals(that.list);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public String toString() {
        return "YamlList{list=" + list + "}";
    }
}
