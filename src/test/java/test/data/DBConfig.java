package test.data;

import annotation.Name;

import java.util.Map;
import java.util.Objects;

/**
 * Date: 11/22/13
 * Time: 11:32 PM
 *
 * @author Artem Prigoda
 */
public class DBConfig {

    private final Map<String, Database> db;

    public DBConfig(@Name("data_bases") Map<String, Database> db) {
        this.db = db;
    }

    public static class Database {
        private final String driver;
        private final String url;

        public Database(@Name("db_driver") String driver, @Name("url") String url) {
            this.driver = driver;
            this.url = url;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Database) {
                Database database = (Database) o;
                return Objects.equals(driver, database.driver) && Objects.equals(url, database.url);
            }
            return true;
        }

        @Override
        public String toString() {
            return "Database{" +
                    "driver=" + driver +
                    ", url=" + url +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DBConfig) {
            DBConfig dbConfig = (DBConfig) o;
            return Objects.equals(db, dbConfig.db);
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBConfig{" +
                "db=" + db +'}';
    }
}
