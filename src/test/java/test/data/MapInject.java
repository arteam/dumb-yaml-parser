package test.data;

import annotation.Name;

/**
 * Date: 11/22/13
 * Time: 9:35 PM
 *
 * @author Artem Prigoda
 */
public class MapInject {
    private final Key key;
    private final String trash;

    public MapInject(@Name("key") Key key, @Name("trash") String trash) {
        this.key = key;
        this.trash = trash;
    }

    public static class Key {
        private final int test;
        private final int pen;

        public Key(@Name("test") int test, @Name("pen") int pen) {
            this.test = test;
            this.pen = pen;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (pen != key.pen) return false;
            if (test != key.test) return false;

            return true;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "test=" + test +
                    ", pen=" + pen +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapInject mapInject = (MapInject) o;

        if (key != null ? !key.equals(mapInject.key) : mapInject.key != null) return false;
        if (trash != null ? !trash.equals(mapInject.trash) : mapInject.trash != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "MapInject{" +
                "key=" + key +
                ", trash=" + trash + '}';
    }
}
