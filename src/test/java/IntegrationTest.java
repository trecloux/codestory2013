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

    @Test
    public void should_have_subscribed_to_mailing_list() throws Exception {
        String content =
                given()
                        .port(webServer.port)
                        .param("q", "Es tu abonne a la mailing list(OUI/NON)")
                        .get("/").asString();
        assertThat(content).isEqualTo("OUI");
    }

    @Test
    public void should_ask_to_repeat_unknown_questions() throws Exception {
        String content =
                given()
                        .port(webServer.port)
                        .param("q", "Qui va gagner le superball")
                        .get("/").asString();
        assertThat(content).contains("répéter");

    }

    @Test
    public void should_ping() throws Exception {
        String content =
                given()
                        .port(webServer.port)
                        .param("q", "ping")
                        .get("/").asString();
        assertThat(content).isEqualTo("OK");
    }

}
