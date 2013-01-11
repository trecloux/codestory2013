package codestory13;

import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.net.ServerSocket;

import static com.google.common.base.Throwables.propagate;

public class WebServerRule extends ExternalResource {

    WebServer webServer;
    int port;

    @Override
    protected void before() throws Throwable {
        port = getNextAvailablePort();
        webServer = new WebServer(port);
    }

    @Override
    protected void after() {
        webServer.stop();
        webServer = null;
        port = -1;
    }

    private int getNextAvailablePort() {
        int unusedPort = 0;
        try {
            ServerSocket socket = new ServerSocket(0);
            unusedPort = socket.getLocalPort();
            socket.close();
        } catch (IOException e) {
            propagate(e);
        }
        return unusedPort;
    }

}
