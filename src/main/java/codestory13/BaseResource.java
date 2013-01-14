package codestory13;

import com.google.common.base.Optional;
import groovy.lang.GroovyShell;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Optional.*;
import static java.lang.System.getenv;
import static java.util.Locale.FRANCE;
import static javax.mail.Message.RecipientType.TO;
import static org.codemonkey.simplejavamail.TransportStrategy.SMTP_TLS;

@Path("/")
public class BaseResource {

    private Logger logger = LoggerFactory.getLogger(BaseResource.class);
    static List<String> knownMailKeys = new ArrayList<>();

    private static Map<String, String> staticResponses = new HashMap<>();
    static {
        staticResponses.put("Quelle est ton adresse email", "tometjerem@gmail.com");
        staticResponses.put("Es tu abonne a la mailing list(OUI/NON)", "OUI");
        staticResponses.put("Es tu heureux de participer(OUI/NON)", "OUI");
        staticResponses.put("Est ce que tu reponds toujours oui(OUI/NON)", "NON");
        staticResponses.put("Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)", "OUI");
        staticResponses.put("As tu bien recu le premier enonce(OUI/NON)", "OUI");
        staticResponses.put("ping", "OK");
        staticResponses.put("As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)", "BOF");
        staticResponses.put("As tu bien recu le second enonce(OUI/NON)", "OUI");
        staticResponses.put("As tu copie le code de ndeloof(OUI/NON/JE_SUIS_NICOLAS)", "NON");
    }

    static Mailer mailer;
    static {
        mailer = new Mailer("smtp.gmail.com", 587, "tometjerem@gmail.com", getenv("GMAIL_PASSWORD"), SMTP_TLS);
    }

    @GET
    @Produces("text/plain;charset=utf-8")
    public String getAnswer(@QueryParam("q") @DefaultValue("") String question) {
        return staticContent(question)
                .or(formula(question))
                .or(() -> defaultResponse(question));
    }

    private Optional<String> staticContent(String question) {
        return fromNullable(staticResponses.get(question));
    }

    private String defaultResponse(String question) {
        sendMailIfNewRequest("question/" + question, "GET question", question);
        return "Vous pouvez répéter la question ?";
    }


    private Optional<String> formula(String question) {
        String formula = question.replaceAll(" ", "+").replaceAll(",", ".");
        if (isFormula(formula)) {
            return eval(formula);
        } else {
            return absent();
        }
    }

    private Optional<String> eval(String formula) {
        try {
            Object result = new GroovyShell().evaluate(formula);
            if (result instanceof Number) {
                NumberFormat format = DecimalFormat.getInstance(FRANCE);
                format.setGroupingUsed(false);
                return of(format.format(result));
            } else {
                return absent();
            }
        } catch (Exception e) {
            return absent();
        }
    }

    private boolean isFormula(String formula) {
        return formula.matches("[0-9\\+\\-/\\*\\.\\(\\)]+"); // 0-9 + - / * . ()
    }


    @POST
    @Path("enonce/{subjectNumber}")
    public Response postSubject(@PathParam("subjectNumber")String subjectNumber, String subjectContent) {
        logger.info("énoncé numero {} : {}", subjectNumber,subjectContent);
        sendMailIfNewRequest("enonce/" + subjectNumber, "POST Subject", subjectContent);
        return Response.created(null).build();
    }

    private void sendMailIfNewRequest(String requestKey, String subject, String body) {
        if (! knownMailKeys.contains(requestKey)) {
            sendEmail("Code Story : Unknown " + subject, body);
            knownMailKeys.add(requestKey);
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