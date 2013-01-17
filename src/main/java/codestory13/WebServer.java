package codestory13;

import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.simple.container.SimpleServerFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
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
            httpServer = SimpleServerFactory.create("http://localhost:" + port,
                    new DefaultResourceConfig(
                            BaseResource.class,
                            ScalaskelResource.class,
                            JajascriptResource.class,
                            JacksonJsonProvider.class,
                            CatchAllExceptions.class
                            ));
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

    @Provider
    public static class CatchAllExceptions implements ExceptionMapper<Throwable> {

        private static final Logger log = getLogger(CatchAllExceptions.class);

        @Override
        public Response toResponse(Throwable exception) {
            if (exception instanceof WebApplicationException) {
                return ((WebApplicationException) exception).getResponse();
            } else {
                // do not log stack trace, heroku breaks stack order
                log.error("Resource has throw an exception", exception.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Oups ...").build();
            }
        }
    }
}