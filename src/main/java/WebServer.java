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

    public WebServer() {
        start(getPort());
    }

    public WebServer (int port) {
        start(port);
    }

    private void start(int port) {
        try {
            log.info("WebServer will try to start on port {} ", port);
            httpServer = SimpleServerFactory.create("http://localhost:" + port, new DefaultResourceConfig(Resource.class));
            log.info("WebServer started. java.runtime.version : {}", System.getProperty("java.runtime.version"));
        } catch (IOException e) {
            log.error("Could not start WebServer", e);
        }
    }

    private int getPort() {
        String portEnv = System.getenv("PORT");
        if (portEnv == null) {
            return 9090;
        } else {
            return Integer.parseInt(portEnv);
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