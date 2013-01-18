package codestory13;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.simple.container.SimpleServerFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicLong;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.slf4j.LoggerFactory.getLogger;


public class WebServer {

    private static final Logger log = getLogger(WebServer.class);
    private static Cache<String, Throwable> errors = CacheBuilder.newBuilder().maximumSize(20).build();
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
        java.util.logging.Logger.getLogger("").addHandler(new java.util.logging.ConsoleHandler());
        java.util.logging.Logger.getLogger("com.sun.jersey").setLevel(java.util.logging.Level.FINEST);
        try {
            log.info("WebServer will try to start on port {} ", port);
            httpServer = SimpleServerFactory.create("http://localhost:" + port,
                    new DefaultResourceConfig(
                            BaseResource.class,
                            ScalaskelResource.class,
                            JajascriptResource.class,
                            JacksonJsonProvider.class,
                            CatchAllExceptions.class,
                            ShowErrorsResource.class
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

        static AtomicLong atomicLong = new AtomicLong(System.currentTimeMillis());

        @Override
        public Response toResponse(Throwable exception) {
            if (exception instanceof WebApplicationException) {
                return ((WebApplicationException) exception).getResponse();
            } else {
                String errorId = generateExceptionId();
                errors.put(errorId, exception);
                log.error("Resource has throw an exception : id:[{}] message:[{}]", errorId, exception.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Oups ...").build();
            }
        }

        private String generateExceptionId() {
            long nid = atomicLong.incrementAndGet();
            return Long.toString(nid, 26);
        }
    }

    @Path("/error")
    public static  class ShowErrorsResource {

        @Path("/{id}")
        @GET
        public Response showError(@PathParam("id") String errorId) {
            Throwable error = errors.getIfPresent(errorId);
            if (error == null) {
                return Response.status(NOT_FOUND).entity("Did not found this error ... sorry").build();
            } else {
                StringWriter out = new StringWriter();
                error.printStackTrace(new PrintWriter(out));
                String stackTrace = out.toString();
                return Response.ok().type(TEXT_PLAIN_TYPE).entity(stackTrace).build();
            }
        }
    }
}