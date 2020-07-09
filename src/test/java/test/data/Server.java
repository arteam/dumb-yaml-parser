package test.data;

import org.dumb.yaml.annotation.Names;

/**
 * Date: 12/22/13
 * Time: 12:26 AM
 *
 * @author Artem Prigoda
 */
public class Server {

    private final String id;
    private final String url;

    @Names({"id", "url"})
    public Server(String id, String url) {
        this.id = id;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;

        if (id != null ? !id.equals(server.id) : server.id != null) return false;
        if (url != null ? !url.equals(server.url) : server.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Server{id=" + id + ", url=" + url + "}";
    }
}
