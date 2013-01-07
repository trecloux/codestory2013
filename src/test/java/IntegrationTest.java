import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.fest.assertions.Assertions.assertThat;

public class IntegrationTest {

    private WebServer server;

    @Test
    public void should_get_email() throws Exception {
        String content =
            given()
                .port(9090)
                .param("q", "Quelle est ton adresse email")
            .get("/").asString();
        assertThat(content).isEqualTo("tometjerem@gmail.com");

    }

    @Before
    public void setUp() throws Exception {
        server = new WebServer();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

}
