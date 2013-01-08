import org.junit.Rule;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.fest.assertions.Assertions.assertThat;

public class IntegrationTest {

    @Rule
    public WebServerRule webServer = new WebServerRule();

    @Test
    public void should_get_email() throws Exception {
        String content =
            given()
                .port(webServer.port)
                .param("q", "Quelle est ton adresse email")
            .get("/").asString();
        assertThat(content).isEqualTo("tometjerem@gmail.com");

    }

}
