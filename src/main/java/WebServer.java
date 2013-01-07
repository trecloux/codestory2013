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
        String portEnv = System.getenv("PORT");
        int port;
        if (portEnv == null) {
            port = 9090;
        } else {
            port = Integer.parseInt(portEnv);
        }
        log.info("Application will try to start on port " + port);
        return port;
    }

    public void stop() {
        try {
            httpServer.close();
        } catch (IOException e) {
            log.error("Error stopping server", e);
        }
    }

}