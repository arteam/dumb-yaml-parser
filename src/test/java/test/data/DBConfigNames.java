package test.data;

import org.dumb.yaml.annotation.Name;
import org.dumb.yaml.annotation.Names;

import java.util.Map;
import java.util.Objects;

/**
 * Date: 11/22/13
 * Time: 11:32 PM
 *
 * @author Artem Prigoda
 */
@Names("data_bases")
public class DBConfigNames {

    private final Map<String, Database> db;

    public DBConfigNames(Map<String, Database> db) {
        this.db = db;
    }

    @Names({"db_driver", "url"})
    public static class Database {
        private final String driver;
        private final String url;

        public Database(String driver, String url) {
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
        if (o instanceof DBConfigNames) {
            DBConfigNames dbConfig = (DBConfigNames) o;
            return Objects.equals(db, dbConfig.db);
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBConfig{" +
                "db=" + db + '}';
    }
}
