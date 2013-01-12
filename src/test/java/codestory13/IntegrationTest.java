package codestory13;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class IntegrationTest {

    @ClassRule
    public static WebServerRule webServer = new WebServerRule();

    @Before
    public void setUp() throws Exception {
        BaseResource.mailer = mock(Mailer.class);
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
        verify(BaseResource.mailer, times(1)).sendMail(any(Email.class));
    }

    @Test
    public void should_send_email_once_on_unknown_post_subjects() throws Exception {
        given().port(webServer.port).body("Hey body").post("/enonce/1");
        given().port(webServer.port).body("Hey body").post("/enonce/1");
        verify(BaseResource.mailer, times(1)).sendMail(any(Email.class));
    }


    @Test
    public void should_not_fail_on_email_failure() throws Exception {
        doThrow(new RuntimeException("Plouf")).when(BaseResource.mailer).sendMail(any(Email.class));
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

    @Test
    public void should_compute_scalaskel_for_8() throws Exception {
        given()
                .port(webServer.port)
        .expect()
                .body("get(1).bar", equalTo(1))
                .body("get(1).foo", equalTo(1))
                .body("get(0).foo", equalTo(8))
        .when()
                .get("/scalaskel/change/8");
    }

    @Test
    public void should_add() throws Exception {
        assertThatAnswerIs("1 1", "2");
    }

    @Test
    public void should_multiply() throws Exception {
        assertThatAnswerIs("3*3", "9");
    }

    @Test
    public void should_substract() throws Exception {
        assertThatAnswerIs("4-1", "3");
    }

    @Test
    public void should_compute_formula() throws Exception {
        assertThatAnswerIs("(1 2)*2", "6");
        assertThatAnswerIs("(1+2)/2", "1,5");
        assertThatAnswerIs("((1 2) 3 4 (5 6 7) (8 9 10)*3)/2*5", "272,5");
        assertThatAnswerIs("1,5*4", "6");
        assertThatAnswerIs("((1,1+2)+3,14+4+(5+6+7)+(8+9+10)*4267387833344334647677634)/2*553344300034334349999000", "3,18780189038289E49");
    }


}
