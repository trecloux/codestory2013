package codestory13;

import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static com.jayway.restassured.RestAssured.expect;
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
        RestAssured.port = webServer.port;
    }

    @Test
    public void should_get_email() throws Exception {
        assertThatAnswerIs("Quelle est ton adresse email", "tometjerem@gmail.com");
        assertThatAnswerIs("Es tu abonne a la mailing list(OUI/NON)", "OUI");
        assertThatAnswerIs("Es tu heureux de participer(OUI/NON)", "OUI");
        assertThatAnswerIs("Est ce que tu reponds toujours oui(OUI/NON)", "NON");
        assertThatAnswerIs("As tu bien recu le premier enonce(OUI/NON)", "OUI");
        assertThatAnswerIs("As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)", "BOF");
        assertThatAnswerIs("ping", "OK");
        assertThatAnswerIs("Qui va gagner le superball", "Vous pouvez répéter la question ?");
        assertThatAnswerIs("As tu bien recu le second enonce(OUI/NON)", "OUI");
    }

    @Test
    public void should_send_email_once_on_unknown_get_question() throws Exception {
        given().param("q", "Comment je m'appelle ?").get("/");
        given().param("q", "Comment je m'appelle ?").get("/");
        verify(BaseResource.mailer, times(1)).sendMail(any(Email.class));
    }

    @Test
    public void should_send_email_once_on_unknown_post_subjects() throws Exception {
        given().body("Hey body").post("/enonce/1");
        given().body("Hey body").post("/enonce/1");
        verify(BaseResource.mailer, times(1)).sendMail(any(Email.class));
    }


    @Test
    public void should_not_fail_on_email_failure() throws Exception {
        doThrow(new RuntimeException("Plouf")).when(BaseResource.mailer).sendMail(any(Email.class));
        given().param("q", "Comment je m'appelle ?").get("/");
    }

    @Test
    public void should_post_subjects() throws Exception {
        given()
            .request().body("*underlined text with markdown*").
        expect()
            .statusCode(201).
        when()
            .post("/enonce/1");
    }

    @Test
    public void should_compute_scalaskel_for_8() throws Exception {
        expect()
            .body("get(1).bar", equalTo(1))
            .body("get(1).foo", equalTo(1))
            .body("get(0).foo", equalTo(8))
        .given()
            .get("/scalaskel/change/8");
    }

    @Test
    public void should_compute() throws Exception {
        assertThatAnswerIs("1 1", "2");
        assertThatAnswerIs("3*3", "9");
        assertThatAnswerIs("4-1", "3");
        assertThatAnswerIs("(1 2)*2", "6");
        assertThatAnswerIs("(1+2)/2", "1,5");
        assertThatAnswerIs("((1 2) 3 4 (5 6 7) (8 9 10)*3)/2*5", "272,5");
        assertThatAnswerIs("1,5*4", "6");
        assertThatAnswerIs("((1,1 2) 3,14 4 (5 6 7) (8 9 10)*4267387833344334647677634)/2*553344300034334349999000", "31878018903828899277492024491376690701584023926880");
        assertThatAnswerIs("(((1,1 2) 3,14 4 (5 6 7) (8 9 10)*4267387833344334647677634)/2*553344300034334349999000)/31878018903828899277492024491376690701584023926880",  "1");
    }


    @Test
    public void should_optimize_orders() throws Exception {
        String message = Resources.toString(getResource("sampleOrders.json"), UTF_8);
        given()
                .contentType(ContentType.JSON)
                .request().body(message)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("gain", equalTo(18))
                .body("path[0]", equalTo("MONAD42"))
                .body("path[1]", equalTo("LEGACY01"))
        .when()
                .post("/jajascript/optimize");
    }

    @Test
    public void should_answer_something_on_slash() throws Exception {
        String content = given().get("/").asString();
        assertThat(content).isEqualTo("Vous pouvez répéter la question ?");
    }

    private void assertThatAnswerIs(String question, String answer) {
        String content = given()
            .param("q", question)
            .get("/")
            .asString();
        assertThat(content).isEqualTo(answer);
    }

}
