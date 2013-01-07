import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.simple.container.SimpleServerFactory;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;


public class WebServer {

    private static final Logger log = getLogger(WebServer.class);
    private static Closeable httpServer;

    public static void main(String [] args) {
        new WebServer();
    }
    public WebServer () {
        try {
            httpServer = SimpleServerFactory.create("http://localhost:"+ getPort(), new DefaultResourceConfig(Resource.class));
        } catch (IOException e) {
            log.error("Could not start WebServer", e);
        }
    }

    private int getPort() {
        String port = System.getenv("PORT");
        if (port == null) {
            return 9090;
        } else {
            return Integer.parseInt(port);
        }

    }

    public void stop() {
        try {
            httpServer.close();
        } catch (IOException e) {
            log.error("Error stopping server", e);
        }
    }

}