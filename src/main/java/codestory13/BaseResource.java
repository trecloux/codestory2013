package codestory13;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.mail.Message.RecipientType.TO;
import static org.codemonkey.simplejavamail.TransportStrategy.SMTP_TLS;

@Path("/")
public class BaseResource {

    private Logger logger = LoggerFactory.getLogger(BaseResource.class);
    static Mailer mailer;
    static List<String> knownMailKeys = new ArrayList<>();
    private Map<String, String> simpleGetResponses = new HashMap<>();
    

    public BaseResource() {
        initializeSimpleGetResponses();
        initializeMailer();
    }

    private void initializeMailer() {
        if (mailer == null) {
            String password = System.getenv("GMAIL_PASSWORD");
            mailer = new Mailer("smtp.gmail.com", 587, "tometjerem@gmail.com", password, SMTP_TLS);
        }
    }

    private void initializeSimpleGetResponses() {
        simpleGetResponses.put("Quelle est ton adresse email","tometjerem@gmail.com");
        simpleGetResponses.put("Es tu abonne a la mailing list(OUI/NON)","OUI");
        simpleGetResponses.put("Es tu heureux de participer(OUI/NON)","OUI");
        simpleGetResponses.put("Est ce que tu reponds toujours oui(OUI/NON)","NON");
        simpleGetResponses.put("As tu bien recu le premier enonce(OUI/NON)","OUI");
        simpleGetResponses.put("ping","OK");
    }

    @GET
    @Produces("text/plain;charset=utf-8")
    public String getAnswer(@QueryParam("q") String question) {
        if (simpleGetResponses.containsKey(question)) {
            return simpleGetResponses.get(question);
        } else if (question.matches("(\\d+)\\+(\\d+)")) {
            return add(question);
        } else if(isUnknownMailKey("question/" + question)) {
            sendEmail("Unknown GET question", question);
        }
        return "Vous pouvez répéter la question ?";
    }

    private String add(String question) {
        String[] parts = question.split("\\+");
        int left = Integer.parseInt(parts[0]);
        int right = Integer.parseInt(parts[1]);
        return String.valueOf(left + right);
    }

    @POST
    @Path("enonce/{subjectNumber}")
    public Response postSubject(@PathParam("subjectNumber")String subjectNumber, String subjectContent) {
        logger.info("énoncé numero {} : {}", subjectNumber,subjectContent);
        if (isUnknownMailKey("enonce/" + subjectNumber)) {
            sendEmail("Unknown POST Subject", subjectContent);
        }
        return Response.created(null).build();
    }

    private boolean isUnknownMailKey(String key) {
        if (knownMailKeys.contains(key)) {
            return false;
        } else {
            knownMailKeys.add(key);
            return true;
        }
    }

    private void sendEmail(String subject, String content) {
        Email email = new Email();
        email.setFromAddress("Tom et Jerem", "tometjerem@gmail.com");
        email.addRecipient("Tom et Jerem", "tometjerem@gmail.com", TO);
        email.setSubject(subject);
        email.setText(content);
        try {
            mailer.sendMail(email);
        } catch (RuntimeException e) {
            logger.error("Error sending email, subject : {}, body : {}", email.getSubject(), email.getText(), e);
        }
    }
}