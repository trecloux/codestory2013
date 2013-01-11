import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class Resource {

    private Logger logger = LoggerFactory.getLogger(Resource.class);

    private Map<String, String> simpleGetResponses = new HashMap<>();
    

    public Resource() {
        simpleGetResponses.put("Quelle est ton adresse email","tometjerem@gmail.com");
        simpleGetResponses.put("Es tu abonne a la mailing list(OUI/NON)","OUI");
        simpleGetResponses.put("Es tu heureux de participer(OUI/NON)","OUI");
        simpleGetResponses.put("Est ce que tu reponds toujours oui(OUI/NON)","NON");
        simpleGetResponses.put("ping","OK");
    }

    @GET
    @Produces("text/plain;charset=utf-8")
    public String getAnswer(@QueryParam("q") String question) {
        if (simpleGetResponses.containsKey(question)) {
            return simpleGetResponses.get(question);
        }

        return "Vous pouvez répéter la question ?";
    }

    @POST
    @Path("enonce/{subjectNumber}")
    public Response postSubject(@PathParam("subjectNumber")String subjectNumber, String subjectContent) {
        logger.info("énoncé numero {} : {}", subjectNumber,subjectContent);
        return Response.created(null).build();
    }
}