package test.data;

/**
 * Date: 12/22/13
 * Time: 12:26 AM
 *
 * @author Artem Prigoda
 */
public class Server {

    private final String id;
    private final String url;

    public Server(String id, String url) {
        this.id = id;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Server{id=" + id + ", url=" + url + "}";
    }
}
