import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class Resource {

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
}