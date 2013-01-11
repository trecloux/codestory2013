import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class IntegrationTest {

    @Rule
    public WebServerRule webServer = new WebServerRule();

    @Before
    public void setUp() throws Exception {
        Resource.mailer = mock(Mailer.class);
    }

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
    public void should_have_received_subject_1() throws Exception {
        assertThatAnswerIs("As tu bien recu le premier enonce(OUI/NON)", "OUI");
    }

    @Test
    public void should_ask_to_repeat_unknown_questions() throws Exception {
        assertThatAnswerIs("Qui va gagner le superball", "Vous pouvez répéter la question ?");
    }

    @Test
    public void should_send_email_once_on_unknown_get_question() throws Exception {
        given().port(webServer.port).param("q", "Comment je m'appelle ?").get("/");
        given().port(webServer.port).param("q", "Comment je m'appelle ?").get("/");
        verify(Resource.mailer, times(1)).sendMail(any(Email.class));
    }

    @Test
    public void should_send_email_once_on_unknown_post_subjects() throws Exception {
        given().port(webServer.port).body("Hey body").post("/enonce/1");
        given().port(webServer.port).body("Hey body").post("/enonce/1");
        verify(Resource.mailer, times(1)).sendMail(any(Email.class));
    }


    @Test
    public void should_not_fail_on_email_failure() throws Exception {
        doThrow(new RuntimeException("Plouf")).when(Resource.mailer).sendMail(any(Email.class));
        given().port(webServer.port).param("q", "Comment je m'appelle ?").get("/");
    }

    @Test
    public void should_ping() throws Exception {
        assertThatAnswerIs("ping", "OK");
    }

    @Test
    public void should_manage_to_get_enonce1_from_post() throws Exception {
                given()
                        .port(webServer.port)
                        .request().body("*underlined text with markdown*").
                expect()
                        .statusCode(201).
                when()
                        .post("/enonce/1");

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
