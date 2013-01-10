import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/")
public class Resource {

    @GET
    @Produces("text/plain;charset=utf-8")
    public String getAnswer(@QueryParam("q") String question) {
        if ("Quelle est ton adresse email".equals(question)) {
            return "tometjerem@gmail.com";
        } else if ("Es tu abonne a la mailing list(OUI/NON)".equals(question)) {
            return "OUI";
        } else if ("Es tu heureux de participer(OUI/NON)".equals(question)) {
            return "OUI";
        } else if ("Est ce que tu reponds toujours oui(OUI/NON)".equals(question)) {
            return "NON";
        } else if ("ping".equals(question)) {
            return "OK";
        } else {
            return "Vous pouvez répéter la question ?";
        }
    }
}