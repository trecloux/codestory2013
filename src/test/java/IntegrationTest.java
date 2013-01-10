import org.junit.Rule;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.fest.assertions.Assertions.assertThat;

public class IntegrationTest {

    @Rule
    public WebServerRule webServer = new WebServerRule();

    @Test
    public void should_get_email() throws Exception {
        assertThatAnswerIs("Quelle est ton adresse email", "tometjerem@gmail.com");
    }

    @Test
    public void should_have_subscribed_to_mailing_list() throws Exception {
        assertThatAnswerIs("Es tu abonne a la mailing list(OUI/NON)", "OUI");
    }

    @Test
    public void should_be_happy() throws Exception {
        assertThatAnswerIs("Es tu heureux de participer(OUI/NON)", "OUI");
    }

    @Test
    public void should_not_be_always_yes() throws Exception {
        assertThatAnswerIs("Est ce que tu reponds toujours oui(OUI/NON)", "NON");
    }

    @Test
    public void should_ask_to_repeat_unknown_questions() throws Exception {
        assertThatAnswerIs("Qui va gagner le superball", "Vous pouvez répéter la question ?");
    }

    @Test
    public void should_ping() throws Exception {
        assertThatAnswerIs("ping", "OK");
    }

    private void assertThatAnswerIs(String question, String answer) {
        String content =
                given()
                        .port(webServer.port)
                        .param("q", question)
                        .get("/").asString();
        assertThat(content).isEqualTo(answer);
    }


}
