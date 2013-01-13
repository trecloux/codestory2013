package codestory13;

import com.google.common.base.Optional;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static java.util.Locale.FRANCE;
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
        simpleGetResponses.put("As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)","BOF");
    }

    @GET
    @Produces("text/plain;charset=utf-8")
    public String getAnswer(@QueryParam("q") String question) {
        if (question != null) {
            if (simpleGetResponses.containsKey(question)) {
                return simpleGetResponses.get(question);
            } else {
                Optional<String> formulaResponse = formula(question);
                if (formulaResponse.isPresent()) {
                    return formulaResponse.get();
                } else if(isUnknownMailKey("question/" + question)) {
                    sendEmail("Code Story : Unknown GET question", question);
                }
            }
        }
        return "Vous pouvez répéter la question ?";
    }

    private Optional<String> formula(String question) {
        String formula = question.replaceAll(" ", "+").replaceAll(",", ".");
        try {
            Object result = new GroovyShell().evaluate(formula);
            if (result instanceof BigDecimal) {
                NumberFormat format = DecimalFormat.getInstance(FRANCE);
                format.setGroupingUsed(false);
                return of(format.format(result));
            } else {
                return of(result.toString());
            }
        } catch (GroovyRuntimeException e) {
            return absent();
        }
    }


    @POST
    @Path("enonce/{subjectNumber}")
    public Response postSubject(@PathParam("subjectNumber")String subjectNumber, String subjectContent) {
        logger.info("énoncé numero {} : {}", subjectNumber,subjectContent);
        if (isUnknownMailKey("enonce/" + subjectNumber)) {
            sendEmail("Code Story : Unknown POST Subject", subjectContent);
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